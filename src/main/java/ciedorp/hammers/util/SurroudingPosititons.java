package ciedorp.hammers.util;

import ciedorp.hammers.tags.ModBlockTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

    private static ArrayList<Vec3i> surroundingPos = new ArrayList<>();
    private static BlockPos middleBlock = new BlockPos(0, 0, 0);

    private static void fillSurroundingPos(int size, Direction direction) {
        surroundingPos.clear();
        if (direction == Direction.DOWN || direction == Direction.UP) {
            for (int x = -size; x <= size; x++) {
                for (int y = -size; y <= size; y++) {
                    for (int z = -size; z <= size; z++) {
                        surroundingPos.add(new Vec3i(x, y, z));
                    }
                }
            }
        } else {
            for (int x = -size; x <= size; x++) {
                for (int y = -1; y <= size * 2 - 1; y++) {
                    for (int z = -size; z <= size; z++) {
                        surroundingPos.add(new Vec3i(x, y, z));
                    }
                }
            }
        }
    }

    public static List<BlockPos> getSurroundingBlocks(BlockPos pos, Direction direction, int size){
        middleBlock = pos;
        fillSurroundingPos(size, direction);

        List<Vec3i> copyOfSurroundingPos = new ArrayList<>(surroundingPos);
        ArrayList<BlockPos> surroundingBlocks = new ArrayList<>();
        for (Vec3i vec3i : copyOfSurroundingPos) {
            if (vec3i != null) {
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
        }
        return new ArrayList<>(surroundingBlocks);
    }

    public static List<BlockPos> getSurroundingBlocks(World world, PlayerEntity player, int size){
        Vec3d cameraPos = player.getCameraPosVec(1);
        Vec3d rotation = player.getRotationVec(1);
        double reachDistance = ReachDistance.getReachDistance(player);
        Vec3d combined = cameraPos.add(rotation.x * reachDistance, rotation.y * reachDistance, rotation.z * reachDistance);

        BlockHitResult blockHitResult = world.raycast(new RaycastContext(cameraPos, combined, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            Direction direction = blockHitResult.getSide();
            return getSurroundingBlocks(blockHitResult.getBlockPos(), direction, size);
        }
        return new ArrayList<>();
    }

    public static List<BlockPos> getFilteredSurroundingBlocks(World world, PlayerEntity player, List<BlockPos> posList) {
        ArrayList<BlockPos> list = new ArrayList<>();
        if (!world.getBlockState(middleBlock).isIn(ModBlockTags.HAMMER_MINEABLE)) {
            return list;
        }
        ItemStack heldStack = player.getMainHandStack();
        int mineableCount = heldStack.getMaxDamage() - heldStack.getDamage();
        float middleBlockBreakDelta = BlockInfo.blockBreakingTime(world, world.getBlockState(middleBlock), middleBlock, player);
        for (BlockPos blockPos : posList) {
            if (!world.getBlockState(blockPos).isIn(ModBlockTags.HAMMER_MINEABLE) || world.getBlockState(blockPos).isAir()) {
                continue;
            }
            float breakTime = BlockInfo.blockBreakingTime(world, world.getBlockState(blockPos), blockPos, player);
            if (breakTime >= middleBlockBreakDelta || breakTime >= 1){
                list.add(blockPos);
            }
            if (list.size() >= mineableCount) {
                return list;
            }
        }
        return list;
    }
}
