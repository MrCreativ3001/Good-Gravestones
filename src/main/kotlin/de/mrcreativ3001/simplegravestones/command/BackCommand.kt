package de.mrcreativ3001.simplegravestones.command

import com.mojang.brigadier.CommandDispatcher
import de.mrcreativ3001.simplegravestones.util.isSolid
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.CommandManager.*
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object BackCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(literal("back").executes { ctx ->
            val entity = ctx.source.entity!!
            if (entity !is PlayerEntity) {
                entity.sendMessage(Text.literal("Only a player can go back to their death position"))
                return@executes 0
            }

            if (entity.lastDeathPos.isEmpty) {
                entity.sendMessage(Text.literal("No death position was found"))
                return@executes 0
            }

            // Set the position 3 blocks higher because of the grave that was spawned
            val lastDeathPos = entity.lastDeathPos.get().pos.up(3)
            val world = entity.world

            // check if position is obstructed
            if (world.isSolid(lastDeathPos) || world.isSolid(lastDeathPos)) {
                entity.sendMessage(Text.literal("Your last death position is obstructed"))
                return@executes 0
            }

            var teleportPos: BlockPos? = null
            var testTeleportPos = lastDeathPos
            for (i in 0..10) {
                if (world.isSolid(testTeleportPos)) {
                    teleportPos = testTeleportPos.up()
                    break
                }
                testTeleportPos = testTeleportPos.down()
            }

            if (teleportPos == null) {
                entity.sendMessage(Text.literal("Your last death position has no solid ground"))
                return@executes 0
            }

            entity.teleport(teleportPos.x.toDouble()+0.5, teleportPos.y.toDouble(), teleportPos.z.toDouble()+0.5)
            entity.sendMessage(Text.literal("You were teleported to your last death position"))

            return@executes 1
        })
    }
}
