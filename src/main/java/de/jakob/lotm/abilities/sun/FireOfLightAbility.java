package de.jakob.lotm.abilities.sun;

import de.jakob.lotm.particle.ModParticles;
import de.jakob.lotm.util.abilities.AbilityItem;
import de.jakob.lotm.util.helper.AbilityUtil;
import de.jakob.lotm.util.helper.ParticleUtil;
import de.jakob.lotm.util.scheduling.ServerScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class FireOfLightAbility extends AbilityItem {
    public FireOfLightAbility(Properties properties) {
        super(properties, .75f);
    }

    @Override
    public Map<String, Integer> getRequirements() {
        Map<String, Integer> reqs = new HashMap<>();
        reqs.put("sun", 7);
        return reqs;
    }

    @Override
    protected float getSpiritualityCost() {
        return 23;
    }

    DustParticleOptions dustOptions = new DustParticleOptions(
            new Vector3f(255 / 255f, 180 / 255f, 66 / 255f),
            2f
    );

    @Override
    protected void onAbilityUse(Level level, LivingEntity entity) {
        if(level.isClientSide)
            return;

        Vec3 targetPos = AbilityUtil.getTargetLocation(entity, 10, 1.4f);
        level.playSound(null, targetPos.x, targetPos.y, targetPos.z, SoundEvents.BLAZE_SHOOT, entity.getSoundSource(), 2.0f, .5f);
        level.playSound(null, targetPos.x, targetPos.y, targetPos.z, SoundEvents.BEACON_ACTIVATE, entity.getSoundSource(), .4f, .5f);

        ParticleUtil.spawnParticles((ServerLevel) level, ParticleTypes.FLAME, targetPos, 11, .4, .035);
        ParticleUtil.spawnParticles((ServerLevel) level, ModParticles.HOLY_FLAME.get(), targetPos, 17, .4, .04);
        ParticleUtil.spawnParticles((ServerLevel) level, dustOptions, targetPos, 6, .75, 0);

        AbilityUtil.damageNearbyEntities((ServerLevel) level, entity, 2.5, 17.5 * multiplier(entity), targetPos, true, false, true, 0, 20 * 2);

         BlockState block = level.getBlockState(BlockPos.containing(targetPos));
         if(block.isAir()) {
             level.setBlockAndUpdate(BlockPos.containing(targetPos), Blocks.LIGHT.defaultBlockState());
         }

        ServerScheduler.scheduleDelayed(25, () -> level.setBlockAndUpdate(BlockPos.containing(targetPos), Blocks.AIR.defaultBlockState()));
    }
}
