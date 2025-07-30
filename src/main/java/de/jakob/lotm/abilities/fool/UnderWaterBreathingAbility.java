package de.jakob.lotm.abilities.fool;

import de.jakob.lotm.util.abilities.AbilityItem;
import de.jakob.lotm.util.abilities.ToggleAbilityItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class UnderWaterBreathingAbility extends ToggleAbilityItem {
    public UnderWaterBreathingAbility(Properties properties) {
        super(properties);
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

    @Override
    protected void tick(Level level, LivingEntity entity) {

    }

    @Override
    protected void stop(Level level, LivingEntity entity) {

    }
}
