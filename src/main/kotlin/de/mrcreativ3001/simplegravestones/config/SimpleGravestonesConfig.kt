package de.mrcreativ3001.simplegravestones.config

import eu.midnightdust.lib.config.MidnightConfig

@Suppress("UNUSED")
class SimpleGravestonesConfig : MidnightConfig() {
    companion object {
        @JvmField
        @Entry var enableBackCommand = true
    }
}