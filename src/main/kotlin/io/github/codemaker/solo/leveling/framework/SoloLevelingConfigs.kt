package io.github.codemaker.solo.leveling.framework

import org.bukkit.Material

object SoloLevelingConfigs {

    var defaultUpgrader = arrayListOf(
        "zqurl",
        "gguggury"
    )


    var defaultClazz = arrayListOf(
        "Mage", //armor with more mana(intelligence), fewer defences; weapon with different types of magic (fire, light, water, earth);
        "Berserker", //armor with more strength and defences; weapon with different types of sword (dagger, long-sword);
        "Assassin", //armor with more speed and moderate defences; weapon with different types of sword (dagger, shuriken);
        "Archer", //armor with more speed and fewer defences; weapon with different types of bow (magic, normal);
        "Fighter", //armor with more strength, speed and moderate defences; weapon is a gantlet;
        "Healer", //armor with more mana(intelligence), more speed; no or little attack skill; Healing passive or spell book instead;
        "Tank" //armor with high defences and health; little attack skill; shield and assistant skills
    )


    var forbiddenItems = arrayListOf(
        Material.TOTEM_OF_UNDYING,
        Material.ELYTRA,
        Material.HOPPER,
        Material.HOPPER_MINECART,
        Material.SHIELD,
        Material.FIREWORK_ROCKET,
        Material.END_CRYSTAL,
        Material.RESPAWN_ANCHOR
    )
}