package de.jakob.lotm.abilities.tyrant;

import de.jakob.lotm.util.BeyonderData;
import de.jakob.lotm.util.abilities.SelectableAbilityItem;
import de.jakob.lotm.util.helper.AbilityUtil;
import de.jakob.lotm.util.helper.ParticleUtil;
import de.jakob.lotm.util.helper.VectorUtil;
import de.jakob.lotm.util.scheduling.ServerScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class WaterManipulation extends SelectableAbilityItem {
    public WaterManipulation(Properties properties) {
        super(properties, .75f);
    }

    private final DustParticleOptions dustOptions = new DustParticleOptions(
            new Vector3f(30 / 255f, 120 / 255f, 255 / 255f),
            1.5f
    );
    private final DustParticleOptions dustOptions2 = new DustParticleOptions(
            new Vector3f(30 / 255f, 153 / 255f, 255 / 255f),
            2.5f
    );


    @Override
    public Map<String, Integer> getRequirements() {
        return new HashMap<>(Map.of(
                "tyrant", 7
        ));
    }

    @Override
    protected float getSpiritualityCost() {
        return 28;
    }

    @Override
    protected String[] getAbilityNames() {
        return new String[]{
                "ability.lotmcraft.water_manipulation.water_whip",
                "ability.lotmcraft.water_manipulation.aqueous_light",
                "ability.lotmcraft.water_manipulation.water_bolt",
                "ability.lotmcraft.water_manipulation.corrosive_rain",
                "ability.lotmcraft.water_manipulation.water_surge"
        };
    }

    @Override
    protected void useAbility(Level level, LivingEntity entity, int abilityIndex) {
        switch(abilityIndex) {
            case 0 -> waterWhip(level, entity);
            case 1 -> aqueousLight(level, entity);
            case 2 -> waterBolt(level, entity);
            case 3 -> corrosiveRain(level, entity);
            case 4 -> waterSurge(level, entity);
        }
    }

    private void waterSurge(Level level, LivingEntity entity) {

    }

    private void corrosiveRain(Level level, LivingEntity entity) {

    }

    private void waterBolt(Level level, LivingEntity entity) {
        if(level.isClientSide)
            return;

        Vec3 startPos = VectorUtil.getRelativePosition(entity.getEyePosition().add(entity.getLookAngle().normalize()), entity.getLookAngle().normalize(), 0, random.nextDouble(-.65, .65), random.nextDouble(-.1, .6));
        Vec3 direction = AbilityUtil.getTargetLocation(entity, 10, 1.4f).subtract(startPos).normalize();

        AtomicReference<Vec3> currentPos = new AtomicReference<>(startPos);

        level.playSound(null, startPos.x, startPos.y, startPos.z, SoundEvents.PLAYER_SPLASH, entity.getSoundSource(), 2.0f, 1.0f);

        AtomicBoolean hasHit = new AtomicBoolean(false);

        ServerScheduler.scheduleForDuration(0, 1, 20 * 40, () -> {
            if (hasHit.get()) {
                return;
            }

            Vec3 pos = currentPos.get();

            if(AbilityUtil.damageNearbyEntities((ServerLevel) level, entity, 2.5f, 12 * multiplier(entity), pos, true, false, true, 0)) {
                hasHit.set(true);
                return;
            }

            if(!level.getBlockState(BlockPos.containing(pos.x, pos.y, pos.z)).isAir()) {
                if(BeyonderData.isGriefingEnabled(entity)) {
                    pos = pos.subtract(direction);
                    level.setBlockAndUpdate(BlockPos.containing(pos.x, pos.y, pos.z), Blocks.WATER.defaultBlockState());
                }
                hasHit.set(true);
                return;
            }

            ParticleUtil.spawnParticles((ServerLevel) level, dustOptions, pos, 12, 0.4, 0.02);
            ParticleUtil.spawnParticles((ServerLevel) level, ParticleTypes.BUBBLE, pos, 8, 0.4, 0.02);

            currentPos.set(pos.add(direction));
        }, (ServerLevel) level);
    }

    final Vec3 eastFacing = new Vec3(1, 0, 0);
    final Vec3 southFacing = new Vec3(0, 0, 1);

    private void aqueousLight(Level level, LivingEntity entity) {
        BlockPos targetBlock = AbilityUtil.getTargetBlock(entity, 8);

        if (!level.isClientSide) {
            BlockState lightBlock = Blocks.LIGHT.defaultBlockState();
            level.setBlock(targetBlock, lightBlock, 3);

            ParticleUtil.spawnCircleParticlesForDuration((ServerLevel) level, ParticleTypes.END_ROD, targetBlock.getCenter(), .75, 20 * 20, 15, 10);
            ParticleUtil.spawnCircleParticlesForDuration((ServerLevel) level, ParticleTypes.END_ROD, targetBlock.getCenter(), southFacing, .75, 20 * 20, 15, 10);
            ParticleUtil.spawnCircleParticlesForDuration((ServerLevel) level, ParticleTypes.END_ROD, targetBlock.getCenter(), eastFacing, .75, 20 * 20, 15, 10);
            ParticleUtil.spawnParticlesForDuration((ServerLevel) level, dustOptions2, targetBlock.getCenter(), 20 * 20, 10, 3, .9);

            ServerScheduler.scheduleDelayed(20 * 20, () -> {
                if (level.getBlockState(targetBlock).is(Blocks.LIGHT)) {
                    level.setBlock(targetBlock, Blocks.AIR.defaultBlockState(), 3);
                }
            }, (ServerLevel) level);

        }
    }

    private void waterWhip(Level level, LivingEntity entity) {
    }
}
