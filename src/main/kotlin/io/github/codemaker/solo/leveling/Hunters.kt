package io.github.codemaker.solo.leveling

import org.bukkit.configuration.ConfigurationSection
import kotlin.random.Random

abstract class Hunters(type: Hunter) {
    enum class Hunter(
        val key: String,
        val displayNames: String,
        val creator: () -> Hunters
    ) {
        S_LEVEL("s", "S급 ", ::S_Level),
        A_LEVEL("a", "A급 ", ::A_Level),
        B_LEVEL("b", "B급 ", ::B_Level),
        C_LEVEL("c", "C급 ", ::C_Level),
        D_LEVEL("d", "D급 ", ::D_Level),
        E_LEVEL("e", "E급 ", ::E_Level),

        Mage("mage", "마법사", ::Mage),
        Berserker("berserker", "전사", ::Berserker),
        Bandit("bandit", "도적", ::Bandit),
        Archer("archer", "궁수", ::Archer),
        Healer("healer", "힐러", ::Healer),
        Tank("tank", "탱커", ::Tank);


        override fun toString(): String {
            return displayNames
        }



        companion object {
//            private val levelValues = values().filter { it.name.endsWith("_LEVEL") }
//            private val classValues = values().filter { !it.name.endsWith("_LEVEL") }
//            private val random = Random
//
//            private fun randomCombination() : String {
//                val randomLevel = levelValues[random.nextInt(levelValues.size)]
//                val randomClass = classValues[random.nextInt(classValues.size)]
//                return "$randomLevel : $randomClass"
//            }
//
//            fun byKey(levels: String, clazz: String): String {
//                return randomCombination()
//            }
            var combinationMap: MutableMap<String, String> = randomCombination()

            private val levelValues = values().filter { it.name.endsWith("_LEVEL") }
            private val classValues = values().filter { !it.name.endsWith("_LEVEL") }
            private val random = Random

            private fun randomCombination(): MutableMap<String, String> {
                val randomLevel = levelValues[random.nextInt(levelValues.size)]
                val randomClass = classValues[random.nextInt(classValues.size)]
                val retrievedValue = "${randomLevel}-${randomClass}"

                combinationMap["${randomLevel.key}_${randomClass.key}"] = retrievedValue

                return combinationMap
//                return randomLevel.key.plus("_") + randomClass.key, retrievedValue
            }

            private fun byKey(key: String): String? {
                return combinationMap[key]
            }

        }

        /*example usage of function example*/
//        fun example() {
//            val combinationMap: MutableMap<Pair<String, String>, String> = mutableMapOf()
//
//            for (i in 1..5) {
//                val randomCombination = Hunter.randomCombination()
//                val key = randomCombination.first to randomCombination.second
//                val value = "Value $i"
//
//                combinationMap[key] = value
//            }
//
//            // Print the combination map
//            combinationMap.forEach { (key, value) ->
//                println("Combination: $key -> $value")
//            }
//
//            // Example usage of byKey function
//            val randomKey = combinationMap.keys.random()
//            val retrievedValue = Hunter.byKey(randomKey)
//
//            println("Retrieved Value for key $randomKey: $retrievedValue")
//        }
    }
}