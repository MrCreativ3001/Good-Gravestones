package de.mrcreativ3001.goodgravestones.compat

import de.mrcreativ3001.goodgravestones.ItemCollector
import dev.emi.trinkets.api.TrinketEnums
import dev.emi.trinkets.api.TrinketsApi
import dev.emi.trinkets.api.event.TrinketDropCallback
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.GameRules

class TrinketItemCollector: ItemCollector{
    override fun collectItems(player: PlayerEntity): Iterable<ItemStack> {
        val trinketComponent = TrinketsApi.getTrinketComponent(player)
        if (trinketComponent.isEmpty)
            return emptyList()

        val items = mutableListOf<ItemStack>()

        val keepInv = player.world.gameRules.getBoolean(GameRules.KEEP_INVENTORY)

        trinketComponent.get().allEquipped.forEach {
            val slot = it.left
            val stack = it.right

            var dropRule = TrinketsApi.getTrinket(stack.item).getDropRule(stack, slot, player)

            dropRule = TrinketDropCallback.EVENT.invoker().drop(dropRule, stack, slot, player)

            if (dropRule == TrinketEnums.DropRule.DEFAULT) {
                dropRule = slot.inventory.slotType.dropRule
            }

            if (dropRule == TrinketEnums.DropRule.DEFAULT) {
                dropRule = if (keepInv && player.type == EntityType.PLAYER) {
                    TrinketEnums.DropRule.KEEP
                } else {
                    if (EnchantmentHelper.hasVanishingCurse(stack)) {
                        TrinketEnums.DropRule.DESTROY
                    } else {
                        TrinketEnums.DropRule.DROP
                    }
                }
            }

            if (dropRule == TrinketEnums.DropRule.DROP) {
                items.add(stack.copy())
                slot.inventory.setStack(slot.index, ItemStack.EMPTY)
            } else if (dropRule == TrinketEnums.DropRule.DESTROY) {
                slot.inventory.setStack(slot.index, ItemStack.EMPTY)
            }
            // KEEP will do nothing
        }

        return items
    }
}