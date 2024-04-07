package ciedorp.hammers.util;

import net.minecraft.entity.player.PlayerEntity;

public class ReachDistance {
    public static double getReachDistance(PlayerEntity playerEntity) {
        return playerEntity.isCreative() ? 5.0F : 4.5F;
    }
}
