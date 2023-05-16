package io.github.codemaker.solo.leveling

import io.github.monun.tap.config.Config
import io.github.monun.tap.config.ConfigSupport.compute
import java.io.File

object SoloLevelingConfig {

    @Config
    var defaultUpgrader = arrayListOf(
        "zqurl"
    )

    @Config
    var defaultSLevel = arrayListOf(
        "gguggury"
    )

    @Config
    var defaultLevels = arrayListOf(
        "S",
        "A",
        "B",
        "C",
        "D",
        "E"
    )

    @Config
    var defaultClazz = arrayListOf(
        "Mage",
        "Berserker",
        "Bandit",
        "Archer",
        "Fighter",
        "Healer",
        "Tank"
    )

    @Config
    var hiddenClazz = arrayListOf(
        "Necromancer"
    )

    lateinit var defaultUpgraders: Set<String>
    lateinit var defaultSLevels: Set<String>

    fun load(configFile: File) {
        compute("type", configFile)

        defaultUpgraders = defaultUpgrader.map { it.trim() }.toSortedSet(String.CASE_INSENSITIVE_ORDER)
        defaultSLevels = defaultSLevel.map { it.trim() }.toSortedSet(String.CASE_INSENSITIVE_ORDER)
    }

}