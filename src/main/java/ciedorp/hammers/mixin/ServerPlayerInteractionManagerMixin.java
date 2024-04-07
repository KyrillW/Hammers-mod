package ciedorp.hammers.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ciedorp.hammers.items.HammerItem;

@Mixin(value = ServerPlayerInteractionManager.class, priority = 1001)
public class ServerPlayerInteractionManagerMixin {

    @Shadow public ServerPlayerEntity player;
    @Shadow public ServerWorld world;

    @Inject(
            method = "tryBreakBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V"
            ),
            cancellable = true
    )
    private void breakBlocks(BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        Item heldItem = player.getMainHandStack().getItem();
        if (!(heldItem instanceof HammerItem)) {
            return;
        }
        HammerItem hammer = (HammerItem) heldItem;

        boolean result = hammer.getMining() || hammer.tryBreakingSurroundingBlocks(world, player);
        if (result) {
            cir.setReturnValue(true);
        }

    }
    
}
