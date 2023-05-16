package io.github.codemaker.solo.leveling

import net.minecraft.world.scores.Team
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.Random

class EventListener : Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        if (player.name in SoloLevelingConfig.defaultUpgrader) {
            player.sendMessage("You have been chosen as an Upgrader")

        } else if (player.name in SoloLevelingConfig.defaultSLevel) {
            player.sendMessage("You have been chosen as a S level hunter")
        } else {
            randomLevels()
        }


        event.joinMessage(null)
    }

    private fun randomLevels(): String {
        val random = Random()
        val randomIndex = random.nextInt(SoloLevelingConfig.defaultLevels.size)

        return SoloLevelingConfig.defaultLevels[randomIndex]
    }
}