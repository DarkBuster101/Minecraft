package io.github.codemaker.solo.leveling.configs

import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.Utils
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

abstract class Config(protected var soloLeveling: SoloLeveling, private var name: String) : YamlConfiguration() {
    private var file: File = File(soloLeveling.dataFolder, name)

    fun getSection(path: String?): Set<String> {
        val section = getConfigurationSection(path!!)
        return section?.getKeys(false) ?: HashSet()
    }

    private fun checkFile() {
        if (!file.exists()) {
            file.parentFile.mkdirs()
            soloLeveling.saveResource(name, false)
        }
    }

    fun loadConfig() {
        checkFile()
        try {
            load(file)
            Utils.log("Loaded data from $name!")
        } catch (exception: InvalidConfigurationException) {
            exception.printStackTrace()
            Utils.log("Error Loading data from $name!")
        } catch (exception: IOException) {
            exception.printStackTrace()
            Utils.log("Error Loading data from $name!")
        }
    }

    fun saveConfig() {
        checkFile()
        try {
            save(file)
            Utils.log("Saved data from $name!")
        } catch (exception: IOException) {
            exception.printStackTrace()
            Utils.log("Error Saving data from $name!")
        }
    }
}
