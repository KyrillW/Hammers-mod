package ciedorp.hammers.items;

import ciedorp.hammers.Hammers;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {

    public static final HammerItem STONE_HAMMER = new HammerItem(0, 0, ToolMaterials.STONE, new Item.Settings());
    public static final HammerItem IRON_HAMMER = new HammerItem(0, 0, ToolMaterials.IRON, new Item.Settings());
    public static final HammerItem GOLD_HAMMER = new HammerItem(0, 0, ToolMaterials.GOLD, new Item.Settings());
    public static final HammerItem DIAMOND_HAMMER = new HammerItem(0, 0, ToolMaterials.DIAMOND, new Item.Settings());
    public static final HammerItem NETHERITE_HAMMER = new HammerItem(0, 0, ToolMaterials.NETHERITE, new Item.Settings());

    public static final SizeCoreItem SIZE_CORE = new SizeCoreItem(new Item.Settings().rarity(Rarity.RARE));
    public static final SizeUpgradeItem SIZE_UPGRADE = new SizeUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC));
    public static final DurabilityCoreItem DURABILITY_CORE = new DurabilityCoreItem(new Item.Settings().rarity(Rarity.RARE));
    public static final DurabilityUpgradeItem DURABILITY_UPGRADE = new DurabilityUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC));
    public static final SpeedCoreItem SPEED_CORE = new SpeedCoreItem(new Item.Settings().rarity(Rarity.RARE));
    public static final SpeedUpgradeItem SPEED_UPGRADE = new SpeedUpgradeItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC));

    public static final List<Item> HammerCores = new ArrayList<>(List.of(SIZE_CORE, DURABILITY_CORE, SPEED_CORE));

    public static void registration() {
        // Register Stone Hammer
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "stone_hammer"), STONE_HAMMER);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(STONE_HAMMER);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(STONE_HAMMER);
        });

        // Register Iron Hammer
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "iron_hammer"), IRON_HAMMER);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(IRON_HAMMER);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(IRON_HAMMER);
        });

        // Register Gold Hammer
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "gold_hammer"), GOLD_HAMMER);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(GOLD_HAMMER);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(GOLD_HAMMER);
        });

        // Register Diamond Hammer
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "diamond_hammer"), DIAMOND_HAMMER);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(DIAMOND_HAMMER);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(DIAMOND_HAMMER);
        });

        // Register Netherite Hammer
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "netherite_hammer"), NETHERITE_HAMMER);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(NETHERITE_HAMMER);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(NETHERITE_HAMMER);
        });

        // Register Size Core
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "size_core"), SIZE_CORE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(SIZE_CORE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(SIZE_CORE);
        });

        // Register Size Upgrade
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "size_upgrade"), SIZE_UPGRADE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(SIZE_UPGRADE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(SIZE_UPGRADE);
        });

        // Register Durability Core
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "durability_core"), DURABILITY_CORE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(DURABILITY_CORE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(DURABILITY_CORE);
        });

        // Register Durability Upgrade
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "durability_upgrade"), DURABILITY_UPGRADE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(DURABILITY_UPGRADE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(DURABILITY_UPGRADE);
        });

        // Register Speed Core
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "speed_core"), SPEED_CORE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(SPEED_CORE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(SPEED_CORE);
        });

        // Register Speed Upgrade
        Registry.register(Registries.ITEM, new Identifier(Hammers.MOD_ID, "speed_upgrade"), SPEED_UPGRADE);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(SPEED_UPGRADE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
            content.add(SPEED_UPGRADE);
        });

    }
}
