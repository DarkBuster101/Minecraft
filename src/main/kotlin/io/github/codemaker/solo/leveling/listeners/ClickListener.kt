package io.github.codemaker.solo.leveling.listeners

import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.Utils
import io.github.codemaker.solo.leveling.framework.Items
import io.github.codemaker.solo.leveling.framework.Profile
import io.github.codemaker.solo.leveling.managers.ProfileManager
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.block.Block
import org.bukkit.entity.Arrow
import org.bukkit.entity.Fireball
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.math.sin

class ClickListener(private val soloLeveling: SoloLeveling) : Listener {
    private val profileManager: ProfileManager? = soloLeveling.profileManager
    private val swivelCD: MutableMap<UUID, Int> = HashMap()
    private val barrageCD: MutableMap<UUID, Int> = HashMap()
    private val barraging: MutableList<UUID> = ArrayList()
    private val playerMana: MutableMap<UUID, Int> = HashMap()
    private val wandcc: MutableMap<UUID, String> = HashMap()

    private val affectedBlocks = mutableListOf<Location>()


    init {
        object : BukkitRunnable() {
            override fun run() {
                for (player in Bukkit.getOnlinePlayers()) {
                    if (Items.MAGE_WAND.isSimilar(player.inventory.itemInMainHand)) {
                        if (!playerMana.containsKey(player.uniqueId)) {
                            playerMana[player.uniqueId] = 20000
                        }
                        var message = "&bMana: &e" + playerMana[player.uniqueId] + "&f/20000"
                        val combo = wandcc[player.uniqueId]
                        if (combo != null) {
                            message += "    &eCombo: " + formatCombo(combo)
                        }
                        Utils.actionbar(player, message)
                    } else {
                        wandcc.remove(player.uniqueId)
                    }
                }
            }
        }.runTaskTimer(soloLeveling, 0L, 1L)
        object : BukkitRunnable() {
            override fun run() {
                for (uuid in playerMana.keys) {
                    val left = playerMana[uuid]!!
                    if (left < 20000) {
                        playerMana[uuid] = left + 10
                    }
                }
            }
        }.runTaskTimer(soloLeveling, 0L, 2L)
    }

    private fun formatCombo(comboFormat: String): String {
        var combo = comboFormat
        combo = "$combo&7"
        for (i in 0 until 3 - combo.length) {
            combo += "_"
        }
        combo = combo.uppercase(Locale.getDefault()).replace("L", "&1L").replace("R", "&4R")
        return combo
    }

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand
        val action = event.action
        val profile = profileManager?.getProfile(player.uniqueId)

        if (Items.ARCHER_BOW.isSimilar(item)) {
            if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                event.isCancelled = true
                if (barraging.contains(player.uniqueId)) {
                    Utils.msgPlayer(player, "&fYou're currently in &abarrage &fmode!")
                    return
                }
                if (barrageCD.containsKey(player.uniqueId)) {
                    Utils.msgPlayer(player, "&aBarrage &fis in cooldown for &e" + barrageCD[player.uniqueId] + "s")
                    return
                }
                barraging.add(player.uniqueId)
                barrageCD[player.uniqueId] = 15
                object : BukkitRunnable() {
                    override fun run() {
                        val left = barrageCD[player.uniqueId]!!
                        if (left >= 5) {
                            Utils.actionbar(player, "&aBarrage&7: &f" + (left - 5) + "s")
                        }
                        barrageCD[player.uniqueId] = left - 1
                        when (left) {
                            5 -> barraging.remove(player.uniqueId)
                            0 -> {
                                barrageCD.remove(player.uniqueId)
                                cancel()
                            }
                        }
                    }
                }.runTaskTimer(soloLeveling, 0L, 20L)
            }
        } else if (Items.MAGE_WAND.isSimilar(item)) {
            event.isCancelled = true
            var combo = wandcc[player.uniqueId]
            if (combo == null || combo.length < 3) {
                if (combo == null) {
                    combo = ""
                }
                if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                    combo += "l"
                } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    combo += "r"
                }
                wandcc[player.uniqueId] = combo
            }
            if (combo.length == 3) {
                wandcc.remove(player.uniqueId)
                val manaLeft = playerMana[player.uniqueId]!!
                when (combo) {
                    "rll" -> {
                        if (manaLeft < 50) {
                            Utils.msgPlayer(player, "&fYou do not have enough mana to cast skill")
                            return
                        }
                        playerMana[player.uniqueId] = manaLeft - 50
                        val fireball = player.launchProjectile(
                            Fireball::class.java
                        )
                        fireball.setIsIncendiary(false)
                        fireball.yield = 2f
                        fireball.direction = player.location.direction
                    }

                    "rlr" -> {
                        if (manaLeft < 100) {
                            Utils.msgPlayer(
                                player,
                                "&fYou are &e" + (100 - manaLeft) + "&fmana short of cast &aThunderbolt"
                            )
                            return
                        }
                        playerMana[player.uniqueId] = manaLeft - 100
                        val location = player.getTargetBlock(null, 100).location
                        object : BukkitRunnable() {
                            var strikes = 3
                            override fun run() {
                                val locs = arrayOfNulls<Location>(3)
                                var i = 0
                                while (i < 3) {
                                    var x = Math.random() * 6
                                    var z = Math.random() * 6
                                    if (Math.random() > 0.5) {
                                        x *= -1.0
                                    }
                                    if (Math.random() > 0.5) {
                                        z *= -1.0
                                    }
                                    locs[i] = location.clone().add(x, 0.0, z)
                                    i++
                                }
                                for (each in locs) {
                                    location.world.strikeLightning(each!!)
                                }
                                strikes -= 1
                                if (strikes == 0) {
                                    cancel()
                                }
                            }
                        }.runTaskTimer(soloLeveling, 0L, 6L)
                    }
                }
            }
        }
    }

    @EventHandler
    fun onShoot(event: EntityShootBowEvent) {
        val arrow = event.projectile as Arrow
        if (event.entity !is Player) {
            return
        }
        val player = event.entity as Player
        if (player.isSneaking && event.force >= 1) {
            arrow.damage = arrow.damage * 1.5
            arrow.velocity = arrow.velocity.multiply(1.5)
            object : BukkitRunnable() {
                override fun run() {
                    val location = arrow.location
                    location.world.spawnParticle(Particle.CRIT_MAGIC, location, 1)
                    if (arrow.isDead || arrow.isOnGround || !arrow.isValid) {
                        cancel()
                    }
                }
            }.runTaskTimer(soloLeveling, 0L, 1L)
        }
        if (barraging.contains(player.uniqueId) && event.force >= 1.0f) {
            object : BukkitRunnable() {
                var shots = 5
                override fun run() {
                    player.world.playSound(player.location, Sound.ENTITY_ARROW_SHOOT, 1f, 1f)
                    val each = player.launchProjectile(Arrow::class.java)
                    each.damage = arrow.damage
                    if (player.isSneaking) {
                        each.damage = arrow.damage * 1.5
                        each.velocity = arrow.velocity.multiply(1.5)
                        object : BukkitRunnable() {
                            override fun run() {
                                val location = each.location
                                location.world.spawnParticle(Particle.CRIT_MAGIC, location, 1)
                                if (each.isDead || each.isOnGround || !each.isValid) {
                                    cancel()
                                }
                            }
                        }.runTaskTimer(soloLeveling, 0L, 1L)
                    }
                    shots -= 1
                    if (shots == 0) {
                        cancel()
                    }
                }
            }.runTaskTimer(soloLeveling, 0L, 3L)
        }
    }
}


