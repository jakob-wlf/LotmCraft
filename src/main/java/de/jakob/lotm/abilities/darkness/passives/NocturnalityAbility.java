package de.jakob.lotm.abilities.darkness.passives;

import de.jakob.lotm.util.abilities.PassiveAbilityItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NocturnalityAbility extends PassiveAbilityItem {

    private final List<MobEffectInstance> modifiedEffects = new ArrayList<>();

    private int tick = 0;

    public NocturnalityAbility(Properties properties) {
        super(properties);
    }

    @Override
    public Map<String, Integer> getRequirements() {
        return new HashMap<>(Map.of("darkness", 9));
    }

    @Override
    public void tick(Level level, LivingEntity entity) {
        if(level.isClientSide)
            return;

        if(level.isDay())
            return;

        //TODO: Fix
        for(MobEffectInstance effect : entity.getActiveEffects()) {
            if(effect.getEffect().value().isBeneficial() && (effect.getEffect() != MobEffects.DAMAGE_RESISTANCE || effect.getAmplifier() < 3) && !modifiedEffects.contains(effect)) {
                MobEffectInstance effectInstance = new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier() + 1, effect.isAmbient(), effect.isVisible(), effect.showIcon());
                modifiedEffects.add(effectInstance);
                entity.addEffect(effectInstance);
            }
        }

        tick++;
        if(tick > 60) {
            modifiedEffects.removeIf(e -> e.getDuration() <= 0);
            tick = 0;
        }
    }
}
