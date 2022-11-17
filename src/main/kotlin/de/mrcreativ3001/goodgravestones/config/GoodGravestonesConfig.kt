package de.mrcreativ3001.goodgravestones.config

import eu.midnightdust.lib.config.MidnightConfig

@Suppress("UNUSED")
class GoodGravestonesConfig : MidnightConfig() {
    companion object {
        @JvmField
        @Entry var enableBackCommand = true
    }
}