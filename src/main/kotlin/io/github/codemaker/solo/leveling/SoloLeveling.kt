package io.github.codemaker.solo.leveling

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

class SoloLeveling : JavaPlugin() {

    private lateinit var configFile: File
    private lateinit var config: FileConfiguration


    override fun onEnable() {
        configFile = File(dataFolder, "config.yml")
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            saveResource("config.yml", false)
        }
        config = YamlConfiguration.loadConfiguration(configFile)

        saveMapToConfig(Hunters.Hunter.combinationMap)
        reloadConfig()
    }

    override fun onDisable() {
        saveConfig()
    }

    private fun saveMapToConfig(map: Map<String, Any>) {
        for ((key, value) in map) {
            config.set(key, value)
        }
        try {
            config.save(configFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}