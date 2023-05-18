package io.github.codemaker.solo.leveling.configs

import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.framework.Class
import io.github.codemaker.solo.leveling.framework.Level
import java.util.*

class PlayerConfig(soloLeveling: SoloLeveling) : Config(soloLeveling, "players.yml") {
    /*For normal class and level*/
    fun getClass(id: UUID): Class? {
        return Class.getClassByName(getString("$id.class")!!)
    }

    fun setClass(id: UUID, clazz: Class?) {
        set("$id.class", clazz?.name)
    }

    fun getLevel(id: UUID): Level? {
        return Level.getLevelByKey(getString("$id.level")!!)
    }

    fun setLevel(id: UUID, level: Level?) {
        set("$id.level", level?.name)
    }

}
