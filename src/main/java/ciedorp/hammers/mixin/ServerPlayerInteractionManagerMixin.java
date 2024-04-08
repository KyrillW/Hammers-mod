package ciedorp.hammers.mixin;

import ciedorp.hammers.items.HammerItem;
import ciedorp.hammers.tags.ModBlockTags;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerInteractionManager.class, priority = 1001)
public class ServerPlayerInteractionManagerMixin {

    @Final @Shadow protected ServerPlayerEntity player;
    @Shadow protected ServerWorld world;

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
        Item heldItem = player.getMainHandStack().getItem();
        if (!(heldItem instanceof HammerItem hammer)) {
            return;
        }

        boolean result = hammer.isMining() || hammer.tryBreakingSurroundingBlocks(world, player);
        if (result) {
            cir.setReturnValue(true);
        }

    }
    
}
