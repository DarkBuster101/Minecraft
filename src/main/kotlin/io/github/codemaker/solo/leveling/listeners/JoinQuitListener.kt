package io.github.codemaker.solo.leveling.listeners

import io.github.codemaker.solo.leveling.Main
import io.github.codemaker.solo.leveling.managers.ProfileManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class JoinQuitListener(private val main: Main) : Listener {
    private val profileManager: ProfileManager? = main.profileManager

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        var profile = profileManager!!.getProfile(player.uniqueId)
        if (profile == null) {
            profile = profileManager.createProfile(player)
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
    }
}
