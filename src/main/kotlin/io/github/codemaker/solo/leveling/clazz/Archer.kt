package io.github.codemaker.solo.leveling.clazz

import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.Utils
import io.github.codemaker.solo.leveling.framework.Class
import io.github.codemaker.solo.leveling.framework.Items
import io.github.codemaker.solo.leveling.managers.ProfileManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class Archer(private val soloLeveling: SoloLeveling): Listener {

    private val profileManager: ProfileManager? = soloLeveling.profileManager
    private val sonicPiercerCD: MutableMap<UUID, Int> = HashMap() //ult
    private val ejectionCD: MutableMap<UUID, Int> = HashMap() //jump
    private val guideMissileCD: MutableMap<UUID, Int> = HashMap() //barraging
    private var arrow: Arrow? = null
    private val arrowName = Component.text("Sonic Piercer").color(TextColor.color(51, 255, 255))


    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action
        val profile = profileManager?.getProfile(player.uniqueId)
        val targetEntity: LivingEntity? = player.getTargetEntity(70, false) as LivingEntity?
        val rayTraceEntity: LivingEntity? = player.rayTraceEntities(70, false) as LivingEntity?

//        var damages = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value!!
//        val armor = targetEntity?.getAttribute(Attribute.GENERIC_ARMOR)?.value!!
//        val armorTough = targetEntity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)?.value!!
//        val protection = targetEntity.getProtection(Enchantment.PROTECTION_ENVIRONMENTAL).toDouble()
//
//        val calculatedDamage = DamageSupport.calculateDamage(damages, armor, armorTough, protection)


        if (profile?.clazz?.name == Class.ARCHER.toString()) {
            if (action.isRightClick && player.isSneaking) {
                event.isCancelled = true


                if (sonicPiercerCD.containsKey(player.uniqueId)) {
                    Utils.msgPlayer(player, "&fCooldown of &e" + sonicPiercerCD[player.uniqueId] + "s for &aSonic Piercer!")
                    return
                }

                player.launchProjectile(Arrow::class.java, player.eyeLocation.direction).apply {
                    this.remove()
                    spawnUltArrow(this)
                    arrow = this
                }
                arrow?.attachedBlock?.location?.createExplosion(30f)
                arrow?.location?.let { createShockwave(it, 30.0) }

                sonicPiercerCD[player.uniqueId] = 100
                object : BukkitRunnable() {
                    override fun run() {
                        val currentCD = sonicPiercerCD[player.uniqueId]!!
                        if (currentCD > 0) {
                            sonicPiercerCD[player.uniqueId] = currentCD - 1
                        }
                        if (currentCD == 0) {
                            sonicPiercerCD.remove(player.uniqueId)
                            cancel()
                        }
                    }
                }.runTaskTimer(soloLeveling, 0L, 20L)
            }

            if (action.isRightClick && !player.isSneaking) {
                event.isCancelled = true

                if (guideMissileCD.containsKey(player.uniqueId)) {
                    Utils.msgPlayer(player, "&fCooldown of &e" + guideMissileCD[player.uniqueId] + "s for &aQuick shots!")
                    return
                }
                if (targetEntity == null) {
                    Utils.msgPlayer(player, "&fPlease Select the target!")
                    return
                } else {
                    spawnGuidedArrows(player, targetEntity, 15)
                    guideMissileCD[player.uniqueId] = 30
                    object : BukkitRunnable() {
                        override fun run() {
                            val currentCD = guideMissileCD[player.uniqueId]!!
                            if (currentCD > 0) {
                                guideMissileCD[player.uniqueId] = currentCD - 1
                            }
                            if (currentCD == 0) {
                                guideMissileCD.remove(player.uniqueId)
                                cancel()
                            }
                        }
                    }.runTaskTimer(soloLeveling, 0L, 20L)
                }
            }

            if (action.isLeftClick && !player.isSneaking) {
                event.isCancelled = true

                shootStraightArrow(player)
            }

            if (action.isLeftClick && player.isSneaking) {
                event.isCancelled = true

                if (ejectionCD.containsKey(player.uniqueId)) {
                    Utils.msgPlayer(player, "&fCooldown of &e" + ejectionCD[player.uniqueId] + "s for &aEjection!")
                    return
                }

                val direction = player.location.direction.multiply(-1).normalize().multiply(10)

                player.velocity = direction

                for (entity: Entity in player.getNearbyEntities(10.0, 10.0, 10.0)) {
                    if (entity is LivingEntity) {
                        val entityDirection = entity.location.subtract(player.location).toVector().normalize().multiply(1.5)
                        entity.velocity = entityDirection
                    }
                }

                ejectionCD[player.uniqueId] = 10
                object : BukkitRunnable() {
                    override fun run() {
                        val currentCD = ejectionCD[player.uniqueId]!!
                        if (currentCD > 0) {
                            ejectionCD[player.uniqueId] = currentCD - 1
                        }
                        if (currentCD == 0) {
                            ejectionCD.remove(player.uniqueId)
                            cancel()
                        }
                    }
                }.runTaskTimer(soloLeveling, 0L, 20L)
            }
        }
    }

    @EventHandler
    fun onProjectileLaunch(event: ProjectileLaunchEvent) {
        val projectile = event.entity

        if (projectile.shooter is Player && projectile is Arrow) {
            val shooter: Player = projectile.shooter as Player

            if (shooter.inventory.itemInMainHand.isSimilar(Items.ARCHER_BOW)) {
                projectile.isGlowing = true // Example: Make the arrow glowing
            }
        }
    }

    private fun shootStraightArrow(player: Player) {
        val arrow: Arrow = player.launchProjectile(Arrow::class.java)
        val direction: Vector = player.eyeLocation.direction

        arrow.velocity = direction.multiply(2) // Adjust the velocity as needed
    }

    @EventHandler
    fun onArrowDamageEntity(event: EntityDamageByEntityEvent) {
        if (event.damager !is Arrow) return
        if (event.damager.customName() != arrowName) return
        if (event.entity !is LivingEntity) return

        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard

        if (scoreboard.getTeam("marked")?.hasEntity(event.entity as LivingEntity) == true) {
            (event.entity as LivingEntity).damage(10.0)
            (event.entity as LivingEntity).addPotionEffect(PotionEffect(PotionEffectType.WITHER, 10, 3))
            scoreboard.getTeam("marked")?.removeEntity(event.entity)
        }

        val location = event.entity.location
        location.createExplosion(30f)
        createShockwave(location, 30.0)
        displayMark(event.entity as LivingEntity)
    }


    private fun createParticleTrail(arrow: Arrow) {
        val location = arrow.location
        val direction = arrow.velocity.normalize()

        object : BukkitRunnable() {
            override fun run() {
                val particleDensity = 0.1 // Higher values produce more particles
                val particleSpeed = 0.05 // Higher values make particles move faster
                val particleSize = 2.0 //size of arrow
                val angleStep = 360.0 / (2 * Math.PI * particleDensity)
                var angle = 0.0

                if (!arrow.isValid) return

                while (angle < 360) {
                    val radians = Math.toRadians(angle)
                    val x = cos(radians) * direction.x - sin(radians) * direction.z
                    val y = direction.y
                    val z = sin(radians) * direction.x + cos(radians) * direction.z

                    location.world.spawnParticle(Particle.REDSTONE, location, 0, x * particleSpeed, y * particleSpeed, z * particleSpeed, particleSize)
                    angle += angleStep
                }
            }
        }.runTaskTimer(soloLeveling, 0, 1)
    }

    private fun spawnGuidedArrows(shooter: Player, target: LivingEntity, count: Int) {
        for (i in 0 until count) {
            val arrow: Arrow = shooter.launchProjectile(Arrow::class.java)
            val direction: Vector = target.eyeLocation.subtract(arrow.location).toVector().normalize()

            // Spread the arrows using random offsets
            val random = Random()
            val spread = 0.5 // Adjust the spread factor to control the arrow dispersion
            val offsetX = (random.nextDouble() * 2 - 1) * spread
            val offsetY = (random.nextDouble() * 2 - 1) * spread
            val offsetZ = (random.nextDouble() * 2 - 1) * spread
            direction.add(Vector(offsetX, offsetY, offsetZ))

            arrow.velocity = Vector(0, 0, 0) // Set initial velocity to zero

            object : BukkitRunnable() {
                override fun run() {
                    if (!arrow.isOnGround && !arrow.isDead) {
                        arrow.velocity = direction // Launch the arrow towards the target after 1 second
                    } else {
                        arrow.remove()
                    }
                }
            }.runTaskLater(soloLeveling, 20L) // Wait 1 second (20 ticks) before launching the arrow

            object : BukkitRunnable() {
                override fun run() {
                    if (!arrow.isOnGround && !arrow.isDead) {
                        arrow.remove() // Remove the arrow after 10 seconds (adjustable)
                    }
                }
            }.runTaskLater(soloLeveling, 20L * 10)
        }
    }

    private fun createShockwave(center: Location, radius: Double) {
        val world: World = center.world

        // Spawn particles in a sphere around the center location
        object : BukkitRunnable() {
            val radiusSquared: Double = radius * radius
            val range = CustomRange(-radius.toInt(), radius.toInt())

            override fun run() {
                for (x in range) {
                    for (y in range) {
                        for (z in range) {
                            val loc: Location = center.clone().add(x.toDouble(), y.toDouble(), z.toDouble())
                            if (center.distanceSquared(loc) <= radiusSquared) {
                                world.spawnParticle(Particle.SONIC_BOOM, loc, 1)
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(soloLeveling, 0, 1) // Run the task every tick

        // Apply an upward force to nearby players
        for (player: Player in Bukkit.getOnlinePlayers()) {
            val playerLoc: Location = player.location
            val distance: Double = center.distance(playerLoc)
            val scoreboard = Bukkit.getScoreboardManager().mainScoreboard

            if (distance <= radius) {
                val direction: Vector = playerLoc.subtract(center).toVector().normalize()
                val force: Double = 1.5 * (1 - distance / radius)
                player.velocity = direction.multiply(force)
                player.damage(20.0)
                if (scoreboard.getTeam("marked")?.hasEntity(player) == true) {
                    player.damage(10.0)
                    player.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 10, 3))
                    scoreboard.getTeam("marked")?.removeEntity(player)
                }
                displayMark(player)
            }
        }
    }

    /**
     * This is the passive of [Archer] Class
     * **/
    private fun displayMark(entity: LivingEntity) {

        val center = entity.location
        val radius = 5.0
        val stepSize = 0.1
        var angle = 0.0
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        val team = scoreboard.getTeam("marked")!!

        team.addEntities(entity)
        entity.isGlowing = true

        object : BukkitRunnable() {
            override fun run() {

                while (angle <= Math.PI * 2) {
                    val x = center.x + radius * cos(angle)
                    val z = center.z + radius * sin(angle)

                    val particleLoc = Location(center.world, x, center.y, z)
                    center.world.spawnParticle(Particle.FLAME, particleLoc, 1)

                    angle += stepSize
                }

            }
        }.runTaskTimer(soloLeveling, 0, 20L)

        object : BukkitRunnable() {
            override fun run() {
                team.removeEntities(entity)
                entity.isGlowing = false
            }
        }.runTaskLater(soloLeveling, 5 * 20)
    }



    private fun spawnUltArrow(arrow: Arrow) {
        val location: Location = arrow.location
        val world: World = location.world
        val direction: Vector = arrow.velocity.normalize()
        val offset: Vector = direction.multiply(4)

        val magicArrow: Arrow = world.spawnArrow(location.add(offset), direction, 4F, 0F).apply {
            this.damage += 70
        }
        magicArrow.addCustomEffect(PotionEffect(PotionEffectType.HARM, 1, 1), true)
        magicArrow.customName(arrowName)
        createParticleTrail(arrow)
    }

    inner class CustomRange(private val start: Int, private val end: Int) : Iterable<Int> {
        override fun iterator(): Iterator<Int> {
            return object : Iterator<Int> {
                private var current = start

                override fun hasNext(): Boolean {
                    return current <= end
                }

                override fun next(): Int {
                    return current++
                }
            }
        }
    }

}



