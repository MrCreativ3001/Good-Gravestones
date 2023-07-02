package de.mrcreativ3001.goodgravestones

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

interface ItemCollector {
    fun collectItems(player: PlayerEntity): Iterable<ItemStack>
}

class VanillaItemCollector: ItemCollector {
    override fun collectItems(player: PlayerEntity): Iterable<ItemStack> {
        val items = mutableListOf<ItemStack>()

        for (i in 0..player.inventory.size()) {
            val stack = player.inventory.getStack(i)
            if (stack.isEmpty)
                continue

            items.add(stack)
            player.inventory.removeStack(i)
        }

        return items
    }
}