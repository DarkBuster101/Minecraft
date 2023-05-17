package io.github.codemaker.solo.leveling.framework

import org.bukkit.attribute.Attribute
import org.bukkit.inventory.ItemStack

enum class Level(
    val key: String,
    val icon: ItemStack?,
    val displayName: String,
    val mcAttributes: Array<Attribute>,
    val attributesLevel: Array<Double>,
    val startingMoney: Int
) {

    S_LEVEL("s", Items.S_LEVEL_ICON, "S급", arrayOf(Attribute.GENERIC_ATTACK_SPEED, Attribute.GENERIC_MAX_HEALTH, Attribute.GENERIC_ATTACK_DAMAGE, Attribute.GENERIC_ARMOR, Attribute.GENERIC_MOVEMENT_SPEED), arrayOf(1024.0, 60.0, 10.0, 30.0, 1.2), 10000),
    A_LEVEL("a", Items.A_LEVEL_ICON, "A급", arrayOf(Attribute.GENERIC_ATTACK_SPEED, Attribute.GENERIC_MAX_HEALTH, Attribute.GENERIC_ATTACK_DAMAGE, Attribute.GENERIC_ARMOR, Attribute.GENERIC_MOVEMENT_SPEED), arrayOf(922.0, 50.0, 8.0, 25.0, 1.0), 8000),
    B_LEVEL("b", Items.B_LEVEL_ICON, "B급", arrayOf(Attribute.GENERIC_ATTACK_SPEED, Attribute.GENERIC_MAX_HEALTH, Attribute.GENERIC_ATTACK_DAMAGE, Attribute.GENERIC_ARMOR, Attribute.GENERIC_MOVEMENT_SPEED), arrayOf(816.0, 40.0, 6.0, 20.0, 0.8), 6000),
    C_LEVEL("c", Items.C_LEVEL_ICON, "C급", arrayOf(Attribute.GENERIC_ATTACK_SPEED, Attribute.GENERIC_MAX_HEALTH, Attribute.GENERIC_ATTACK_DAMAGE, Attribute.GENERIC_MOVEMENT_SPEED), arrayOf(717.0, 35.0, 4.0, 0.6), 4000),
    D_LEVEL("d", Items.D_LEVEL_ICON, "D급", arrayOf(Attribute.GENERIC_ATTACK_SPEED, Attribute.GENERIC_MAX_HEALTH, Attribute.GENERIC_ATTACK_DAMAGE, Attribute.GENERIC_MOVEMENT_SPEED), arrayOf(615.0, 30.0, 3.0, 0.4), 2000),
    E_LEVEL("e", Items.E_LEVEL_ICON, "E급", arrayOf(Attribute.GENERIC_ATTACK_SPEED, Attribute.GENERIC_MAX_HEALTH, Attribute.GENERIC_ATTACK_DAMAGE, Attribute.GENERIC_MOVEMENT_SPEED), arrayOf(512.0, 25.0, 2.5, 0.2), 1000);

    companion object {
        fun getLevelByKey(key: String): Level? {
            for (levels in values()) {
                if (key.equals(levels.key, ignoreCase = true)) {
                    return levels
                }
            }
            return null
        }
    }


}