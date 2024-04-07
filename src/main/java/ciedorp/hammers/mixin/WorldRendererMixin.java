package ciedorp.hammers.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ciedorp.hammers.Hammers;
import ciedorp.hammers.items.HammerItem;
import ciedorp.hammers.util.AppendedObjectIterator;
import ciedorp.hammers.util.SurroudingPosititons;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BlockBreakingInfo;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;

@Mixin(WorldRenderer.class)
@Environment(EnvType.CLIENT)
public class WorldRendererMixin {
   
    @Shadow @Final private MinecraftClient client;
    @Shadow private ClientWorld world;
    @Shadow @Final private Long2ObjectMap<SortedSet<BlockBreakingInfo>> blockBreakingProgressions;
    
    @Inject(at = @At("HEAD"), method = "drawBlockOutline", cancellable = true)
    private void drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, CallbackInfo ci){
        if (!(entity instanceof PlayerEntity player)) {
            return;
        }
        Item tool = player.getMainHandStack().getItem();
        if (!(tool instanceof HammerItem hammer)) {
            return;
        }
        if (!(tool.isSuitableFor(state))) {
            return;
        }

        Vec3d camera = new Vec3d(cameraX, cameraY, cameraZ);
        Vec3d blockPos = pos.toCenterPos();
        BlockHitResult blockHitResult = world.raycast(new RaycastContext(camera, blockPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
        Direction direction = blockHitResult.getSide();

        ArrayList<BlockPos> seeableBlocks = SurroudingPosititons.getSurroundingBlocks(pos, direction);
        hammer.setSurroundingBlocksPos(seeableBlocks);

        List<VoxelShape> outlineShapes = new ArrayList<>();
        outlineShapes.add(VoxelShapes.empty());
        
        if (client.crosshairTarget instanceof BlockHitResult crosshairTarget){
            BlockPos crosshairPos = crosshairTarget.getBlockPos();

            for (BlockPos position : seeableBlocks) {
                BlockPos diffPos = position.subtract(crosshairPos);
                outlineShapes.set(0, VoxelShapes.union(outlineShapes.get(0), VoxelShapes.fullCube().offset(diffPos.getX(), diffPos.getY(), diffPos.getZ())));
            }

            outlineShapes.forEach(shape -> drawCuboidShapeOutline(matrices, vertexConsumer, shape,
                (double) crosshairPos.getX() - cameraX,
                (double) crosshairPos.getY() - cameraY,
                (double) crosshairPos.getZ() - cameraZ,
                0.08f, 0.8f, 0.996f, 1));

            ci.cancel();
        }
    }

    @Invoker("drawCuboidShapeOutline")
    public static void drawCuboidShapeOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {
        throw new AssertionError();
    }

    @ModifyVariable(method = "render",
                    at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectSet;iterator()Lit/unimi/dsi/fastutil/objects/ObjectIterator;",
                             shift = At.Shift.BY, by = 2), ordinal = 0)
    private ObjectIterator<Long2ObjectMap.Entry<SortedSet<BlockBreakingInfo>>> appendBlockBreakingProgressions(ObjectIterator<Long2ObjectMap.Entry<SortedSet<BlockBreakingInfo>>> originalIterator) {
        return new AppendedObjectIterator<>(originalIterator, getCurrentExtraBreakingInfos());
    }

    @Unique
    private Long2ObjectMap<BlockBreakingInfo> getCurrentExtraBreakingInfos() {
        assert client.player != null;
        ItemStack heldStack = this.client.player.getInventory().getMainHandStack();

        // make sure we should display the outline based on the tool
        if (heldStack.getItem() instanceof HammerItem) {
            HitResult crosshairTarget = client.crosshairTarget;

            // ensure we're not displaying an outline on a creeper or air
            if (crosshairTarget instanceof BlockHitResult) {
                BlockPos crosshairPos = ((BlockHitResult) crosshairTarget).getBlockPos();
                SortedSet<BlockBreakingInfo> infos = this.blockBreakingProgressions.get(crosshairPos.asLong());

                // make sure current block breaking progress is valid
                if (infos != null && !infos.isEmpty()) {
                    BlockBreakingInfo breakingInfo = infos.last();
                    int stage = breakingInfo.getStage();

                    // collect positions for displaying outlines at
                    List<BlockPos> positions = SurroudingPosititons.getSurroundingBlocks(world, client.player, crosshairPos);
                    Long2ObjectMap<BlockBreakingInfo> map = new Long2ObjectLinkedOpenHashMap<>(positions.size());

                    // filter positions
                    for (BlockPos position : positions) {
                        BlockBreakingInfo info = new BlockBreakingInfo(breakingInfo.hashCode(), position);
                        info.setStage(stage);
                        map.put(position.asLong(), info);
                    }

                    return map;
                }
            }
        }
        
        return Long2ObjectMaps.emptyMap();
    }
}
