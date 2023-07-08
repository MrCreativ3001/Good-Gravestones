package de.mrcreativ3001.goodgravestones
import de.mrcreativ3001.goodgravestones.command.BackCommand
import de.mrcreativ3001.goodgravestones.compat.TrinketItemCollector
import de.mrcreativ3001.goodgravestones.config.GoodGravestonesConfig
import de.mrcreativ3001.goodgravestones.util.dropXp
import de.mrcreativ3001.goodgravestones.util.insertItem
import de.mrcreativ3001.goodgravestones.util.vanishCursedItems
import eu.midnightdust.lib.config.MidnightConfig
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader
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
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Suppress("UNUSED")
object GoodGravestones: ModInitializer {
    const val MOD_ID = "goodgravestones"
    val logger: Logger = LogManager.getLogger(MOD_ID)

    private val itemCollectors = mutableListOf<ItemCollector>()

    override fun onInitialize() {
        MidnightConfig.init(MOD_ID, GoodGravestonesConfig::class.java)
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            BackCommand.register(dispatcher)
        }

        // Register item collectors
        itemCollectors.clear()
        itemCollectors.add(VanillaItemCollector())
        if (FabricLoader.getInstance().isModLoaded("trinkets")) {
            itemCollectors.add(TrinketItemCollector())
        }
    }

    /**
     * Tries to spawn a grave for the player.
     * @return
     * Returns if the grave was spawned.
     */
    @JvmStatic
    fun spawnGrave(player: PlayerEntity): Boolean {
        player.dropXp()

        // If keep inventory is enabled we don't need to place a grave
        if (player.world.gameRules.getBoolean(GameRules.KEEP_INVENTORY))
            return false

        player.vanishCursedItems()

        val collectedItems = itemCollectors.flatMap { it.collectItems(player) }.filter { !it.isEmpty }.toCollection(mutableListOf())
        if (collectedItems.isEmpty())
            return true

        // Place barrel
        var currentPos = player.blockPos
        var barrel = placeBarrel(player.world, currentPos)
        while (collectedItems.isNotEmpty()) {
            val item = collectedItems.removeFirst()
            if (item.isEmpty)
                continue
            val rest = barrel.insertItem(item)
            if (!rest.isEmpty) {
                currentPos = currentPos.up()
                barrel = placeBarrel(player.world, currentPos)

                val restRest = barrel.insertItem(rest)
                if(!restRest.isEmpty) {
                    logger.warn("Could not place all items in grave of player ${player.name}. Item lost is $restRest.")
                }
            }
        }

        // Place sign
        val signPos = currentPos.up()
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
