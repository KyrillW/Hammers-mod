package ciedorp.hammers.util;

import ciedorp.hammers.interfaces.HammerMining;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.List;

public class BlockBreaker {
    public static boolean tryBreakingSurroundingBlocks(World world, PlayerEntity player, List<BlockPos> filteredSurroundingBlocks){
        ServerPlayerInteractionManager interactionManager = ((ServerPlayerEntity) player).interactionManager;
        HammerMining hammerMining = (HammerMining) interactionManager;
        hammerMining.setHammerIsMining(true);
        for (BlockPos pos : filteredSurroundingBlocks) {
            BlockState state = world.getBlockState(pos);
            if (state.isAir()) {
                continue;
            }
            Block block = state.getBlock();
            block.onBreak(world, pos, state, player);
            if (!interactionManager.tryBreakBlock(pos)) {
                continue;
            }

            boolean result = PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(world, player, pos, state, world.getBlockEntity(pos));
            if(!result) {
                continue;
            }

            boolean bl = world.removeBlock(pos, false);
            if (bl) {
                block.onBroken(world, pos, state);
            }

            if (interactionManager.getGameMode() != GameMode.CREATIVE) {
                boolean bl2 = player.canHarvest(state);
                player.getMainHandStack().postMine(world, state, pos, player);
                if (bl && bl2) {
                    block.afterBreak(world, player, pos, state, world.getBlockEntity(pos), player.getMainHandStack());
                }
            }

        }
        hammerMining.setHammerIsMining(false);
        return true;
    }
}
