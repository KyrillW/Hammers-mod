package ciedorp.hammers.mixin;

import ciedorp.hammers.interfaces.HammerStack;
import ciedorp.hammers.items.HammerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    @Shadow @Final private Property levelCost;
    private AnvilScreenHandler self = (AnvilScreenHandler) (Object) this;

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    public void updateResult(CallbackInfo ci){
        ItemStack input_1 = self.getSlot(0).getStack();
        ItemStack input_2 = self.getSlot(1).getStack();
        Slot output = self.getSlot(2);
        if (input_1.getItem() instanceof HammerItem && input_2.getItem() == Items.OBSIDIAN) {
            ItemStack outputStack = input_1.copy();
            HammerStack newHammer = (HammerStack) (Object) outputStack;
            if (newHammer.upgradeSize()){
                this.levelCost.set(newHammer.getSize());
                output.setStack(outputStack);
                ci.cancel();
            }
        }
    }



}