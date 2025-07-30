package de.jakob.lotm.abilities.tyrant;

import de.jakob.lotm.util.abilities.AbilityItem;
import de.jakob.lotm.util.helper.AbilityUtil;
import de.jakob.lotm.util.helper.ParticleUtil;
import de.jakob.lotm.util.helper.VectorUtil;
import de.jakob.lotm.util.scheduling.ServerScheduler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class RagingBlowsAbility extends AbilityItem {
    public RagingBlowsAbility(Properties properties) {
        super(properties, 1.2f);
    }

    @Override
    public Map<String, Integer> getRequirements() {
        return new HashMap<>(Map.of("tyrant", 8));
    }

    @Override
    protected float getSpiritualityCost() {
        return 14;
    }

    @Override
    protected void onAbilityUse(Level level, LivingEntity entity) {
        if(!level.isClientSide) {
            ServerScheduler.scheduleForDuration(0, 6, 6 * 9, () -> {
                Vec3 pos = VectorUtil.getRelativePosition(entity.getEyePosition(), entity.getLookAngle().normalize(), random.nextDouble(1, 2), random.nextDouble(-1.5, 1.5), random.nextDouble(-.5, .5));
                ParticleUtil.spawnParticles((ServerLevel) level, ParticleTypes.POOF, pos, 2, 0, 0.125);
                ParticleUtil.spawnParticles((ServerLevel) level, ParticleTypes.CLOUD, pos, 5, 0, 0.125);
                ParticleUtil.spawnParticles((ServerLevel) level, ParticleTypes.CRIT, pos, 7, 0, 0.115);

                level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE.value(), entity.getSoundSource(), 1, 1);

                AbilityUtil.damageNearbyEntities((ServerLevel) level, entity, 2.75f, 5 * multiplier(entity), pos, true, false, true, 0);
            }, (ServerLevel) level);
        }
    }
}
