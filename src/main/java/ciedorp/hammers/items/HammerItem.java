package ciedorp.hammers.items;

import ciedorp.hammers.interfaces.HammerStack;
import ciedorp.hammers.tags.ModBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class HammerItem extends MiningToolItem {
    private final TagKey<Block> effectiveBlocks = ModBlockTags.HAMMER_MINEABLE;
    public HammerItem(float attackDamage, float attackSpeed, ToolMaterial material, Item.Settings settings) {
        super(attackDamage, attackSpeed, material, ModBlockTags.HAMMER_MINEABLE, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if (stack.getItem() instanceof HammerItem) {
            HammerStack hammerStack = (HammerStack) (Object) stack;
            tooltip.add(Text.literal("HammerInfo:").formatted(Formatting.GRAY));
            tooltip.add(Text.literal(" Durability " + hammerStack.getHammerDurability()).formatted(Formatting.BLUE));
            tooltip.add(Text.literal(" Size " + hammerStack.getSize()).formatted(Formatting.BLUE));
            tooltip.add(Text.literal(" Speed " + hammerStack.getSpeed()).formatted(Formatting.BLUE));
            tooltip.add(Text.of(""));
        }
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.round(13.0f - (float)stack.getDamage() * 13.0f / (float)stack.getMaxDamage());
    }


    @Override
    public int getItemBarColor(ItemStack stack) {
        float f = Math.max(0.0f, ((float)stack.getMaxDamage() - (float)stack.getDamage()) / (float)stack.getMaxDamage());
        return MathHelper.hsvToRgb(f / 3.0f, 1.0f, 1.0f);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        HammerStack hammerStack = (HammerStack) (Object) stack;
        return state.isIn(this.effectiveBlocks) ? this.miningSpeed * (1.0f + (float)(hammerStack.getSpeed() - 1) * 1.65f) : 1.0f; //TODO Right speed for insta mining bedrock ores with efficiency 5
    }
}
