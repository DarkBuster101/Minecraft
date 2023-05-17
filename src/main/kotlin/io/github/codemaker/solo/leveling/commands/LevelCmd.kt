package io.github.codemaker.solo.leveling.commands

import io.github.codemaker.solo.leveling.Main
import io.github.codemaker.solo.leveling.Utils
import io.github.codemaker.solo.leveling.framework.Level
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class LevelCmd(main: Main) : Command(main, "SoloLevelingLevel") {
    override fun execute(player: Player, args: Array<String>?) {
        val inv = Bukkit.createInventory(null, 27, Utils.color("&eChoose your level"))
        var slot = 10
        for (level in Level.values()) {
            inv.setItem(slot, level.icon)
            slot++
        }
        player.openInventory(inv)
    }
}
