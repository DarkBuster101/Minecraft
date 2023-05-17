package io.github.codemaker.solo.leveling

import io.github.codemaker.solo.leveling.commands.ClassCmd
import io.github.codemaker.solo.leveling.commands.LevelCmd
import io.github.codemaker.solo.leveling.listeners.ChatListener
import io.github.codemaker.solo.leveling.listeners.ClickListener
import io.github.codemaker.solo.leveling.listeners.InventoryListener
import io.github.codemaker.solo.leveling.listeners.JoinQuitListener
import io.github.codemaker.solo.leveling.managers.ConfigManager
import io.github.codemaker.solo.leveling.managers.ProfileManager
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class Main : JavaPlugin() {
    var configManager: ConfigManager? = null
        private set
    var profileManager: ProfileManager? = null
        private set

    override fun onEnable() {
        pluginLogger = getLogger()
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
