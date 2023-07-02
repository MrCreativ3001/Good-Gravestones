package de.mrcreativ3001.goodgravestones.compat

import de.mrcreativ3001.goodgravestones.ItemCollector
import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

class TrinketItemCollector: ItemCollector {
    override fun collectItems(player: PlayerEntity): Iterable<ItemStack> {
        val trinketComponent = TrinketsApi.getTrinketComponent(player)
        if (trinketComponent.isEmpty)
            return emptyList()

        return trinketComponent.get().allEquipped.map { it.right }
    }
}