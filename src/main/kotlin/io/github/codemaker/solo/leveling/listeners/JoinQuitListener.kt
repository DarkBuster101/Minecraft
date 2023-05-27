package io.github.codemaker.solo.leveling.listeners

import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.Utils
import io.github.codemaker.solo.leveling.framework.SoloLevelingConfigs
import io.github.codemaker.solo.leveling.managers.ProfileManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scoreboard.Team

class JoinQuitListener(private val soloLeveling: SoloLeveling) : Listener {
    private val profileManager: ProfileManager? = soloLeveling.profileManager

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        team()
        val player = event.player
        var profile = profileManager!!.getProfile(player.uniqueId)
        if (profile == null) {
            profile = if (player.name in SoloLevelingConfigs.defaultUpgrader) {
                profileManager.createSpeicalProfile(player)
            } else {
                profileManager.createProfile(player)
            }
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
    }

    private fun team() : Team {
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        return scoreboard.getTeam("marked") ?: scoreboard.registerNewTeam("marked").apply {
            displayName(Component.text("Marked"))
            color(NamedTextColor.RED)
            setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS)
        }
    }
}
