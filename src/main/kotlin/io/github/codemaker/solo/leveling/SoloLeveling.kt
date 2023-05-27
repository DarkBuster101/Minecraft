package io.github.codemaker.solo.leveling

import io.github.codemaker.solo.leveling.clazz.Archer
import io.github.codemaker.solo.leveling.clazz.Berserker
import io.github.codemaker.solo.leveling.clazz.Mage
import io.github.codemaker.solo.leveling.commands.ClassCmd
import io.github.codemaker.solo.leveling.commands.LevelCmd
import io.github.codemaker.solo.leveling.listeners.*
import io.github.codemaker.solo.leveling.managers.ConfigManager
import io.github.codemaker.solo.leveling.managers.ProfileManager
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.logging.Logger

class SoloLeveling : JavaPlugin() {
    var configManager: ConfigManager? = null
        private set
    var profileManager: ProfileManager? = null
        private set
    private var connection: Connection? = null
    private lateinit var config: FileConfiguration


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
        pluginManager.registerEvents(Mage(this), this)
        pluginManager.registerEvents(Archer(this), this)

        Utils.log("Plugin fully enabled!")
    }

    override fun onDisable() {
        profileManager!!.saveProfiles()
        configManager!!.saveConfig()
        disconnectFromDatabase()
        Utils.log("Plugin fully disabled!")
    }

    companion object {
        var pluginLogger: Logger? = null
            private set
    }

    private fun conncetToDataBase() {
        try {
            Class.forName("org.sqlite.JDBC")

            val connection = DriverManager.getConnection("jdbc:sqlite:${dataFolder}/database.db")

        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun disconnectFromDatabase() {
        try {
            connection?.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun saveConfigDataToDatabase() {
        try {
            // Create a table if it doesn't exist
            val statement = connection?.createStatement()
            statement?.execute("CREATE TABLE IF NOT EXISTS config_data (key TEXT, value TEXT)")

            // Clear the existing data in the table
            statement?.execute("DELETE FROM config_data")

            // Save data from config.yml to the database
            for (key in config.getKeys(false)) {
                val value = config.getString(key)
                val preparedStatement = connection?.prepareStatement(
                    "INSERT INTO config_data (key, value) VALUES (?, ?)"
                )
                preparedStatement?.setString(1, key)
                preparedStatement?.setString(2, value)
                preparedStatement?.executeUpdate()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun getDataFromDatabase() {
        try {
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM config_data")

            while (resultSet?.next() == true) {
                val key = resultSet.getString("key")
                val value = resultSet.getString("value")
                logger.info("Key: $key, Value: $value")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}

//https://github.com/DarkBuster101/Minecraft.git
