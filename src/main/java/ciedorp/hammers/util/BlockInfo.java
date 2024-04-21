package ciedorp.hammers.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockInfo {
    public static float blockBreakingTime(World world, BlockState state, BlockPos pos, PlayerEntity player){
        float f = state.getHardness(world, pos);
        if (f == -1.0f) {
            return 0.0f;
        }
        int i = player.canHarvest(state) ? 30 : 100;
        return player.getBlockBreakingSpeed(state) / f / i;
    }

    public static List<Integer> makeListFromBlockPos(BlockPos pos){
        List<Integer> list = new ArrayList<>();
        list.add(pos.getX());
        list.add(pos.getY());
        list.add(pos.getZ());
        return list;
    }
}
