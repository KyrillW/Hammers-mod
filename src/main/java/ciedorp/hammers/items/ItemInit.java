package ciedorp.hammers.items;

import ciedorp.hammers.Hammers;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemInit {

    public static final HammerItem DIAMOND_HAMMER = new HammerItem(0, 0, ToolMaterials.DIAMOND, new Item.Settings());

    public static final SizeCoreItem SIZE_CORE = new SizeCoreItem(new Item.Settings());
    public static final SizeUpgradeItem SIZE_UPGRADE = new SizeUpgradeItem(new Item.Settings().maxCount(1));

    public static void registration() {
        // Register Diamond Hammer
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "diamond_hammer"), DIAMOND_HAMMER);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(DIAMOND_HAMMER);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(DIAMOND_HAMMER);
        });

        // Register Size Core
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "size_core"), SIZE_CORE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(SIZE_CORE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(SIZE_CORE);
        });

        // Register Size Upgrade
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "size_upgrade"), SIZE_UPGRADE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(SIZE_UPGRADE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(SIZE_UPGRADE);
        });
    }
}
