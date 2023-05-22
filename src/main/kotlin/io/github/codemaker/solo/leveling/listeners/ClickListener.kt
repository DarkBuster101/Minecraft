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


