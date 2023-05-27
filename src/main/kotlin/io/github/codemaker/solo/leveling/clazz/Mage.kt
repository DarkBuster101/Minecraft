package io.github.codemaker.solo.leveling.clazz

import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.Utils
import io.github.codemaker.solo.leveling.framework.Class
import io.github.codemaker.solo.leveling.framework.Items
import io.github.codemaker.solo.leveling.managers.ProfileManager
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.Fireball
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class Mage(private val soloLeveling: SoloLeveling) : Listener {
    private val profileManager: ProfileManager? = soloLeveling.profileManager
    private val playerMana: MutableMap<UUID, Int> = HashMap()
    private val barrierCD: MutableMap<UUID, Int> = HashMap()
    private val wandcc: MutableMap<UUID, String> = HashMap()

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

    private fun createFireballRain(centerLocation: Location) {
        val world: World? = centerLocation.world
        val radius = 10.0 // Adjust the radius of the fireball rain
        val numFireballs = 15 // Adjust the number of fireballs in the rain

        for (i in 0 until numFireballs) {
            val x = centerLocation.x + (Math.random() * radius * 2 - radius)
            val y = centerLocation.y + 50.0 // Start the fireballs from a higher altitude
            val z = centerLocation.z + (Math.random() * radius * 2 - radius)

            val fireballLocation = Location(world, x, y, z)
            val fireball = world?.spawn(fireballLocation, Fireball::class.java)

            // Customize fireball properties if desired
            fireball?.yield = 4f
            fireball?.setIsIncendiary(true) // Set to true if you want the fireballs to ignite blocks
            fireball?.direction = centerLocation.toVector().subtract(fireballLocation.toVector()).normalize()


            createFlameParticles(fireballLocation, fireball?.direction)
        }
    }
    private fun createFlameParticles(startLocation: Location, direction: Vector?) {
        object : BukkitRunnable() {
            var distance = 0.0
            val maxDistance = 50.0 // Adjust the maximum distance for flame particles

            override fun run() {
                if (distance >= maxDistance) {
                    cancel()
                    return
                }

                val currentLocation = startLocation.clone().add(direction?.multiply(distance) ?: return)
                currentLocation.world?.spawnParticle(Particle.FLAME, currentLocation, 5)

                distance += 0.5 // Adjust the speed of the flame particles
            }
        }.runTaskTimer(soloLeveling, 0, 1) // Adjust the delay and period for the flame particle creation
    }

    private fun ignite(target: LivingEntity) { //passive
        target.fireTicks = 20*10
    }

    private fun createDomeBarrierParticles(player: Player) {
        object : BukkitRunnable() {
            private var iterations = 0
            private val maxIterations = 100 // Adjust the number of iterations (duration) as desired

            override fun run() {
                if (iterations >= maxIterations) {
                    cancel()
                    return
                }

                val radius = 5.0 // Adjust the radius of the dome
                val height = 5.0 // Adjust the height of the dome

                // Get the player's location
                val location: Location = player.location

                // Iterate through the dome shape
                for (theta in 0..360 step 10) {
                    for (phi in 0..90 step 10) {
                        // Calculate the spherical coordinates
                        val x = radius * sin(Math.toRadians(phi.toDouble())) * cos(Math.toRadians(theta.toDouble()))
                        val y = radius * sin(Math.toRadians(phi.toDouble())) * sin(Math.toRadians(theta.toDouble()))
                        val z = height * cos(Math.toRadians(phi.toDouble()))

                        // Offset the coordinates relative to the player's location
                        val offsetX = x / 10
                        val offsetY = z / 10
                        val offsetZ = y / 10

                        // Spawn particle effects to create the dome shape
                        val particle = Particle.CRIT_MAGIC // Adjust the particle type as desired
                        val particleCount = 1
                        val particleOffset = 0.0
                        val particleData = null

                        location.world.spawnParticle(
                            particle,
                            location.x + offsetX,
                            location.y + offsetY,
                            location.z + offsetZ,
                            particleCount,
                            particleOffset,
                            particleOffset,
                            particleOffset,
                            particleData
                        )
                    }
                }
            }
        }.runTaskTimer(soloLeveling, 0L, 10L) // Adjust the delay and period as desired
    }

//    private fun startProjectileLocationChecker() {
//        object : BukkitRunnable() {
//            override fun run() {
//                val projectiles = server.worlds.flatMap { it.entities }.filterIsInstance<Projectile>()
//
//                for (projectile in projectiles) {
//                    // Check the projectile's location here
//                    val location = projectile.location
//
//                    // Perform any desired actions based on the projectile's location
//                    // For example, you can check if it's inside the barrier and remove it if needed
//                    if (isInsideBarrier(location)) {
//                        projectile.remove()
//                    }
//                }
//            }
//        }.runTaskTimer(soloLeveling, 0L, 1L) // Adjust the delay and period as needed
//    }
//
//    private fun isInsideBarrier(location: Location): Boolean {
//        // Implement your logic to determine if the location is inside the barrier
//        // Return true if the location is inside the barrier, false otherwise
//        // You can check coordinates, boundaries, or any other criteria you have defined for the barrier
//        if (location)
//
//        return false
//    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        val projectile = event.entity

        // Check if the projectile is inside the barrier
        if (event.hitEntity != null) {
            if (event.hitEntity is Player) {
                if (barrierCD.containsKey(event.hitEntity?.uniqueId)) {
                    event.isCancelled = true
                    projectile.remove()
                }
            }
        }

    }


    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        val player = event.player
        val profile = profileManager?.getProfile(player.uniqueId)
        val targetEntity: LivingEntity? = player.getTargetEntity(4, false) as LivingEntity?
        val targetBlock: Block = player.getTargetBlock(null, 4)
        val rayTraceEntity : LivingEntity? = player.rayTraceEntities(4, false) as LivingEntity?
        val action = event.action

        if (profile?.clazz?.name == Class.MAGE.toString()) {
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
                        createFireballRain(targetBlock.location)
                        targetBlock.location.getNearbyLivingEntities(1.0).forEach {
                            it.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 2, 255))
                            ignite(it)
                        }
                    }

                    "lrl" -> {
                        if (manaLeft < 70) {
                            Utils.msgPlayer(player, "&fYou do not have enough mana to cast skill")
                            return
                        }
                        if (barrierCD.containsKey(player.uniqueId)) {
                            Utils.msgPlayer(player, "&fCooldown of &e" + barrierCD[player.uniqueId] + "s for &aBarrier!")
                            return
                        }

                        playerMana[player.uniqueId] = manaLeft - 70
                        createDomeBarrierParticles(player)

                        barrierCD[player.uniqueId] = 15
                        object : BukkitRunnable() {
                            override fun run() {
                                val currentCD = barrierCD[player.uniqueId]!!
                                if (currentCD > 0) {
                                    barrierCD[player.uniqueId] = currentCD - 1
                                }
                                if (currentCD == 0) {
                                    barrierCD.remove(player.uniqueId)
                                    cancel()
                                }
                            }
                        }.runTaskTimer(soloLeveling, 0L, 20L)
                    }

                    "rlr" -> { //ult
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
                            var strikes = 7
                            override fun run() {
                                val locs = arrayOfNulls<Location>(7)
                                var i = 0
                                while (i < 7) {
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
                                    location.world.strikeLightningEffect(each!!)
                                    val targets = each.getNearbyLivingEntities(1.0)
                                    targets.forEach {
                                        it.health -= 10
                                        ignite(it)
                                    }
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

}