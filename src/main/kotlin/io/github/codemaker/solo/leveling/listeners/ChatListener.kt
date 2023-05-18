package io.github.codemaker.solo.leveling.listeners

import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.Utils
import io.github.codemaker.solo.leveling.managers.ProfileManager
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatListener(soloLeveling: SoloLeveling) : Listener {
    private val profileManager: ProfileManager?

    init {
        profileManager = soloLeveling.profileManager
    }

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        val player = event.player
        val profile = profileManager!!.getProfile(player.uniqueId)
        val clazz = profile?.clazz
        val level = profile?.level
        event.isCancelled = true
        if (clazz == null && level == null) {
            for (each in Bukkit.getOnlinePlayers()) {
                Utils.msgPlayer(each, "&8[&fNon-Hunter&8] &f" + player.name + "&7: &f" + event.message())
            }
        } else {
            for (each in Bukkit.getOnlinePlayers()) {
                Utils.msgPlayer(each, "&8[&f" + "${level?.displayName}" + "${clazz?.displayName}" + "&8] &f" + player.name + "&7: &f" + event.message())
            }
        }
    }
}
