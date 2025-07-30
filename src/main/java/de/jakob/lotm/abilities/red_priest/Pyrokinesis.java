package de.jakob.lotm.abilities.red_priest;

import de.jakob.lotm.entity.custom.FlamingSpearProjectileEntity;
import de.jakob.lotm.util.BeyonderData;
import de.jakob.lotm.util.abilities.SelectableAbilityItem;
import de.jakob.lotm.util.helper.AbilityUtil;
import de.jakob.lotm.util.helper.ParticleUtil;
import de.jakob.lotm.util.helper.VectorUtil;
import de.jakob.lotm.util.scheduling.ServerScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Pyrokinesis extends SelectableAbilityItem {

    public Pyrokinesis(Properties properties) {
        super(properties, .75f);
    }

    @Override
    public Map<String, Integer> getRequirements() {
        return new HashMap<>(Map.of(
                "red_priest", 7
        ));
    }

    @Override
    protected float getSpiritualityCost() {
        return 30;
    }

    @Override
    protected String[] getAbilityNames() {
        return new String[]{
                "ability.lotmcraft.pyrokinesis.fireball",
                "ability.lotmcraft.pyrokinesis.flame_wave",
                "ability.lotmcraft.pyrokinesis.wall_of_fire",
                "ability.lotmcraft.pyrokinesis.fire_ravens",
                "ability.lotmcraft.pyrokinesis.flaming_spear"
        };
    }

    @Override
    protected void useAbility(Level level, LivingEntity entity, int abilityIndex) {
        switch(abilityIndex) {
            case 0 -> fireball(level, entity);
            case 1 -> flameWave(level, entity);
            case 2 -> wallOfFire(level, entity);
            case 3 -> fireRavens(level, entity);
            case 4 -> flamingSpear(level, entity);
        }
    }

    private void flamingSpear(Level level, LivingEntity entity) {
        if(level.isClientSide)
            return;

        Vec3 startPos = VectorUtil.getRelativePosition(entity.getEyePosition().add(entity.getLookAngle().normalize()), entity.getLookAngle().normalize(), 0, random.nextDouble(1, 2.85f), random.nextDouble(-.1, .6));
        Vec3 direction = AbilityUtil.getTargetLocation(entity, 50, 1.4f).subtract(startPos).normalize();

        FlamingSpearProjectileEntity spear = new FlamingSpearProjectileEntity(level, entity, 18 * multiplier(entity), BeyonderData.isGriefingEnabled(entity));
        spear.setPos(startPos.x, startPos.y, startPos.z); // Set initial position
        spear.shoot(direction.x, direction.y, direction.z, 1.6f, 0);
        level.addFreshEntity(spear);

    }

    private void fireRavens(Level level, LivingEntity entity) {

    }

    private void wallOfFire(Level level, LivingEntity entity) {
        if(level.isClientSide)
            return;

        Vec3 targetPos = AbilityUtil.getTargetLocation(entity, 10, 1.4f);

        Vec3 perpendicular = VectorUtil.getPerpendicularVector(entity.getLookAngle()).normalize();

        ServerScheduler.scheduleForDuration(0, 4, 20 * 20, () -> {
            for(int i = -1; i < 6; i++) {
                for(int j = -7; j < 8; j++) {
                    Vec3 pos = targetPos.add(perpendicular.scale(j)).add(0, i, 0);

                    ParticleUtil.spawnParticles((ServerLevel) level, ParticleTypes.FLAME, pos, 1, 0.5, 0.02);
                    ParticleUtil.spawnParticles((ServerLevel) level, ParticleTypes.SMOKE, pos, 1, 0.5, 0.02);

                    AbilityUtil.damageNearbyEntities((ServerLevel) level, entity, 1f, 7 * multiplier(entity), pos, true, false, false, 15, 20 * 4);

                    for(LivingEntity target : AbilityUtil.getNearbyEntities(entity, (ServerLevel) level, pos, 1f)) {
                        Vec3 knockback = target.position().subtract(pos).normalize().add(0, .2, 0).scale(0.8f);
                        target.setDeltaMovement(knockback);
                    }
                }
            }
        }, (ServerLevel) level);
    }

    private void flameWave(Level level, LivingEntity entity) {

    }

    private void fireball(Level level, LivingEntity entity) {
        if(level.isClientSide)
            return;

        Vec3 startPos = VectorUtil.getRelativePosition(entity.getEyePosition().add(entity.getLookAngle().normalize()), entity.getLookAngle().normalize(), 0, random.nextDouble(-.65, .65), random.nextDouble(-.1, .6));
        Vec3 direction = AbilityUtil.getTargetLocation(entity, 10, 1.4f).subtract(startPos).normalize();

        AtomicReference<Vec3> currentPos = new AtomicReference<>(startPos);

        AtomicBoolean hasHit = new AtomicBoolean(false);

        level.playSound(null, startPos.x, startPos.y, startPos.z, SoundEvents.BLAZE_SHOOT, entity.getSoundSource(), 1.0f, 1.0f);

        ServerScheduler.scheduleForDuration(0, 1, 20 * 40, () -> {
            if(hasHit.get())
                return;

            Vec3 pos = currentPos.get();

            if(AbilityUtil.damageNearbyEntities((ServerLevel) level, entity, 2.5f, 15 * multiplier(entity), pos, true, false, true, 0, 20 * 5)) {
                hasHit.set(true);
                return;
            }

            if(!level.getBlockState(BlockPos.containing(pos.x, pos.y, pos.z)).isAir()) {
                if(BeyonderData.isGriefingEnabled(entity)) {
                    pos = pos.subtract(direction);
                    level.setBlockAndUpdate(BlockPos.containing(pos.x, pos.y, pos.z), Blocks.FIRE.defaultBlockState());
                }
                hasHit.set(true);
                return;
            }

            ParticleUtil.spawnParticles((ServerLevel) level, ParticleTypes.FLAME, pos, 12, 0.4, 0.02);
            ParticleUtil.spawnParticles((ServerLevel) level, ParticleTypes.SMOKE, pos, 8, 0.4, 0.02);

            currentPos.set(pos.add(direction));
        }, (ServerLevel) level);
    }
}
