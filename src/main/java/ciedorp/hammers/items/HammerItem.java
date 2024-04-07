package ciedorp.hammers.items;

import java.util.ArrayList;

import ciedorp.hammers.Hammers;
import ciedorp.hammers.tags.ModBlockTags;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HammerItem extends MiningToolItem {
    private boolean isMining = false;
    private ArrayList<BlockPos> surroundingBlocks = new ArrayList<>();

    public HammerItem(float attackDamage, float attackSpeed, ToolMaterial material, Item.Settings settings) {
        super(attackDamage, attackSpeed, material, ModBlockTags.HAMMER_MINEABLE, settings);
    }

    public boolean getMining(){
        return isMining;
    }

    public void setMining(boolean isMining){
        this.isMining = isMining;
    }

    public ArrayList<BlockPos> getSurroundingBlocksPos(){
        return surroundingBlocks;
    }

    public void setSurroundingBlocksPos(ArrayList<BlockPos> posList){
        surroundingBlocks = posList;
    }
    
    public boolean tryBreakingSurroundingBlocks(World world, PlayerEntity player){
        ServerPlayerInteractionManager interactionManager = ((ServerPlayerEntity) player).interactionManager;
        this.isMining = true;
        for (BlockPos pos : surroundingBlocks) {
            BlockState state = world.getBlockState(pos);
            state.getBlock().onBreak(world, pos, state, player);
            if (!interactionManager.tryBreakBlock(pos)) {
                continue;
            }

            boolean result = PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(world, player, pos, state, world.getBlockEntity(pos));
            if(!result) {
                continue;
            }

            boolean bl = world.removeBlock(pos, false);
            if (bl) {
                state.getBlock().onBroken(world, pos, state);
            }
        }
        this.isMining = false;
        return true;
    }
}
