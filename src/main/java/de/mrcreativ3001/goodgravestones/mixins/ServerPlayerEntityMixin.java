package de.mrcreativ3001.goodgravestones.mixins;

import de.mrcreativ3001.goodgravestones.GoodGravestones;
import de.mrcreativ3001.goodgravestones.PlayerItemDropDisable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayerEntity.class, priority = 500) // Low priority to make sure that this mixin is applied before the other mixins
public class ServerPlayerEntityMixin {

    @SuppressWarnings("all")
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeathHead(DamageSource source, CallbackInfo info) {
        // This method is called on death of the entity

        // Spawn the grave and disable item drops

        Object thisObj = this;
        if (!(thisObj instanceof PlayerEntity player)) {
            return;
        }

        ((PlayerItemDropDisable) this).setItemDropsAllowed(false);

        GoodGravestones.spawnGrave(player);
    }

    @SuppressWarnings("all")
    @Inject(method = "onDeath", at = @At("RETURN"))
    public void onDeathReturn(DamageSource source, CallbackInfo info) {
        // This method is called on death of the entity
        // Enable item drops again

        Object thisObj = this;
        if (!(thisObj instanceof PlayerEntity)) {
            return;
        }

        ((PlayerItemDropDisable) this).setItemDropsAllowed(true);
    }
}
