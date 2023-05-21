package io.github.codemaker.solo.leveling.clazz

import io.github.codemaker.solo.leveling.SoloLeveling
import io.github.codemaker.solo.leveling.Utils
import io.github.codemaker.solo.leveling.damage.DamageSupport.calculateDamage
import io.github.codemaker.solo.leveling.damage.DamageSupport.getProtection
import io.github.codemaker.solo.leveling.framework.Class
import io.github.codemaker.solo.leveling.managers.ProfileManager
import io.github.monun.tap.math.toRadians
import io.github.monun.tap.protocol.PacketSupport
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class Berserker(private val soloLeveling: SoloLeveling) : Listener {
    private val profileManager: ProfileManager? = soloLeveling.profileManager
    private val dashSpeed = 2.0 // Adjust the dash speed as desired
    private val dashTargets: MutableMap<UUID, LivingEntity> = HashMap()
    private val swivelCD: MutableMap<UUID, Int> = HashMap()
    private val affectedBlocks = mutableListOf<Location>()

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        val player = event.player
        val profile = profileManager?.getProfile(player.uniqueId)
        val clickedBlock = event.clickedBlock
        val targetEntity: LivingEntity? = player.getTargetEntity(4, false) as LivingEntity?
        val rayTraceEntity : LivingEntity? = player.rayTraceEntities(4, false) as LivingEntity?

        if (profile?.clazz?.name == Class.BERSKER.toString()) {
            var damage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value!!
            val armor = targetEntity?.getAttribute(Attribute.GENERIC_ARMOR)?.value!!
            val armorTough = targetEntity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)?.value!!
            val protection = targetEntity.getProtection(Enchantment.PROTECTION_ENVIRONMENTAL).toDouble()

            val calculatedDamage = calculateDamage(damage, armor, armorTough, protection)

            if (Action.PHYSICAL.isLeftClick && player.isSneaking) {
                event.isCancelled = true


                for (i in 0..1) {
                    targetEntity.noDamageTicks = 0
                    targetEntity.damage(calculatedDamage.apply { damage += 5 }, player)

                    targetEntity.location.world.spawnParticle(Particle.REDSTONE, targetEntity.location, 50, 0.5, 0.5, 0.5, 0.1)
                    player.location.world.playSound(player, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1f, 1f )
                    playSwingParticles(player)
                    spawnSemiXShapedSweepParticles(player.location)
                    PacketSupport.entityAnimation(player.entityId, 0)
                    PacketSupport.entityAnimation(player.entityId, 3)
                }
            }

            if (Action.PHYSICAL.isRightClick && !player.isSneaking) { //dash
                event.isCancelled = true
                val direction: Vector = targetEntity.location.subtract(player.location).toVector().normalize()

                dashTargets[player.uniqueId] = targetEntity
                performDash(player, targetEntity.location)
                spawnConeParticles(player.location, direction, 45.0, 10, 0.5)
            }


            if (Action.PHYSICAL.isRightClick && player.isSneaking) {
                event.isCancelled = true

                if (swivelCD.containsKey(player.uniqueId)) {
                    Utils.msgPlayer(player, "&fCooldown of &e" + swivelCD[player.uniqueId] + "s for &aSwivel!")
                    return
                }

                for (entity in player.getNearbyEntities(4.0, 4.0, 4.0)) {
                    if (entity is LivingEntity) {
                        val clickedBlockLoc = clickedBlock?.location
                        if (clickedBlockLoc != null) {
                            createShockwave(clickedBlockLoc)
                            entity.damage(calculatedDamage.apply {
                                damage += 10
                            }, player)
                            entity.velocity = player.location.toVector().subtract(entity.location.toVector())
                        }
                    }
                }
                swivelCD[player.uniqueId] = 10
                object : BukkitRunnable() {
                    override fun run() {
                        val current = swivelCD[player.uniqueId]!!
                        if (current > 0) {
                            swivelCD[player.uniqueId] = current - 1
                        }
                        if (current == 0) {
                            swivelCD.remove(player.uniqueId)
                            cancel()
                        }
                    }
                }.runTaskTimer(soloLeveling, 0L, 20L)
            }


        }
    }

    private fun playSwingParticles(player: Player) {
        val particleType = Particle.SWEEP_ATTACK // Adjust the particle type as desired
        val particleCount = 30
        val particleOffset = 0.5

        // Play particles in a circular pattern around the player
        for (i in 0 until particleCount) {
            val angle = (i.toDouble() / particleCount) * 2.0 * Math.PI
            val offsetX = particleOffset * cos(angle)
            val offsetY = 1.0 // Adjust the Y offset as desired
            val offsetZ = particleOffset * sin(angle)

            player.world.spawnParticle(
                particleType,
                player.location.add(offsetX, offsetY, offsetZ),
                1,
                0.0, 0.0, 0.0,
                0.0
            )
            player.location.subtract(offsetX, offsetY, offsetZ)
        }
    }

    private fun spawnSemiXShapedSweepParticles(location: Location) {
        object : BukkitRunnable() {
            var phi = 0.0
            val angle = Math.toRadians(45.0) // Adjust the angle as needed

            override fun run() {
                phi += Math.PI / 8 // Controls the shape of the 'X'

                val x1 = cos(phi) * cos(angle)
                val y1 = sin(angle)
                val z1 = sin(phi) * cos(angle)

//                val x2 = -cos(phi) * cos(angle)
//                val y2 = sin(angle)
//                val z2 = -sin(phi) * cos(angle)

                val vector1 = Vector(x1, y1, z1).normalize()
//                val vector2 = Vector(x2, y2, z2).normalize()

                val particleLoc1 = location.clone().add(vector1)
//                val particleLoc2 = location.clone().add(vector2)

                location.world.spawnParticle(Particle.SWEEP_ATTACK, particleLoc1, 1)
//                location.world.spawnParticle(Particle.SWEEP_ATTACK, particleLoc2, 1)

                if (phi >= 2 * Math.PI) {
                    cancel()
                }
            }
        }.runTaskTimer(soloLeveling, 0, 1)
    }

    private fun performDash(player: Player, targetLocation: Location) {
        object : BukkitRunnable() {
            private val initialLocation = player.location
            private val direction: Vector = targetLocation.subtract(initialLocation).toVector().normalize()
            private var distance = 0.0

            override fun run() {
                distance += dashSpeed

                if (distance >= initialLocation.distance(targetLocation)) {
                    player.teleport(targetLocation)
                    dashTargets.remove(player.uniqueId)
                    cancel()
                    return
                }

                val velocity = direction.clone().multiply(dashSpeed)
                player.velocity = velocity
            }
        }.runTaskTimer(soloLeveling, 0L, 1L)
    }

    private fun spawnConeParticles(location: Location, direction: Vector, angle: Double, numParticles: Int, particleOffset: Double) {
        for (i in 0 until numParticles) {
            val currentAngle: Double = (i * 360 / numParticles).toDouble() * Math.PI / 180
            val offset: Vector = Vector(cos(currentAngle), sin(currentAngle), 0.0).multiply(particleOffset)
            val rotatedDirection: Vector = rotateVectorAroundAxisZ(direction, angle).multiply(-1)
            val particleLocation: Vector = location.toVector().add(offset).add(rotatedDirection)
            location.world?.spawnParticle(Particle.SWEEP_ATTACK, particleLocation.x, particleLocation.y, particleLocation.z, 1)
        }
    }

    // Rotates a vector around the Z axis
    private fun rotateVectorAroundAxisZ(vector: Vector, angle: Double): Vector {
        val rad: Double = angle.toRadians()
        val cos: Double = cos(rad)
        val sin: Double = sin(rad)
        val x: Double = vector.x * cos - vector.y * sin
        val y: Double = vector.x * sin + vector.y * cos
        return Vector(x, y, vector.z)
    }

    private fun createShockwave(location: Location) {
        val world: World? = location.world
        val radius = 5.0

        // Store the initial state of the affected blocks
        for (x in -5..5) {
            for (y in -5..5) {
                for (z in -5..5) {
                    val currentLocation = location.clone().add(x.toDouble(), y.toDouble(), z.toDouble())
                    if (currentLocation.distance(location) <= radius) {
                        val block: Block = world?.getBlockAt(currentLocation) ?: continue
                        if (block.type != Material.AIR) {
                            affectedBlocks.add(currentLocation.clone())
                        }
                    }
                }
            }
        }

        // Start the animation task
        object : BukkitRunnable() {
            var t = 0.0
            val duration = 20.0 // Adjust the duration as desired

            override fun run() {
                t += 0.1

                // Animate the blocks rising and falling
                for (blockLoc in affectedBlocks) {
                    val x: Double = blockLoc.x
                    val y: Double = blockLoc.y
                    val z: Double = blockLoc.z

                    val newY = y + sin(t) * 2

                    val newLoc = Location(world, x, newY, z)
                    val block: Block = newLoc.block

                    if (newY > y) {
                        block.type = Material.AIR
                    } else {
                        block.type = Material.STONE // Adjust the material as desired
                    }
                }

                if (t >= duration) {
                    // Regenerate the blocks
                    for (blockLoc in affectedBlocks) {
                        val block: Block = blockLoc.block
                        block.type = Material.STONE // Adjust the material as desired
                    }

                    affectedBlocks.clear()
                    cancel()
                }
            }
        }.runTaskTimer(soloLeveling, 0, 1)
    }

}