package ciedorp.hammers.items;

import ciedorp.hammers.interfaces.HammerStack;
import ciedorp.hammers.tags.ModBlockTags;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class HammerItem extends MiningToolItem {
    private int speed = 1;

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
            tooltip.add(Text.literal(" Speed " + speed).formatted(Formatting.BLUE));
            tooltip.add(Text.of(""));
        }
    }
}
