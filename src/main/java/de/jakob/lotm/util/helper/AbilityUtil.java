package de.jakob.lotm.util.helper;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AbilityUtil {

    public static BlockPos getTargetBlock(LivingEntity entity, int radius) {
        Vec3 lookDirection = entity.getLookAngle().normalize();
        Vec3 playerPosition = entity.position().add(0, entity.getEyeHeight(), 0);

        Vec3 targetPosition = playerPosition;

        for(int i = 0; i < radius; i++) {
            targetPosition = playerPosition.add(lookDirection.scale(i));

            BlockState block = entity.level().getBlockState(BlockPos.containing(targetPosition));

            if (!block.isAir()) {
                targetPosition = playerPosition.add(lookDirection.scale(i - 1));
                break;
            }
        }

        return BlockPos.containing(targetPosition);
    }

    public static Vec3 getTargetLocation(LivingEntity entity, int radius, float entityDetectionRadius) {
        Vec3 lookDirection = entity.getLookAngle().normalize();
        Vec3 playerPosition = entity.position().add(0, entity.getEyeHeight(), 0);

        Vec3 targetPosition = playerPosition;

        for(int i = 0; i < radius; i++) {
            targetPosition = playerPosition.add(lookDirection.scale(i));

            // Check for entities at this position
            AABB detectionBox = new AABB(targetPosition.subtract(entityDetectionRadius, entityDetectionRadius, entityDetectionRadius),
                    targetPosition.add(entityDetectionRadius, entityDetectionRadius, entityDetectionRadius));

            List<Entity> nearbyEntities = entity.level().getEntities(entity, detectionBox).stream().filter(
                    e -> e instanceof LivingEntity && e != entity
            ).toList();
            if (!nearbyEntities.isEmpty()) {
                return nearbyEntities.get(0).getEyePosition();
            }

            // Check for blocks
            BlockState block = entity.level().getBlockState(BlockPos.containing(targetPosition));

            if (!block.isAir()) {
                targetPosition = playerPosition.add(lookDirection.scale(i - 1));
                break;
            }
        }

        return targetPosition;
    }

    public static boolean damageNearbyEntities(
            ServerLevel level,
            LivingEntity source,
            double radius,
            double damage,
            Vec3 center,
            boolean ignoreSource,
            boolean distanceFalloff,
            boolean ignoreCooldown,
            int cooldownTicks) {

        // Create detection box slightly larger than radius for efficiency
        AABB detectionBox = new AABB(
                center.subtract(radius, radius, radius),
                center.add(radius, radius, radius)
        );

        List<LivingEntity> nearbyEntities = source.level().getEntitiesOfClass(
                LivingEntity.class,
                detectionBox
        );

        boolean hitAnyEntity = false;
        double radiusSquared = radius * radius; // Avoid sqrt calculations

        for (LivingEntity entity : nearbyEntities) {
            if (ignoreSource && entity == source) continue;

            // Calculate actual distance for spherical damage
            double distanceSquared = entity.position().distanceToSqr(center);

            // Skip entities outside the actual radius
            if (distanceSquared > radiusSquared) continue;

            // Calculate damage based on distance if enabled
            float finalDamage = (float) damage;
            if (distanceFalloff) {
                double distance = Math.sqrt(distanceSquared);
                double falloffMultiplier = Math.max(0.1, 1.0 - (distance / radius));
                finalDamage *= falloffMultiplier;
            }

            // Apply damage with appropriate damage source
            if (ignoreCooldown || entity.invulnerableTime <= 0) {
                entity.hurt(source.damageSources().explosion(null, source), finalDamage);

                // Set custom invulnerability time if specified
                if (cooldownTicks >= 0) {
                    entity.invulnerableTime = cooldownTicks;
                }

                hitAnyEntity = true;
            }
        }

        return hitAnyEntity;
    }

    public static List<LivingEntity> getNearbyEntities(@Nullable LivingEntity exclude,
                                                       ServerLevel level,
                                                       Vec3 center,
                                                       double radius) {
        // Create detection box slightly larger than radius for efficiency
        AABB detectionBox = new AABB(
                center.subtract(radius, radius, radius),
                center.add(radius, radius, radius)
        );

        double radiusSquared = radius * radius;

        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(
                LivingEntity.class,
                detectionBox
        ).stream().filter(
                entity -> entity.position().distanceToSqr(center) <= radiusSquared
        ).filter(entity -> entity != exclude).toList();

        return nearbyEntities;
    }

    public static List<LivingEntity> getNearbyEntities(@Nullable LivingEntity exclude,
                                                       ClientLevel level,
                                                       Vec3 center,
                                                       double radius) {
        // Create detection box slightly larger than radius for efficiency
        AABB detectionBox = new AABB(
                center.subtract(radius, radius, radius),
                center.add(radius, radius, radius)
        );

        double radiusSquared = radius * radius;

        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(
                LivingEntity.class,
                detectionBox
        ).stream().filter(
                entity -> entity.position().distanceToSqr(center) <= radiusSquared
        ).filter(entity -> entity != exclude).toList();

        return nearbyEntities;
    }

    public static boolean damageNearbyEntities(
            ServerLevel level,
            LivingEntity source,
            double radius,
            double damage,
            Vec3 center,
            boolean ignoreSource,
            boolean distanceFalloff) {

        // Create detection box slightly larger than radius for efficiency
        AABB detectionBox = new AABB(
                center.subtract(radius, radius, radius),
                center.add(radius, radius, radius)
        );

        List<LivingEntity> nearbyEntities = source.level().getEntitiesOfClass(
                LivingEntity.class,
                detectionBox
        );

        boolean hitAnyEntity = false;
        double radiusSquared = radius * radius; // Avoid sqrt calculations

        for (LivingEntity entity : nearbyEntities) {
            if (ignoreSource && entity == source) continue;

            // Calculate actual distance for spherical damage
            double distanceSquared = entity.position().distanceToSqr(center);

            // Skip entities outside the actual radius
            if (distanceSquared > radiusSquared) continue;

            // Calculate damage based on distance if enabled
            float finalDamage = (float) damage;
            if (distanceFalloff) {
                double distance = Math.sqrt(distanceSquared);
                double falloffMultiplier = Math.max(0.1, 1.0 - (distance / radius));
                finalDamage *= falloffMultiplier;
            }

            // Apply damage with appropriate damage source
            entity.hurt(source.damageSources().mobAttack(source), finalDamage);
            hitAnyEntity = true;
        }

        return hitAnyEntity;
    }

    public static boolean damageNearbyEntities(
            ServerLevel level,
            LivingEntity source,
            double radius,
            double damage,
            Vec3 center,
            boolean ignoreSource,
            boolean distanceFalloff,
            boolean ignoreCooldown,
            int cooldownTicks,
            int fireTicks) {

        // Create detection box slightly larger than radius for efficiency
        AABB detectionBox = new AABB(
                center.subtract(radius, radius, radius),
                center.add(radius, radius, radius)
        );

        List<LivingEntity> nearbyEntities = source.level().getEntitiesOfClass(
                LivingEntity.class,
                detectionBox
        );

        boolean hitAnyEntity = false;
        double radiusSquared = radius * radius; // Avoid sqrt calculations

        for (LivingEntity entity : nearbyEntities) {
            if (ignoreSource && entity == source) continue;

            // Calculate actual distance for spherical damage
            double distanceSquared = entity.position().distanceToSqr(center);

            // Skip entities outside the actual radius
            if (distanceSquared > radiusSquared) continue;

            // Calculate damage based on distance if enabled
            float finalDamage = (float) damage;
            if (distanceFalloff) {
                double distance = Math.sqrt(distanceSquared);
                double falloffMultiplier = Math.max(0.1, 1.0 - (distance / radius));
                finalDamage *= falloffMultiplier;
            }

            // Apply damage with appropriate damage source
            if (ignoreCooldown || entity.invulnerableTime <= 0) {
                entity.hurt(source.damageSources().mobAttack(source), finalDamage);

                // Set custom invulnerability time if specified
                if (cooldownTicks >= 0) {
                    entity.invulnerableTime = cooldownTicks;
                }

                // Set entity on fire if fireTicks > 0
                if (fireTicks > 0) {
                    entity.setRemainingFireTicks(fireTicks);
                }

                hitAnyEntity = true;
            }
        }

        return hitAnyEntity;
    }

    public static boolean damageNearbyEntities(
            ServerLevel level,
            LivingEntity source,
            double radius,
            double damage,
            Vec3 center,
            boolean ignoreSource,
            boolean distanceFalloff,
            int fireTicks) {

        // Create detection box slightly larger than radius for efficiency
        AABB detectionBox = new AABB(
                center.subtract(radius, radius, radius),
                center.add(radius, radius, radius)
        );

        List<LivingEntity> nearbyEntities = source.level().getEntitiesOfClass(
                LivingEntity.class,
                detectionBox
        );

        boolean hitAnyEntity = false;
        double radiusSquared = radius * radius; // Avoid sqrt calculations

        for (LivingEntity entity : nearbyEntities) {
            if (ignoreSource && entity == source) continue;

            // Calculate actual distance for spherical damage
            double distanceSquared = entity.position().distanceToSqr(center);

            // Skip entities outside the actual radius
            if (distanceSquared > radiusSquared) continue;

            // Calculate damage based on distance if enabled
            float finalDamage = (float) damage;
            if (distanceFalloff) {
                double distance = Math.sqrt(distanceSquared);
                double falloffMultiplier = Math.max(0.1, 1.0 - (distance / radius));
                finalDamage *= falloffMultiplier;
            }

            // Apply damage with appropriate damage source
            entity.hurt(source.damageSources().mobAttack(source), finalDamage);

            // Set entity on fire if fireTicks > 0
            if (fireTicks > 0) {
                entity.setRemainingFireTicks(fireTicks);
            }

            hitAnyEntity = true;
        }

        return hitAnyEntity;
    }


    public static void addPotionEffectToNearbyEntities(ServerLevel level, LivingEntity entity, double radius, Vec3 pos, MobEffectInstance... mobEffectInstances) {
        List<LivingEntity> nearbyEntities = getNearbyEntities(entity, level, pos, radius);
        for (LivingEntity nearbyEntity : nearbyEntities) {
            if (!nearbyEntity.isAlive() || nearbyEntity.isInvulnerableTo(entity.damageSources().mobAttack(entity))) {
                continue;
            }
            for (MobEffectInstance effect : mobEffectInstances) {
                if (effect != null && !nearbyEntity.hasEffect(effect.getEffect())) {
                    nearbyEntity.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.isVisible(), effect.showIcon()));
                }
            }
        }
    }
}
