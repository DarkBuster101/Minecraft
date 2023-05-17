package io.github.codemaker.solo.leveling

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.logging.Logger

object Utils {
    private val logger: Logger? = Main.Companion.pluginLogger
    fun color(string: String?): String {
        return ChatColor.translateAlternateColorCodes('&', string!!)
    }

    fun decolor(string: String?): String? {
        return ChatColor.stripColor(color(string))
    }

    fun actionbar(player: Player, string: String?) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(color(string)))
    }

    fun log(vararg strings: String?) {
        for (string in strings) {
            logger!!.info(string)
        }
    }

    fun msgPlayer(player: Player, vararg strings: String?) {
        for (string in strings) {
            player.sendMessage(color(string))
        }
    }

    fun createItem(
        material: Material?,
        amount: Int,
        glow: Boolean,
        unb: Boolean,
        name: String?,
        vararg lore: String?
    ): ItemStack {
        val item = ItemStack(material!!, amount)
        val meta = item.itemMeta
        if (name != null) {
            meta.setDisplayName(color(name))
        }
        if (lore != null) {
            val list: MutableList<String> = ArrayList()
            for (string in lore) {
                list.add(color(string))
            }
            meta.lore = list
        }
        if (glow) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            meta.addEnchant(Enchantment.DURABILITY, 1, true)
        }
        if (unb) {
            meta.isUnbreakable = true
        }
        item.setItemMeta(meta)
        return item
    }

    fun createArmor(
        helmet: ItemStack?,
        chestplate: ItemStack?,
        leggings: ItemStack?,
        boots: ItemStack?
    ): Array<ItemStack?> {
        val armor = arrayOfNulls<ItemStack>(4)
        armor[3] = helmet
        armor[2] = chestplate
        armor[1] = leggings
        armor[0] = boots
        return armor
    }
}
