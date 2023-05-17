package io.github.codemaker.solo.leveling.commands

import io.github.codemaker.solo.leveling.Main
import io.github.codemaker.solo.leveling.Utils
import io.github.codemaker.solo.leveling.framework.Class
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ClassCmd(main: Main) : Command(main, "class") {
    override fun execute(player: Player, args: Array<String>?) {
        val inv = Bukkit.createInventory(null, 27, Utils.color("&eChoose your class"))
        var slot = 10
        for (clazz in Class.values()) {
            inv.setItem(slot, clazz.icon)
            slot++
        }
        player.openInventory(inv)
    }
}
