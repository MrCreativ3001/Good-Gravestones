package de.mrcreativ3001.goodgravestones.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin extends Entity{
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    public void drop(DamageSource source, CallbackInfo info) {
        // This method normally will drop all items of the entity. We don't want that, so we cancel it.

        Object thisObj = this;
        if (!(thisObj instanceof PlayerEntity player)) {
            return;
        }

        info.cancel(); // This will cancel dropInventory which will indirectly cancel Trinkets mixin for dropping items
    }

}
