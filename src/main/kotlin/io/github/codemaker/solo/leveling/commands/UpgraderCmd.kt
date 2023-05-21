package io.github.codemaker.solo.leveling.commands

import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.Utils
import io.github.codemaker.solo.leveling.framework.Level
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType

class UpgraderCmd(soloLeveling: SoloLeveling) : Command(soloLeveling, "shop")  {
    override fun execute(player: Player, args: Array<String>?) {
        val inv = Bukkit.createInventory(null, InventoryType.CHEST, Component.text(Utils.color("&eShop for the 'player'")))
        var slot = 10
        for (level in Level.values()) {
            inv.setItem(slot, level.icon)
            slot++
        }
        player.openInventory(inv)
    }
}