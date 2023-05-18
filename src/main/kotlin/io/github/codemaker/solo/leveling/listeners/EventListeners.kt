package io.github.codemaker.solo.leveling.listeners

import com.destroystokyo.paper.event.server.PaperServerListPingEvent
import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.framework.SoloLevelingConfigs
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryPickupItemEvent
import org.bukkit.event.player.PlayerItemConsumeEvent

class EventListeners(private val soloLeveling: SoloLeveling): Listener {
    @EventHandler
    fun onServerListPing(event: PaperServerListPingEvent) {
        event.setHidePlayers(true)
        event.maxPlayers = 0
        event.numPlayers = 0
        event.motd(
            Component.text().color(TextColor.color(0x000049)).content("S O L O L E V E L I N G").decorate(TextDecoration.BOLD).build()
        )
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        event.deathMessage(null)
    }

    @EventHandler
    fun onPlayerPickup(event: InventoryPickupItemEvent) {
        if (event.item.itemStack.type in SoloLevelingConfigs.forbiddenItems) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerCraft(event: CraftItemEvent) {
        val item = event.currentItem ?: return
        val type = item.type

        if (type in SoloLevelingConfigs.forbiddenItems) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onFoodLevelChange(event: FoodLevelChangeEvent) {
        if (event.entityType != EntityType.PLAYER) return
        event.foodLevel = 20
    }

    @EventHandler
    fun onRegainHealth(event: EntityRegainHealthEvent) {
        if (event.regainReason == EntityRegainHealthEvent.RegainReason.SATIATED || event.regainReason == EntityRegainHealthEvent.RegainReason.REGEN) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerItemConsume(event: PlayerItemConsumeEvent) {
        if (event.item.type == Material.MILK_BUCKET) {
            event.isCancelled = true
        }
    }
}