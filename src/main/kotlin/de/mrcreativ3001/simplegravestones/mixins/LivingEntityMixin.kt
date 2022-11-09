package de.mrcreativ3001.simplegravestones.mixins

import de.mrcreativ3001.simplegravestones.SimpleGravestones
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(LivingEntity::class)
abstract class LivingEntityMixin {

    @Inject(method = ["drop"], at = [At("HEAD")], cancellable = true)
    fun drop(source: DamageSource, info: CallbackInfo) {
        // This will be transformed into the instance of the LivingEntity
        if (this !is PlayerEntity)
            return

        if (SimpleGravestones.spawnGrave(this))
            info.cancel()
    }

}