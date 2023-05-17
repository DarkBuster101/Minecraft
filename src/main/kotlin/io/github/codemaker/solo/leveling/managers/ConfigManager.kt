package io.github.codemaker.solo.leveling.managers

import io.github.codemaker.solo.leveling.Main
import io.github.codemaker.solo.leveling.configs.Config
import io.github.codemaker.solo.leveling.configs.PlayerConfig

class ConfigManager(private val main: Main) {
    private val configs: MutableList<Config> = ArrayList()
    var playerConfig: PlayerConfig

    init {
        playerConfig = PlayerConfig(main)
        configs.add(playerConfig)
        configs.add(PlayerConfig(main).also { playerConfig = it })
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
