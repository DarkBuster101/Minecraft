package io.github.codemaker.solo.leveling.commands

import io.github.codemaker.solo.leveling.Main
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class Command(protected var main: Main, name: String?) : CommandExecutor {
    init {
        val pluginCommand = main.getCommand(name!!)
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
