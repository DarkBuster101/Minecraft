package io.github.codemaker.solo.leveling.framework

import io.github.codemaker.solo.leveling.Utils
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class Class(
    val displayName: String,
    icon: ItemStack?,
    val armor: Array<ItemStack?>?,
    vararg items: ItemStack
) {
    BERSKER(
        "&3Bersker",
        Items.BERSKER_ICON,
        Items.BERSKER_ARMOR,
        Items.BERSKER_SWORD,
        ItemStack(Material.COOKED_BEEF, 64)
    ),
    ARCHER("&bArcher", Items.ARCHER_ICON, Items.ARCHER_ARMOR, Items.ARCHER_BOW, ItemStack(Material.COOKED_BEEF, 64)),
    MAGE("&dMage", Items.MAGE_ICON, Items.MAGE_ARMOR, Items.MAGE_WAND, ItemStack(Material.COOKED_BEEF, 64));

    val rawName: String? = Utils.decolor(name)
    val items: Array<ItemStack>
    val icon: ItemStack?

    init {
        this.items = items as Array<ItemStack>
        this.icon = icon
    }

    companion object {
        fun getClassByName(name: String): Class? {
            for (clazz in values()) {
                if (name.equals(clazz.rawName, ignoreCase = true)) {
                    return clazz
                }
            }
            return null
        }
    }
}
