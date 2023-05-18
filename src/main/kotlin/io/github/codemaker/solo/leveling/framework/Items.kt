package io.github.codemaker.solo.leveling.framework

import io.github.codemaker.solo.leveling.Utils
import io.github.codemaker.solo.leveling.Utils.returnAttributeModifier
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

object Items {
    val BERSKER_ICON = Utils.createItem(
        Material.IRON_SWORD,
        1,
        false,
        false,
        "&3Bersker",
        "&7A decent melee character with decent armor"
    )
    val ARCHER_ICON =
        Utils.createItem(Material.BOW, 1, false, false, "&bArcher", "&7A grate ranged class with weak armor")
    val MAGE_ICON = Utils.createItem(Material.STICK, 1, false, false, "&dMage", "&7A Magical class with weak armor")
    val BERSKER_ARMOR = Utils.createArmor(
        Utils.createItem(Material.IRON_HELMET, 1, false, true, null), Utils.createItem(
            Material.IRON_CHESTPLATE, 1, false, true, null
        ), Utils.createItem(Material.IRON_LEGGINGS, 1, false, true, null), Utils.createItem(
            Material.IRON_BOOTS, 1, false, true, null
        )
    )
    val ARCHER_ARMOR = Utils.createArmor(
        Utils.createItem(Material.CHAINMAIL_HELMET, 1, false, true, null), Utils.createItem(
            Material.CHAINMAIL_CHESTPLATE, 1, false, true, null
        ), Utils.createItem(Material.LEATHER_LEGGINGS, 1, false, true, null), Utils.createItem(
            Material.LEATHER_BOOTS, 1, false, true, null
        )
    )
    val MAGE_ARMOR = Utils.createArmor(
        Utils.createItem(Material.GOLDEN_HELMET, 1, false, true, null), Utils.createItem(
            Material.LEATHER_CHESTPLATE, 1, false, true, null
        ), Utils.createItem(Material.CHAINMAIL_LEGGINGS, 1, false, true, null), Utils.createItem(
            Material.GOLDEN_BOOTS, 1, false, true, null
        )
    )
    val BERSKER_SWORD = Utils.createItem(
        Material.IRON_SWORD,
        1,
        false,
        true,
        "&3Bersker's Sword",
        "&eSKills:",
        "&aDash &8[&4R&8] &7- &fCharges forward and damages",
        "&7enemies nearby"
    )
    val ARCHER_BOW = Utils.createItem(
        Material.BOW,
        1,
        false,
        true,
        "&bArcher's Bow",
        "&eSkills:",
        "&aConcentrate &8[&9Crouch&8] &7- &fWhile sneaking, fully",
        "&fcharged arrows will shot faster and more damages",
        "&7",
        "&aBarrage &8[&1L&8] &7- &fFor 10 seconds, fully",
        "&fcharged arrows will shot in burs"
    )
    val MAGE_WAND = Utils.createItem(
        Material.STICK,
        1,
        false,
        true,
        "&dMage's Wand",
        "&eSkills:",
        "&aFireBall &8[&4r&1LL&8] &7- &fCast a",
        "&ffireball for &e50 &fmana",
        "&7",
        "&aThunderbolt &8[&4R&1L&4R&8]",
        "&fSummon a few",
        "&fdevastating lightning bolts where you're looking for &e100 &fmana"
    )

    val S_LEVEL_ICON = Utils.createItem(
        Material.NETHERITE_HELMET,
        1,
        true,
        false,
        "&3S Level Hunter",
        "&7Become a strongest hunter in the world"
    )

    val A_LEVEL_ICON = Utils.createItem(
        Material.DIAMOND_HELMET,
        1,
        true,
        false,
        "&3A Level Hunter",
        "&7Become a stronger hunter"
    )

    val B_LEVEL_ICON = Utils.createItem(
        Material.IRON_HELMET,
        1,
        true,
        false,
        "&3B Level Hunter",
        "&7Become a middle class hunter"
    )


    val C_LEVEL_ICON = Utils.createItem(
        Material.CHAINMAIL_HELMET,
        1,
        false,
        false,
        "&3C Level Hunter",
        "&7Become a normal hunter"
    )

    val D_LEVEL_ICON = Utils.createItem(
        Material.GOLDEN_HELMET,
        1,
        false,
        false,
        "&3D Level Hunter",
        "&7Become a weak hunter"
    )

    val E_LEVEL_ICON = Utils.createItem(
        Material.LEATHER_HELMET,
        1,
        false,
        false,
        "&3E Level Hunter",
        "&7Become a weakest hunter"
    )

    val Necromacer_ICON = Utils.createItem(
        Material.WITHER_SKELETON_SKULL,
        1,
        true,
        false,
        "&1어둠의 군주",
        "&7최강의 군주 - 어둠의 군주"
    )

    private val armorAttribute = Attribute.GENERIC_ARMOR_TOUGHNESS
    private val armorAttributeModifier = returnAttributeModifier(armorAttribute, 20.0, AttributeModifier.Operation.ADD_NUMBER)

    val Necromacer_ARMOR = Utils.createArmor(
        Utils.createItem(Material.NETHERITE_HELMET, 1, true, true, "&1어둠의 군주의 투구", "&7기본으로 지급되는 어둠의 군주의 투구입니다", attribute = armorAttribute, attributeModifier = armorAttributeModifier),
        Utils.createItem(Material.IRON_CHESTPLATE, 1, false, true, "&1어둠의 군주의 흉갑", "&7기본으로 지급되는 어둠의 군주의 흉갑입니다", attribute = armorAttribute, attributeModifier = armorAttributeModifier),
        Utils.createItem(Material.IRON_LEGGINGS, 1, false, true, "&1어둠의 군주의 각반", "&7기본으로 지급되는 어둠의 군주의 각반입니다", attribute = armorAttribute, attributeModifier = armorAttributeModifier),
        Utils.createItem(Material.IRON_BOOTS, 1, false, true, "&1어둠의 군주의 신발", "&7기본으로 지급되는 어둠의 군주의 신발입니다", attribute = armorAttribute, attributeModifier = armorAttributeModifier)
    )

    val Necromacer_SWORD = Utils.createItem(
        Material.NETHERITE_SWORD,
        1,
        true,
        true,
        "&1어둠의 군주의 검",
        "&7기본으로 지급되는 어둠의 군주의 검입니다"
    )

    val Necromacer_WAND = Utils.createItem(
        Material.BLAZE_ROD,
        1,
        true,
        true,
        "&1어둠의 군주의 지팡이",
        "&7기본으로 지급되는 어둠의 군주의 지팡이입니다"
    )

    val ASSASSIN_ICON = Utils.createItem(
        Material.REDSTONE,
        1,
        false,
        false,
        "&4암살자",
        "&7암살자가 되어 적들을 암살해 보세요!"
    )

    val ASSASSIN_SWORD = Utils.createItem( //continue, armor with invisibility and speed
        Material.IRON_SWORD,
        1,
        false,
        false,
        "&4암살자의 검",
        "&7암살자가 되어 적들을 암살해 보세요!"
    )

    val FIGHTER_ICON = Utils.createItem(
        Material.NETHER_PORTAL,
        1,
        false,
        false,
        "&4무투가",
        "&7무투가가 되어 세상을 지배해보세요!"
    )

    val FIGHTER_SWORD = Utils.createItem(
        Material.IRON_SWORD,
        1,
        false,
        false,
        "&4무투가의 검",
        "&7무투가가 되어 세상을 지배해보세요!"
    )


}
