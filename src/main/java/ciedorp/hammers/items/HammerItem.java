package ciedorp.hammers.items;

import ciedorp.hammers.tags.ModBlockTags;
import ciedorp.hammers.util.BlockInfo;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class HammerItem extends MiningToolItem {
    private boolean isMining = false;
    private List<BlockPos> surroundingBlocks = new ArrayList<>();
    private BlockPos middleBlock = new BlockPos(0, 0,  0);
    private int size = 1;
    private int speed = 1;
    private int durability = 1;

    public HammerItem(float attackDamage, float attackSpeed, ToolMaterial material, Item.Settings settings) {
        super(attackDamage, attackSpeed, material, ModBlockTags.HAMMER_MINEABLE, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("HammerInfo:").formatted(Formatting.GRAY));
        tooltip.add(Text.literal(" Durability " + durability).formatted(Formatting.BLUE));
        tooltip.add(Text.literal(" Size " + size).formatted(Formatting.BLUE));
        tooltip.add(Text.literal(" Speed " + speed).formatted(Formatting.BLUE));
        tooltip.add(Text.of(""));
    }

    public boolean isMining(){
        return isMining;
    }

    public void setSurroundingBlocksPos(List<BlockPos> posList){
        surroundingBlocks = posList;
        if (surroundingBlocks.size() > 4){
            middleBlock = surroundingBlocks.get(4);
        }
    }

    public List<BlockPos> getFilteredSurroundingBlocks(World world, PlayerEntity player) {
        ArrayList<BlockPos> list = new ArrayList<>();
        if (!world.getBlockState(middleBlock).isIn(ModBlockTags.HAMMER_MINEABLE)) {
            return list;
        }
        float middleBlockBreakDelta = BlockInfo.blockBreakingTime(world, world.getBlockState(middleBlock), middleBlock, player);
        for (BlockPos blockPos : surroundingBlocks) {
            if (!world.getBlockState(blockPos).isIn(ModBlockTags.HAMMER_MINEABLE)) {
                continue;
            }
            float breakTime = BlockInfo.blockBreakingTime(world, world.getBlockState(blockPos), blockPos, player);
            if (breakTime >= middleBlockBreakDelta || breakTime >= 1){
                list.add(blockPos);
            }
        }
        return list;
    }

    public boolean tryBreakingSurroundingBlocks(World world, PlayerEntity player){
        List<BlockPos> filteredSurroundingBlocks = getFilteredSurroundingBlocks(world, player);
        ServerPlayerInteractionManager interactionManager = ((ServerPlayerEntity) player).interactionManager;
        this.isMining = true;
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

            boolean bl2 = player.canHarvest(state);
            player.getMainHandStack().postMine(world, state, pos, player);
            if (bl && bl2) {
                block.afterBreak(world, player, pos, state, world.getBlockEntity(pos), player.getMainHandStack());
            }
        }
        this.isMining = false;
        surroundingBlocks.clear();
        return true;
    }
}
