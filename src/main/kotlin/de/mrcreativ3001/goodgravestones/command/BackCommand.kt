package de.mrcreativ3001.goodgravestones.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import de.mrcreativ3001.goodgravestones.GoodGravestones
import de.mrcreativ3001.goodgravestones.config.GoodGravestonesConfig
import de.mrcreativ3001.goodgravestones.util.isSolid
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager.*
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.GlobalPos
import net.minecraft.world.World
import java.util.EnumSet

object BackCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(literal("back").executes(BackCommand::execute))
    }
    private fun execute(ctx: CommandContext<ServerCommandSource>): Int {
        val entity = ctx.source.entity!!
        if (entity !is PlayerEntity) {
            entity.sendMessage(Text.translatable("permissions.requires.player"))
            return 0
        }

        if(!GoodGravestonesConfig.enableBackCommand) {
            entity.sendMessage(Text.translatable("command.${GoodGravestones.MOD_ID}.back.disabled"))
            return 0
        }

        val deathPos = entity.lastDeathPos.map(GlobalPos::getPos).orElse(null)
        if (deathPos == null) {
            entity.sendMessage(Text.translatable("command.${GoodGravestones.MOD_ID}.back.notfound.deathpos"))
            return 0
        }

        val teleportPos = findTeleportSpot(entity.world, deathPos)

        if (teleportPos == null) {
            entity.sendMessage(Text.translatable("command.${GoodGravestones.MOD_ID}.back.obstructed", deathPos.x, deathPos.y, deathPos.z))
            return 0
        }

        entity.teleport(teleportPos.x.toDouble()+0.5, teleportPos.y.toDouble(), teleportPos.z.toDouble()+0.5)
        entity.sendMessage(Text.translatable("command.${GoodGravestones.MOD_ID}.back.success"))

        return 1
    }

    /**
     * Finds a block where the player can safely be teleported to.
     * @return
     * Returns null or a position which the player can safely be teleported to.
     */
    private fun findTeleportSpot(world: World, pos: BlockPos): BlockPos? {
        fun findY(world: World, pos: BlockPos): BlockPos? {
            // Check 5 blocks higher because of the grave that spawned and the player height
            val pos = pos.up(5)
            for (offset in 0 downTo -10) {
                val checkPos = pos.add(0, offset, 0)

                if (world.isSolid(checkPos)) {
                    return if (offset >= -2) null else checkPos.up()
                }
            }
            return null
        }

        var teleportPos = findY(world, pos)
        if (teleportPos != null) return teleportPos

        // Also search blocks next to the position for available spots
        for (direction in EnumSet.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST)) {
            teleportPos = findY(world, pos.offset(direction))
            if (teleportPos != null) return teleportPos
        }

        return null
    }
}
