package de.mrcreativ3001.simplegravestones
import de.mrcreativ3001.simplegravestones.command.BackCommand
import de.mrcreativ3001.simplegravestones.config.ModConfig
import de.mrcreativ3001.simplegravestones.util.dropXp
import de.mrcreativ3001.simplegravestones.util.insertItem
import de.mrcreativ3001.simplegravestones.util.vanishCursedItems
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.block.Blocks
import net.minecraft.block.SignBlock
import net.minecraft.block.entity.BarrelBlockEntity
import net.minecraft.block.entity.SignBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.GameRules
import net.minecraft.world.World
import java.lang.RuntimeException

@Suppress("UNUSED")
object SimpleGravestones: ModInitializer {
    const val MOD_ID = "simplegravestones"

    // TODO:
    // Add back command
    // Add config option for back command
    // Add block checking for the grave!

    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            BackCommand.register(dispatcher)
        }
        
    }

    /**
     * Tries to spawn a grave for the player.
     * @return
     * Returns if the grave was spawned.
     */
    fun spawnGrave(player: PlayerEntity): Boolean {
        player.dropXp()

        // If keep inventory is enabled we don't need to place a grave
        if (player.world.gameRules.getBoolean(GameRules.KEEP_INVENTORY))
            return false

        player.vanishCursedItems()

        if (player.inventory.isEmpty)
            return true

        var barrel = placeBarrel(player.world, player.blockPos)
        var placedSecondBarrel = false
        for (slot in 0..player.inventory.size()) {
            val item = player.inventory.getStack(slot)
            if (!barrel.insertItem(item).isEmpty) {
                if (!placedSecondBarrel) {
                    placedSecondBarrel = true
                    barrel = placeBarrel(player.world, player.blockPos.up())
                    barrel.insertItem(item)
                } else {
                    throw RuntimeException("The player inventory contained more items than possible!")
                }
            }
        }

        val signPos = if (placedSecondBarrel) player.blockPos.up(2) else player.blockPos.up()
        placeSignWithText(player.world, signPos, player.horizontalFacing, player.displayName)

        return true
    }
    private fun placeBarrel(world: World, pos: BlockPos): BarrelBlockEntity {
        world.setBlockState(pos, Blocks.BARREL.defaultState)
        return world.getBlockEntity(pos) as BarrelBlockEntity
    }
    private fun placeSignWithText(world: World, pos: BlockPos, direction: Direction, text: Text) {
        val signRotation = when(direction) {
            Direction.SOUTH -> 0
            Direction.WEST ->  4
            Direction.NORTH -> 8
            Direction.EAST -> 12
            else -> 0
        }
        val signState = Blocks.SPRUCE_SIGN.defaultState.with(SignBlock.ROTATION, signRotation)
        world.setBlockState(pos, signState)

        val sign = world.getBlockEntity(pos) as SignBlockEntity
        sign.setTextOnRow(1, text)
    }
}