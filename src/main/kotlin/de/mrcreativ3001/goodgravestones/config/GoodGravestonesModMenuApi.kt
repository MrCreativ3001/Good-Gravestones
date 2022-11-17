package de.mrcreativ3001.goodgravestones.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import de.mrcreativ3001.goodgravestones.GoodGravestones
import eu.midnightdust.lib.config.MidnightConfig
import net.minecraft.client.gui.screen.Screen

@Suppress("UNUSED")
class GoodGravestonesModMenuApi: ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*>? {
        return ConfigScreenFactory { parent: Screen? -> MidnightConfig.getScreen(parent, GoodGravestones.MOD_ID) }
    }
}