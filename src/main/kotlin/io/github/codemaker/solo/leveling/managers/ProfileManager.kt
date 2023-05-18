package io.github.codemaker.solo.leveling.managers

import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.configs.PlayerConfig
import io.github.codemaker.solo.leveling.framework.*
import org.bukkit.entity.Player
import java.util.*
import kotlin.random.Random

class ProfileManager(private val soloLeveling: SoloLeveling) {
    private val playerConfig: PlayerConfig? = soloLeveling.configManager?.playerConfig
    private val profiles: MutableMap<UUID, Profile> = HashMap()


    fun loadProfiles() {
        for (key in playerConfig!!.getSection("")) {
            val id = UUID.fromString(key)
            val clazz = playerConfig.getClass(id)
            val level = playerConfig.getLevel(id)
            val profile = Profile(clazz, level)
            profiles[id] = profile
        }
    }

    fun saveProfiles() {
        for (uuid in profiles.keys) {
            val profile = profiles[uuid]
            playerConfig!!.setClass(uuid, profile?.clazz)
            playerConfig.setLevel(uuid, profile?.level)
        }
    }

    fun createProfile(player: Player): Profile {
        val profile = Profile(randomClass(), randomLevel())
        profiles[player.uniqueId] = profile
        return profile
    }

    fun createSpeicalProfile(player: Player): Profile {
        val profile = Profile(Class.Necromacer, Level.Upgrader)
        profiles[player.uniqueId] = profile
        return profile
    }

    fun getProfile(uuid: UUID): Profile? {
        return profiles[uuid]
    }

    private fun randomClass(): Class {
        val options = listOf(Class.MAGE, Class.ARCHER, Class.BERSKER)
        val randomIndex = Random.nextInt(options.size)
        return options[randomIndex]
    }

    private fun randomLevel(): Level {
        val options = listOf(Level.S_LEVEL, Level.A_LEVEL, Level.B_LEVEL, Level.C_LEVEL, Level.D_LEVEL, Level.E_LEVEL)
        val randomIndex = Random.nextInt(options.size)
        return options[randomIndex]
    }
}
