package io.github.codemaker.solo.leveling.commands

import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.Utils
import io.github.codemaker.solo.leveling.framework.Class
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType

class ClassCmd(soloLeveling: SoloLeveling) : Command(soloLeveling, "class") {
    override fun execute(player: Player, args: Array<String>?) {
        val inv = Bukkit.createInventory(null, InventoryType.CHEST, Component.text(Utils.color("&eChoose your class")))
        var slot = 10
        for (clazz in Class.values()) {
            inv.setItem(slot, clazz.icon)
            slot++
        }
        player.openInventory(inv)
    }
}
