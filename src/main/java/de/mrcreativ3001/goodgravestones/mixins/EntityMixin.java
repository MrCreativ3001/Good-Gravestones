package de.mrcreativ3001.goodgravestones.mixins;

import de.mrcreativ3001.goodgravestones.PlayerItemDropDisable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements PlayerItemDropDisable {
    private boolean allowItemDrops = true;

    @Inject(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
    public void dropStack(ItemStack stack, float yOffset, CallbackInfoReturnable<ItemEntity> info) {
        // This will prevent the entity from dropping items on death. Look at other mixins for more info.
        if (!this.areItemDropsAllowed()) {
            info.setReturnValue(null);
        }
    }

    @Override
    public boolean areItemDropsAllowed() {
        return allowItemDrops;
    }

    @Override
    public void setItemDropsAllowed(boolean allowed) {
        allowItemDrops = allowed;
    }
}
