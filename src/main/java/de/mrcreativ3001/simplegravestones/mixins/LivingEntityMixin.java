package de.mrcreativ3001.simplegravestones.mixins;

import de.mrcreativ3001.simplegravestones.SimpleGravestones;
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

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    public void drop(DamageSource source, CallbackInfo info) {
        // This will be transformed into the instance of the LivingEntity
        Object thisObj = this;
        if (!(thisObj instanceof PlayerEntity player))
            return;

        if (SimpleGravestones.spawnGrave(player))
            info.cancel();
    }
}
