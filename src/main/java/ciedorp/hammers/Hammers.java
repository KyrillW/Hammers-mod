package ciedorp.hammers;

import ciedorp.hammers.items.ItemInit;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hammers implements ModInitializer {
	public static final String MOD_ID = "hammers";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

//	public static HammerItem DIAMOND_HAMMER = new HammerItem(0, 0, ToolMaterials.DIAMOND, new Item.Settings());

	@Override
	public void onInitialize() {

		ItemInit.registration();

//		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("toggleCreeperGriefing").requires(source -> source.hasPermissionLevel(4)).executes(context -> {
//			final ServerCommandSource source = context.getSource();
//			PlayerEntity player = source.getPlayer();
//			ItemStack itemStack = player.getMainHandStack();
//			if (player.getMainHandStack().getItem() instanceof HammerItem) {
//				itemStack.setDamage(200);
//				HammerStackInterface hammerStack = (HammerStackInterface) (Object) itemStack;
//				hammerStack.setSize(69);
//				player.getInventory().setStack(0, (ItemStack) (Object) hammerStack);
//				source.sendFeedback(() -> Text.literal("WOOOW het werkt"), false);
//			}
//			return 1;
//		})));

		LOGGER.info("Hello Fabric world!");
	}
}