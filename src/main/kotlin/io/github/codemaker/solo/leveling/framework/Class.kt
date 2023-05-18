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
        ItemStack(Material.GOLDEN_APPLE, 10)
    ),
    ARCHER("&bArcher", Items.ARCHER_ICON, Items.ARCHER_ARMOR, Items.ARCHER_BOW, ItemStack(Material.GOLDEN_APPLE, 10)),
    MAGE("&dMage", Items.MAGE_ICON, Items.MAGE_ARMOR, Items.MAGE_WAND, ItemStack(Material.GOLDEN_APPLE, 10)),
    ASSASSIN("&4Assassin", Items.ASSASSIN_ICON, Items.MAGE_ARMOR, Items.ASSASSIN_SWORD, ItemStack(Material.GOLDEN_APPLE, 10)),
//    FIGHTER("&2Fighter", Items),


    Necromacer(
        "&1Necromacer",
        Items.Necromacer_ICON,
        Items.Necromacer_ARMOR,
        Items.Necromacer_SWORD,
        Items.Necromacer_WAND,
        ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 10)
    );

    val rawName: String? = Utils.decolor(displayName)
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
