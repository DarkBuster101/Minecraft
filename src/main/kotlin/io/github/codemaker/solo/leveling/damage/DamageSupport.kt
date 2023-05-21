package io.github.codemaker.solo.leveling.damage

import io.github.codemaker.solo.leveling.damage.DamageSupport.getProtection
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import kotlin.math.min

object DamageSupport {
    fun calculateDamage(damage: Double, armor: Double, armorTough: Double, protection: Double): Double {
        return (1.0 - 0.04 * protection) * damage * (1.0 + (-min(armor, armorTough) - armor) / 50.0)
    }

    /**
     * 아이템의 보호 인챈트 수치를 가져옵니다.
     * [Enchantment.PROTECTION_ENVIRONMENTAL]의 경우 두배의 수치를 반환합니다.
     */
    fun ItemStack.getProtection(enchantment: Enchantment): Int {
        var protection = getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)

        if (enchantment != Enchantment.PROTECTION_ENVIRONMENTAL) {
            val enchantmentProtection = getEnchantmentLevel(enchantment) shl 1

            if (protection < enchantmentProtection) {
                protection = enchantmentProtection
            }
        }

        return protection
    }

    /**
     * 개체 방어구의 보호 인챈트 수치를 가져옵니다.
     *
     * 수치는 [getProtection]을 통해 계산되며 최대 40을 반환합니다.
     */
    fun LivingEntity.getProtection(enchantment: Enchantment): Int {
        val armorContents = equipment?.armorContents ?: return 0
        var protection = 0

        armorContents.asSequence().filterNotNull().forEach lit@{ item ->
            protection += item.getProtection(enchantment)
            if (protection >= 40) return@lit
        }

        return min(40, protection)
    }

//    fun inverseDamage(damage: Double, armor: Double, armorTough: Double, protection: Double): Double {
//        return damage / (1.0 - 0.04 * protection) / (1.0 + (-min(armor, armorTough) - armor) / 50.0)
//    }
//
//    fun calculateAttackDamage(armor: Double, armorTough: Double, psionicsLevel: Double) =
//        inverseDamage(1.0, armor, armorTough, psionicsLevel)
//
//    val LivingEntity.attackDamage: Double
//        get() {
//            val armor = getAttribute(Attribute.GENERIC_ARMOR)?.value ?: 0.0
//            val armorTough = getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)?.value ?: 0.0
//            val psionicsLevel = equipment?.armorContents?.asSequence()?.filterNotNull()?.sumOf { it.getProtection(
//                Enchantment.PROTECTION_ENVIRONMENTAL) } ?: 0
//
//            return calculateAttackDamage(armor, armorTough, psionicsLevel.toDouble())
//        }
}