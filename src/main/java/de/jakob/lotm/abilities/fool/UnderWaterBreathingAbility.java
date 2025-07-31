package de.jakob.lotm.abilities.fool;

import de.jakob.lotm.util.abilities.AbilityItem;
import de.jakob.lotm.util.abilities.ToggleAbilityItem;
import de.jakob.lotm.util.helper.ParticleUtil;
import de.jakob.lotm.util.helper.VectorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class UnderWaterBreathingAbility extends ToggleAbilityItem {
    public UnderWaterBreathingAbility(Properties properties) {
        super(properties);

        canBeUsedByNPC = false;
    }

    @Override
    protected float getSpiritualityCost() {
        return 2;
    }

    @Override
    public Map<String, Integer> getRequirements() {
        return new HashMap<>(Map.of("fool", 7));
    }

    @Override
    protected void start(Level level, LivingEntity entity) {

    }

    private final Vec3 direction = new Vec3(0, .5, 0);

    @Override
    protected void tick(Level level, LivingEntity entity) {
        if(level.getBlockState(BlockPos.containing(entity.position().x, entity.position().y, entity.position().z)).getBlock() != Blocks.WATER) {
            use(level, (Player) entity, InteractionHand.MAIN_HAND);
            return;
        }

        entity.addEffect(new MobEffectInstance(
                net.minecraft.world.effect.MobEffects.WATER_BREATHING,
                20 * 2,
                0,
                false,
                false,
                false
        ));

        Vec3 pos = VectorUtil.getRelativePosition(entity.getEyePosition(), entity.getLookAngle().normalize(), .75f, .8f, 0f);

        for(int i = 0; i < 40; i++) {
            ParticleUtil.spawnParticles((ServerLevel) level, ParticleTypes.BUBBLE, pos, 1, 0, 0);
            pos = pos.add(direction);
            if(level.getBlockState(BlockPos.containing(pos.x, pos.y, pos.z)).getBlock() != Blocks.WATER)
                break;
        }
    }

    @Override
    protected void stop(Level level, LivingEntity entity) {

    }
}
