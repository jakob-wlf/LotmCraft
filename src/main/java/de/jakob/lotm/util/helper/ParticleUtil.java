package de.jakob.lotm.util.helper;

import de.jakob.lotm.util.scheduling.ClientScheduler;
import de.jakob.lotm.util.scheduling.ServerScheduler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class ParticleUtil {

    /**
     * Helper method to spawn particles at a location for a duration
     *
     * @param particleType      Type of particle to spawn
     * @param position          Position to spawn particles
     * @param duration          Duration in ticks
     * @param interval          Interval between particle spawns
     * @param particlesPerSpawn Number of particles to spawn each time
     * @param spread            Spread radius for particles
     */
    public static void spawnParticlesForDuration(ServerLevel level, ParticleOptions particleType, Vec3 position,
                                                 int duration, int interval, int particlesPerSpawn,
                                                 double spread) {
        if (level == null) return;

        RandomSource random = level.random;

        ServerScheduler.scheduleForDuration(0, interval, duration, () -> {
            for (int i = 0; i < particlesPerSpawn; i++) {
                double offsetX = (random.nextDouble() - 0.5) * spread;
                double offsetY = (random.nextDouble() - 0.5) * spread;
                double offsetZ = (random.nextDouble() - 0.5) * spread;

                level.sendParticles(particleType,
                        position.x,
                        position.y,
                        position.z,
                        particlesPerSpawn, offsetX, offsetY, offsetZ, 0);
            }
        }, level);
    }

    public static void drawParticleLine(ServerLevel level, ParticleOptions particleType, Vec3 start, Vec3 end, double step, int particleCount) {
        if (level == null) return;

        Vec3 direction = end.subtract(start).normalize();
        double distance = start.distanceTo(end);
        int steps = (int) Math.ceil(distance / step);

        for (int i = 0; i < steps; i++) {
            double t = (double) i / (steps - 1);
            Vec3 position = start.add(direction.scale(t * distance));

            level.sendParticles(particleType, position.x, position.y, position.z, particleCount, 0, 0, 0, 0);
        }
    }

    public static void drawParticleLine(ServerLevel level, ParticleOptions particleType, Vec3 start, Vec3 end, double step, int particleCount, double offset) {
        if (level == null) return;

        Vec3 direction = end.subtract(start).normalize();
        double distance = start.distanceTo(end);
        int steps = (int) Math.ceil(distance / step);

        for (int i = 0; i < steps; i++) {
            double t = (double) i / (steps - 1);
            Vec3 position = start.add(direction.scale(t * distance));

            level.sendParticles(particleType, position.x, position.y, position.z, particleCount, offset, offset, offset, 0);
        }
    }

    public static void drawParticleLine(ServerLevel level, ParticleOptions particleType, Vec3 start, Vec3 direction, double length, double step, int particleCount) {
        if (level == null) return;

        Vec3 end = start.add(direction.normalize().scale(length));
        drawParticleLine(level, particleType, start, end, step, particleCount);
    }

    public static void drawParticleLine(ServerLevel level, ParticleOptions particleType, Vec3 start, Vec3 direction, double length, double step, int particleCount, double offset) {
        if (level == null) return;

        Vec3 end = start.add(direction.normalize().scale(length));
        drawParticleLine(level, particleType, start, end, step, particleCount, offset);
    }


    /**
     * Helper method to spawn particles in a circle pattern
     *
     * @param particleType  Type of particle to spawn
     * @param center        Center position
     * @param radius        Circle radius
     * @param duration      Duration in ticks
     * @param interval      Interval between particle spawns
     * @param particleCount Number of particles in circle
     */
    public static void spawnCircleParticlesForDuration(ServerLevel level, ParticleOptions particleType, Vec3 center,
                                                       double radius, int duration, int interval,
                                                       int particleCount) {
        if (level == null) return;

        ServerScheduler.scheduleForDuration(0, interval, duration, () -> {
            for (int i = 0; i < particleCount; i++) {
                double angle = (2 * Math.PI * i) / particleCount;
                double x = center.x + radius * Math.cos(angle);
                double z = center.z + radius * Math.sin(angle);

                level.sendParticles(particleType, x, center.y, z, 1, 0, 0, 0, 0);
            }
        }, level);
    }

    /**
     * Helper method to spawn particles in a circle pattern
     *
     * @param particleType  Type of particle to spawn
     * @param center        Center position
     * @param direction     Direction vector (normal to the circle plane)
     * @param radius        Circle radius
     * @param particleCount Number of particles in circle
     */
    public static void spawnCircleParticles(ServerLevel level, ParticleOptions particleType, Vec3 center, Vec3 direction,
                                                       double radius, int particleCount) {
        if (level == null) return;

        // Normalize the direction vector
        Vec3 normal = direction.normalize();

        // Create two perpendicular vectors in the plane of the circle
        Vec3 tangent1, tangent2;

        // Find a vector that's not parallel to the normal
        if (Math.abs(normal.x) < 0.9) {
            tangent1 = new Vec3(1, 0, 0).cross(normal).normalize();
        } else {
            tangent1 = new Vec3(0, 1, 0).cross(normal).normalize();
        }

        // Get the second tangent vector perpendicular to both normal and tangent1
        tangent2 = normal.cross(tangent1).normalize();

        for (int i = 0; i < particleCount; i++) {
            double angle = (2 * Math.PI * i) / particleCount;
            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);

            // Calculate position on the circle in 3D space
            Vec3 offset = tangent1.scale(radius * cosAngle).add(tangent2.scale(radius * sinAngle));
            Vec3 particlePos = center.add(offset);

            level.sendParticles(particleType, particlePos.x, particlePos.y, particlePos.z, 1, 0, 0, 0, 0);
        }
    }

    /**
     * Helper method to spawn particles at a location for a duration
     *
     * @param particleType      Type of particle to spawn
     * @param position          Position to spawn particles
     * @param particlesPerSpawn Number of particles to spawn each time
     * @param spread            Spread radius for particles
     */
    public static void spawnParticles(ServerLevel level, ParticleOptions particleType, Vec3 position,
                                                 int particlesPerSpawn, double spread) {
        if (level == null) return;

        RandomSource random = level.random;

        for (int i = 0; i < particlesPerSpawn; i++) {
            double offsetX = (random.nextDouble() - 0.5) * spread;
            double offsetY = (random.nextDouble() - 0.5) * spread;
            double offsetZ = (random.nextDouble() - 0.5) * spread;

            level.sendParticles(particleType,
                    position.x,
                    position.y,
                    position.z,
                    particlesPerSpawn, offsetX, offsetY, offsetZ, 0);
        }
    }

    /**
     * Helper method to spawn particles at a location for a duration
     *
     * @param particleType      Type of particle to spawn
     * @param position          Position to spawn particles
     * @param particlesPerSpawn Number of particles to spawn each time
     * @param spread            Spread radius for particles
     */
    public static void spawnParticles(ServerLevel level, ParticleOptions particleType, Vec3 position,
                                      int particlesPerSpawn, double spread, double speed) {
        if (level == null) return;

        RandomSource random = level.random;

        for (int i = 0; i < particlesPerSpawn; i++) {
            double offsetX = (random.nextDouble() - 0.5) * spread;
            double offsetY = (random.nextDouble() - 0.5) * spread;
            double offsetZ = (random.nextDouble() - 0.5) * spread;

            level.sendParticles(particleType,
                    position.x,
                    position.y,
                    position.z,
                    particlesPerSpawn, offsetX, offsetY, offsetZ, speed);
        }
    }

    /**
     * Helper method to spawn particles in a circle pattern
     *
     * @param particleType  Type of particle to spawn
     * @param center        Center position
     * @param radius        Circle radius
     * @param particleCount Number of particles in circle
     */
    public static void spawnCircleParticles(ServerLevel level, ParticleOptions particleType, Vec3 center,
                                                       double radius, int particleCount) {
        if (level == null) return;

        for (int i = 0; i < particleCount; i++) {
            double angle = (2 * Math.PI * i) / particleCount;
            double x = center.x + radius * Math.cos(angle);
            double z = center.z + radius * Math.sin(angle);

            level.sendParticles(particleType, x, center.y, z,0, 0, 0, 0, 0);
        }
    }

    /**
     * Helper method to spawn particles in a circle pattern
     *
     * @param particleType  Type of particle to spawn
     * @param center        Center position
     * @param direction     Direction vector (normal to the circle plane)
     * @param radius        Circle radius
     * @param duration      Duration in ticks
     * @param interval      Interval between particle spawns
     * @param particleCount Number of particles in circle
     */
    public static void spawnCircleParticlesForDuration(ServerLevel level, ParticleOptions particleType, Vec3 center, Vec3 direction,
                                                       double radius, int duration, int interval,
                                                       int particleCount) {
        if (level == null) return;

        // Normalize the direction vector
        Vec3 normal = direction.normalize();

        // Create two perpendicular vectors in the plane of the circle
        Vec3 tangent1, tangent2;

        // Find a vector that's not parallel to the normal
        if (Math.abs(normal.x) < 0.9) {
            tangent1 = new Vec3(1, 0, 0).cross(normal).normalize();
        } else {
            tangent1 = new Vec3(0, 1, 0).cross(normal).normalize();
        }

        // Get the second tangent vector perpendicular to both normal and tangent1
        tangent2 = normal.cross(tangent1).normalize();

        ServerScheduler.scheduleForDuration(0, interval, duration, () -> {
            for (int i = 0; i < particleCount; i++) {
                double angle = (2 * Math.PI * i) / particleCount;
                double cosAngle = Math.cos(angle);
                double sinAngle = Math.sin(angle);

                // Calculate position on the circle in 3D space
                Vec3 offset = tangent1.scale(radius * cosAngle).add(tangent2.scale(radius * sinAngle));
                Vec3 particlePos = center.add(offset);

                level.sendParticles(particleType, particlePos.x, particlePos.y, particlePos.z, particleCount, 0, 0, 0, 0);
            }
        }, level);
    }

    /**
     * Helper method to spawn particles at a location for a duration
     *
     * @param particleType      Type of particle to spawn
     * @param position          Position to spawn particles
     * @param duration          Duration in ticks
     * @param interval          Interval between particle spawns
     * @param particlesPerSpawn Number of particles to spawn each time
     * @param spread            Spread radius for particles
     */
    public static void spawnParticlesForDuration(ClientLevel level, ParticleOptions particleType, Vec3 position,
                                                 int duration, int interval, int particlesPerSpawn,
                                                 double spread) {
        if (level == null) return;

        RandomSource random = level.random;

        ClientScheduler.scheduleForDuration(0, interval, duration, () -> {
            for (int i = 0; i < particlesPerSpawn; i++) {
                double offsetX = (random.nextDouble() - 0.5) * spread;
                double offsetY = (random.nextDouble() - 0.5) * spread;
                double offsetZ = (random.nextDouble() - 0.5) * spread;

                level.addParticle(particleType,
                        position.x + offsetX,
                        position.y + offsetY,
                        position.z + offsetZ,
                        0, 0, 0);
            }
        });
    }

    /**
     * Helper method to spawn particles in a circle pattern
     *
     * @param particleType  Type of particle to spawn
     * @param center        Center position
     * @param radius        Circle radius
     * @param duration      Duration in ticks
     * @param interval      Interval between particle spawns
     * @param particleCount Number of particles in circle
     */
    public static void spawnCircleParticlesForDuration(ClientLevel level, ParticleOptions particleType, Vec3 center,
                                                       double radius, int duration, int interval,
                                                       int particleCount) {
        if (level == null) return;

        ClientScheduler.scheduleForDuration(0, interval, duration, () -> {
            for (int i = 0; i < particleCount; i++) {
                double angle = (2 * Math.PI * i) / particleCount;
                double x = center.x + radius * Math.cos(angle);
                double z = center.z + radius * Math.sin(angle);

                level.addParticle(particleType, x, center.y, z, 0, 0, 0);
            }
        });
    }

    /**
     * Helper method to spawn particles in a circle pattern
     *
     * @param particleType  Type of particle to spawn
     * @param center        Center position
     * @param direction     Direction vector (normal to the circle plane)
     * @param radius        Circle radius
     * @param particleCount Number of particles in circle
     */
    public static void spawnCircleParticles(ClientLevel level, ParticleOptions particleType, Vec3 center, Vec3 direction,
                                            double radius, int particleCount) {
        if (level == null) return;

        // Normalize the direction vector
        Vec3 normal = direction.normalize();

        // Create two perpendicular vectors in the plane of the circle
        Vec3 tangent1, tangent2;

        // Find a vector that's not parallel to the normal
        if (Math.abs(normal.x) < 0.9) {
            tangent1 = new Vec3(1, 0, 0).cross(normal).normalize();
        } else {
            tangent1 = new Vec3(0, 1, 0).cross(normal).normalize();
        }

        // Get the second tangent vector perpendicular to both normal and tangent1
        tangent2 = normal.cross(tangent1).normalize();

        for (int i = 0; i < particleCount; i++) {
            double angle = (2 * Math.PI * i) / particleCount;
            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);

            // Calculate position on the circle in 3D space
            Vec3 offset = tangent1.scale(radius * cosAngle).add(tangent2.scale(radius * sinAngle));
            Vec3 particlePos = center.add(offset);

            level.addParticle(particleType, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
        }
    }

    /**
     * Helper method to spawn particles at a location
     *
     * @param particleType      Type of particle to spawn
     * @param position          Position to spawn particles
     * @param particlesPerSpawn Number of particles to spawn each time
     * @param spread            Spread radius for particles
     */
    public static void spawnParticles(ClientLevel level, ParticleOptions particleType, Vec3 position,
                                      int particlesPerSpawn, double spread) {
        if (level == null) return;

        RandomSource random = level.random;

        for (int i = 0; i < particlesPerSpawn; i++) {
            double offsetX = (random.nextDouble() - 0.5) * spread;
            double offsetY = (random.nextDouble() - 0.5) * spread;
            double offsetZ = (random.nextDouble() - 0.5) * spread;

            level.addParticle(particleType,
                    position.x + offsetX,
                    position.y + offsetY,
                    position.z + offsetZ,
                    0, 0, 0);
        }
    }

    /**
     * Helper method to spawn particles at a location with velocity
     *
     * @param particleType      Type of particle to spawn
     * @param position          Position to spawn particles
     * @param particlesPerSpawn Number of particles to spawn each time
     * @param spread            Spread radius for particles
     * @param speed             Initial velocity for particles
     */
    public static void spawnParticles(ClientLevel level, ParticleOptions particleType, Vec3 position,
                                      int particlesPerSpawn, double spread, double speed) {
        if (level == null) return;

        RandomSource random = level.random;

        for (int i = 0; i < particlesPerSpawn; i++) {
            double offsetX = (random.nextDouble() - 0.5) * spread;
            double offsetY = (random.nextDouble() - 0.5) * spread;
            double offsetZ = (random.nextDouble() - 0.5) * spread;

            double velocityX = (random.nextDouble() - 0.5) * speed;
            double velocityY = (random.nextDouble() - 0.5) * speed;
            double velocityZ = (random.nextDouble() - 0.5) * speed;

            level.addParticle(particleType,
                    position.x + offsetX,
                    position.y + offsetY,
                    position.z + offsetZ,
                    velocityX, velocityY, velocityZ);
        }
    }

    /**
     * Helper method to spawn particles in a circle pattern
     *
     * @param particleType  Type of particle to spawn
     * @param center        Center position
     * @param radius        Circle radius
     * @param particleCount Number of particles in circle
     */
    public static void spawnCircleParticles(ClientLevel level, ParticleOptions particleType, Vec3 center,
                                            double radius, int particleCount) {
        if (level == null) return;

        for (int i = 0; i < particleCount; i++) {
            double angle = (2 * Math.PI * i) / particleCount;
            double x = center.x + radius * Math.cos(angle);
            double z = center.z + radius * Math.sin(angle);

            level.addParticle(particleType, x, center.y, z, 0, 0, 0);
        }
    }

    /**
     * Helper method to spawn particles in a circle pattern for a duration
     *
     * @param particleType  Type of particle to spawn
     * @param center        Center position
     * @param direction     Direction vector (normal to the circle plane)
     * @param radius        Circle radius
     * @param duration      Duration in ticks
     * @param interval      Interval between particle spawns
     * @param particleCount Number of particles in circle
     */
    public static void spawnCircleParticlesForDuration(ClientLevel level, ParticleOptions particleType, Vec3 center, Vec3 direction,
                                                       double radius, int duration, int interval,
                                                       int particleCount) {
        if (level == null) return;

        // Normalize the direction vector
        Vec3 normal = direction.normalize();

        // Create two perpendicular vectors in the plane of the circle
        Vec3 tangent1, tangent2;

        // Find a vector that's not parallel to the normal
        if (Math.abs(normal.x) < 0.9) {
            tangent1 = new Vec3(1, 0, 0).cross(normal).normalize();
        } else {
            tangent1 = new Vec3(0, 1, 0).cross(normal).normalize();
        }

        // Get the second tangent vector perpendicular to both normal and tangent1
        tangent2 = normal.cross(tangent1).normalize();

        ClientScheduler.scheduleForDuration(0, interval, duration, () -> {
            for (int i = 0; i < particleCount; i++) {
                double angle = (2 * Math.PI * i) / particleCount;
                double cosAngle = Math.cos(angle);
                double sinAngle = Math.sin(angle);

                // Calculate position on the circle in 3D space
                Vec3 offset = tangent1.scale(radius * cosAngle).add(tangent2.scale(radius * sinAngle));
                Vec3 particlePos = center.add(offset);

                level.addParticle(particleType, particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
            }
        });
    }

}
