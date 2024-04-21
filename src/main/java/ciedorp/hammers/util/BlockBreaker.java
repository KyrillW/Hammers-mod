package ciedorp.hammers.util;

import ciedorp.hammers.interfaces.HammerMining;
import ciedorp.hammers.interfaces.HammerStack;
import ciedorp.hammers.items.ItemInit;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockBreaker {
    private static Random random = new Random();
    public static boolean tryBreakingSurroundingBlocks(World world, PlayerEntity player, List<BlockPos> filteredSurroundingBlocks, BlockPos middleBlock){
        ServerPlayerInteractionManager interactionManager = ((ServerPlayerEntity) player).interactionManager;
        HammerMining hammerMining = (HammerMining) interactionManager;
        hammerMining.setHammerIsMining(true);

        ItemStack heldStack = player.getMainHandStack();
        HammerStack hammerStack = (HammerStack) (Object) heldStack;

        assert hammerStack != null;
        boolean lucky = isLucky(hammerStack);

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

                ItemStack heldStack2 = heldStack.copy();
                heldStack.postMine(world, state, pos, player);
                if (bl && bl2) {
                    if (lucky) {
                        ItemStack luckyDrop = luckyItem();
                        Block.dropStack(world, middleBlock, luckyDrop);
                        MutableText notification = Text.translatable(luckyDrop.getItem().getTranslationKey()).formatted(Formatting.BLUE);
                        notification.append(Text.literal(" dropped!").formatted(Formatting.BLUE));
                        player.sendMessage(notification, true);
                        lucky = false;
                    }
                    block.afterBreak(world, player, pos, state, world.getBlockEntity(pos), heldStack2);
                }
            }

        }
        hammerMining.setHammerIsMining(false);
        return true;
    }

    private static boolean isLucky(HammerStack hammerStack) {
        for (int i = 0; i < hammerStack.getSize(); i++) {
            int luckyInt = random.nextInt(250);
            if (luckyInt == 0) {
                return true;
            }
        }
        return false;
    }

    private static ItemStack luckyItem() {
        int luckyInt = random.nextInt(ItemInit.HammerCores.size());
        return new ItemStack(ItemInit.HammerCores.get(luckyInt));
    }
}
