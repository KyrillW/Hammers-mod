package ciedorp.hammers.mixin;

import ciedorp.hammers.interfaces.HammerStack;
import ciedorp.hammers.items.HammerItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin implements HammerStack {
    ItemStack self = (ItemStack) (Object) this;
    private static final String TAG_SIZE = "Ciedorp_HammerSize";
    private static final String TAG_DURABILITY = "Ciedorp_HammerDurability";
    private static final String TAG_SPEED = "Ciedorp_HammerSpeed";

    @Shadow
    private NbtCompound nbt;

    @Override
    public int getSize() {
        return nbt.getInt(TAG_SIZE);
    }

    @Override
    public void setSize(int size) {
        nbt.putInt(TAG_SIZE, size);
    }

    @Override
    public boolean upgradeSize() {
        if (getSize() <= 4){
            setSize(getSize() + 1);
            return true;
        }
        return false;
    }

    @Override
    public int getHammerDurability() {
        return nbt.getInt(TAG_DURABILITY);
    }

    @Override
    public void setHammerDurability(int durability) {
        nbt.putInt(TAG_DURABILITY, durability);
    }

    @Override
    public boolean upgradeHammerDurability() {
        if (getHammerDurability() <= 9){
            setHammerDurability(getHammerDurability() + 1);
            return true;
        }
        return false;
    }

    @Override
    public int getSpeed() {
        return nbt.getInt(TAG_SPEED);
    }

    @Override
    public void setSpeed(int speed) {
        nbt.putInt(TAG_SPEED, speed);
    }

    @Override
    public boolean upgradeSpeed() {
        if (getSpeed() <= 9){
            setSpeed(getSpeed() + 1);
            return true;
        }
        return false;
    }

    @Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;)V", at = @At("TAIL"))
    private void onInit(ItemConvertible item, CallbackInfo ci) {
        if (self.getItem() instanceof HammerItem) {
            setSize(1);
            setHammerDurability(1);
            setSpeed(1);
        }
    }

    @Inject(method = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;I)V", at = @At("TAIL"))
    private void onInit(ItemConvertible item, int count, CallbackInfo ci) {
        if (self.getItem() instanceof HammerItem) {
            setSize(1);
            setHammerDurability(1);
            setSpeed(1);
        }
    }

//    @Inject(method = "<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
//    private void onInit(NbtCompound nbt, CallbackInfo ci) {
//        if (self.getItem() instanceof HammerItem) {
//            setSize(1);
//        }
//    }

    @Inject(method = "Lnet/minecraft/item/ItemStack;getMaxDamage()I", at = @At("RETURN"), cancellable = true)
    private void getMaxDamage(CallbackInfoReturnable<Integer> cir) {
        if (self.getItem() instanceof HammerItem hammer) {
            HammerStack hammerStack = (HammerStack) (Object) self;
            cir.setReturnValue(hammer.getMaxDamage() * hammerStack.getHammerDurability());
        }
    }
}
