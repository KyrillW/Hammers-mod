package ciedorp.hammers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ciedorp.hammers.items.HammerItem;

public class Hammers implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("hammers");

	public static HammerItem DIAMOND_HAMMER = new HammerItem(0, 0, ToolMaterials.DIAMOND, new Item.Settings());


	@Override
	public void onInitialize() {
		Registry.register(Registries.ITEM, new Identifier("hammers", "diamond_hammer"), DIAMOND_HAMMER);

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
			content.add(DIAMOND_HAMMER);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SEARCH).register(content -> {
			content.add(DIAMOND_HAMMER);
		});

		// UseItemCallback.EVENT.register((world, player, pos, state, entity) -> {
        //     HammerItem.test();
        //     return false;
        // });

		LOGGER.info("Hello Fabric world!");
	}
}