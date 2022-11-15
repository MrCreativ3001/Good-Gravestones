package de.mrcreativ3001.simplegravestones.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import de.mrcreativ3001.simplegravestones.SimpleGravestones
import eu.midnightdust.lib.config.MidnightConfig
import net.minecraft.client.gui.screen.Screen

@Suppress("UNUSED")
class SimpleGravestonesModMenuApi: ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*>? {
        return ConfigScreenFactory { parent: Screen? -> MidnightConfig.getScreen(parent, SimpleGravestones.MOD_ID) }
    }
}