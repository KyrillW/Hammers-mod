package ciedorp.hammers.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SurroudingPosititons {

    private SurroudingPosititons() {
        throw new IllegalStateException("Utility class");
    }

    private static final ArrayList<Vec3i> surroundingPos = new ArrayList<>();

    static {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    surroundingPos.add(new Vec3i(x, y, z));
                }
            }
        }
    }

    public static List<BlockPos> getSurroundingBlocks(BlockPos pos, Direction direction){
        ArrayList<BlockPos> surroundingBlocks = new ArrayList<>();

        for (Vec3i vec3i : surroundingPos) {
            if(direction == Direction.DOWN || direction == Direction.UP){
                if (vec3i.getY() == 0) {
                    surroundingBlocks.add(pos.add(vec3i));
                }
            }else if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                if (vec3i.getZ() == 0) {
                    surroundingBlocks.add(pos.add(vec3i));
                }
            }else if (direction == Direction.EAST || direction == Direction.WEST) {
                if (vec3i.getX() == 0) {
                    surroundingBlocks.add(pos.add(vec3i));
                }
            }
        }

        return surroundingBlocks;
    }

    public static List<BlockPos> getSurroundingBlocks(World world, PlayerEntity player, BlockPos pos){
        Vec3d cameraPos = player.getCameraPosVec(1);
        Vec3d rotation = player.getRotationVec(1);
        double reachDistance = ReachDistance.getReachDistance(player);
        Vec3d combined = cameraPos.add(rotation.x * reachDistance, rotation.y * reachDistance, rotation.z * reachDistance);

        BlockHitResult blockHitResult = world.raycast(new RaycastContext(cameraPos, combined, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            Direction direction = blockHitResult.getSide();
            return getSurroundingBlocks(pos, direction);
        }
        return new ArrayList<>();
    }
}
