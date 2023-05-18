package io.github.codemaker.solo.leveling.managers

import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.configs.Config
import io.github.codemaker.solo.leveling.configs.PlayerConfig

class ConfigManager(private val soloLeveling: SoloLeveling) {
    private val configs: MutableList<Config> = ArrayList()
    var playerConfig: PlayerConfig

    init {
        playerConfig = PlayerConfig(soloLeveling)
        configs.add(playerConfig)
        configs.add(PlayerConfig(soloLeveling).also { playerConfig = it })
    }

    fun loadConfig() {
        for (config in configs) {
            config.loadConfig()
        }
    }

    fun saveConfig() {
        for (config in configs) {
            config.saveConfig()
        }
    }
}
