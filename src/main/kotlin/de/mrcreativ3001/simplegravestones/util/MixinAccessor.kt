package de.mrcreativ3001.simplegravestones.util

import de.mrcreativ3001.simplegravestones.mixins.LivingEntityAccessor
import de.mrcreativ3001.simplegravestones.mixins.PlayerEntityAccessor
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity

fun LivingEntity.dropXp() {
    (this as LivingEntityAccessor).invokeDropXp()
}

fun PlayerEntity.vanishCursedItems() {
    (this as PlayerEntityAccessor).invokeVanishCursedItems()
}