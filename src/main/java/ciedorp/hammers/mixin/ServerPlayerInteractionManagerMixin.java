package ciedorp.hammers.mixin;

import ciedorp.hammers.interfaces.HammerMining;
import ciedorp.hammers.interfaces.HammerStack;
import ciedorp.hammers.items.HammerItem;
import ciedorp.hammers.tags.ModBlockTags;
import ciedorp.hammers.util.BlockBreaker;
import ciedorp.hammers.util.SurroudingPosititons;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ServerPlayerInteractionManager.class, priority = 1001)
public class ServerPlayerInteractionManagerMixin implements HammerMining {

    @Final @Shadow protected ServerPlayerEntity player;
    @Shadow protected ServerWorld world;
    @Unique private boolean isMining = false;

    @Inject(
        method = "tryBreakBlock",
        at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V"
        ),
        cancellable = true
    )
    private void breakBlocks(BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        if (!world.getBlockState(pos).isIn(ModBlockTags.HAMMER_MINEABLE)) {
            return;
        }
        ItemStack heldStack = player.getMainHandStack();
        Item heldItem = heldStack.getItem();
        if (!(heldItem instanceof HammerItem hammer)) {
            return;
        }
        HammerStack hammerStack = (HammerStack) (Object) heldStack;
        List<BlockPos> seeableBlocks = SurroudingPosititons.getSurroundingBlocks(world, player, hammerStack.getSize());
        if (seeableBlocks.isEmpty()) {
            return;
        }
        List<BlockPos> filteredBlocks = SurroudingPosititons.getFilteredSurroundingBlocks(world, player, seeableBlocks);

        boolean result = isMining || BlockBreaker.tryBreakingSurroundingBlocks(world, player, filteredBlocks, pos);
        if (result) {
            cir.setReturnValue(true);
        }

    }

    @Override
    public boolean hammerIsMining() {
        return isMining;
    }

    @Override
    public void setHammerIsMining(boolean isMining) {
        this.isMining = isMining;
    }

}
