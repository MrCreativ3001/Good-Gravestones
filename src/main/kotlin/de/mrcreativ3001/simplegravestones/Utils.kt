package de.mrcreativ3001.simplegravestones.util

import de.mrcreativ3001.simplegravestones.mixins.LivingEntityAccessor
import de.mrcreativ3001.simplegravestones.mixins.PlayerEntityAccessor
import net.minecraft.block.entity.LockableContainerBlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Inserts the items into the chest.
 * @return
 * Returns the items that couldn't be inserted
 */
fun LockableContainerBlockEntity.insertItem(item: ItemStack): ItemStack {
    if (item.isEmpty)
        return ItemStack.EMPTY

    var item = item.copy()
    for (slot in 0 until this.size()) {
        item = insertItem(slot, item)
        if (item.isEmpty)
            return ItemStack.EMPTY
    }
    return item
}

/**
 * Inserts the items into the chest slot.
 * @return
 * Returns the items that couldn't be inserted
 */
fun LockableContainerBlockEntity.insertItem(slot: Int, item: ItemStack): ItemStack {
    val item = item.copy()
    if (item.isEmpty)
        return ItemStack.EMPTY

    val containerItem = this.getStack(slot).copy()

    if (containerItem.isEmpty) {
        this.setStack(slot, item)
        return ItemStack.EMPTY
    }

    if (!containerItem.isItemEqual(item))
        return item.copy()

    var newContainerItemCount = item.count + containerItem.count
    if (newContainerItemCount > item.maxCount) {
        val rest = newContainerItemCount - item.maxCount
        item.count = rest
        newContainerItemCount = item.maxCount
    } else {
        item.count = 0
    }
    containerItem.count = newContainerItemCount

    this.setStack(slot, containerItem)

    return item
}

fun LivingEntity.dropXp() {
    (this as LivingEntityAccessor).invokeDropXp()
}

fun PlayerEntity.vanishCursedItems() {
    (this as PlayerEntityAccessor).invokeVanishCursedItems()
}