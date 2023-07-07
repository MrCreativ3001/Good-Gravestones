package de.mrcreativ3001.goodgravestones

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

interface ItemCollector {
    fun collectItems(player: PlayerEntity): Iterable<ItemStack>
}

// The vanilla item collector will also collect items from backSlot because it injects into the player's inventory
class VanillaItemCollector: ItemCollector {
    override fun collectItems(player: PlayerEntity): Iterable<ItemStack> {
        val items = mutableListOf<ItemStack>()

        for (i in 0..player.inventory.size()) {
            items.add(player.inventory.getStack(i).copy())
            player.inventory.removeStack(i)
        }

        return items
    }
}