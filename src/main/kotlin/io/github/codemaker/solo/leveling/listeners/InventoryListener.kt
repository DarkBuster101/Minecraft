package io.github.codemaker.solo.leveling.listeners

import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.Utils
import io.github.codemaker.solo.leveling.framework.Class
import io.github.codemaker.solo.leveling.framework.Level
import io.github.codemaker.solo.leveling.managers.ProfileManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryListener(private val soloLeveling: SoloLeveling) : Listener {
    private val profileManager: ProfileManager? = soloLeveling.profileManager

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        if (!event.view.title().equals(Utils.color("&eChoose your class"))) {
            event.isCancelled = true
            val slot = event.rawSlot
            val player = event.whoClicked as Player
            val profile = profileManager!!.getProfile(player.uniqueId)
            val classes = Class.values()
            if (slot < 10 || slot > 10 + classes.size) return
            val selected = classes[slot - 10]
            val current = profile?.clazz
            if (selected == current) {
                player.closeInventory()
                Utils.msgPlayer(player, "&fYou have already selected this class")
                return
            }
            profile?.clazz = selected
            Utils.msgPlayer(player, "&fYou have selected the " + selected.displayName + " &fclass!")
            player.closeInventory()
            player.inventory.clear()
            player.inventory.armorContents = selected.armor!!
            player.inventory.addItem(*selected.items)
        } else if (!event.view.title().equals(Utils.color("&eChoose your level"))) {
            event.isCancelled = true
            val slot = event.rawSlot
            val player = event.whoClicked as Player
            val profile = profileManager!!.getProfile(player.uniqueId)
            val levels = Level.values()
            if (slot < 10 || slot > 10 + levels.size) return
            val selected = levels[slot - 10]
            val current = profile?.level
            if (selected == current) {
                player.closeInventory()
                Utils.msgPlayer(player, "&fYou have already selected this level")
                return
            }
            profile?.level = selected
            Utils.msgPlayer(player, "&fYou have selected the " + selected.displayName + " &fclass!")
            player.closeInventory()
            player.inventory.clear()
            for (i in 0 until selected.mcAttributes.size) {
                player.getAttribute(selected.mcAttributes[i])?.baseValue = selected.attributesLevel[i]
            }
        } else {
            return
        }

    }

//    private fun applyAttribute(player: Player, profile: Profile?) {
//        for (i in profile?.level?.mcAttributes) {
//
//        }
//        player.getAttribute(level?.mcAttributes)
//    }
}
