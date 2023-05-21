package io.github.codemaker.solo.leveling

import io.github.codemaker.solo.leveling.clazz.Berserker
import io.github.codemaker.solo.leveling.commands.ClassCmd
import io.github.codemaker.solo.leveling.commands.LevelCmd
import io.github.codemaker.solo.leveling.listeners.*
import io.github.codemaker.solo.leveling.managers.ConfigManager
import io.github.codemaker.solo.leveling.managers.ProfileManager
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class SoloLeveling : JavaPlugin() {
    var configManager: ConfigManager? = null
        private set
    var profileManager: ProfileManager? = null
        private set

    override fun onEnable() {
        pluginLogger = logger
        configManager = ConfigManager(this)
        profileManager = ProfileManager(this)
        configManager!!.loadConfig()
        profileManager!!.loadProfiles()
        ClassCmd(this)
        LevelCmd(this)
        val pluginManager = server.pluginManager
        pluginManager.registerEvents(JoinQuitListener(this), this)
        pluginManager.registerEvents(InventoryListener(this), this)
        pluginManager.registerEvents(ChatListener(this), this)
        pluginManager.registerEvents(ClickListener(this), this)
        pluginManager.registerEvents(EventListeners(this), this)
        pluginManager.registerEvents(Berserker(this), this)
        Utils.log("Plugin fully enabled!")
    }

    override fun onDisable() {
        profileManager!!.saveProfiles()
        configManager!!.saveConfig()
        Utils.log("Plugin fully disabled!")
    }

    companion object {
        var pluginLogger: Logger? = null
            private set
    }
}

//https://github.com/DarkBuster101/Minecraft.git
