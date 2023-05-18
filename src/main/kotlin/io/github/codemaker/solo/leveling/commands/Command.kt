package io.github.codemaker.solo.leveling.commands

import io.github.codemaker.solo.leveling.SoloLeveling
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class Command(protected var soloLeveling: SoloLeveling, name: String?) : CommandExecutor {
    init {
        val pluginCommand = soloLeveling.getCommand(name!!)
        pluginCommand!!.setExecutor(this)
    }

    abstract fun execute(player: Player, args: Array<String>?)
    override fun onCommand(sender: CommandSender, cmd: Command, alias: String, args: Array<String>): Boolean {
        if (sender is Player) {
            execute(sender, args)
        }
        return true
    }
}
