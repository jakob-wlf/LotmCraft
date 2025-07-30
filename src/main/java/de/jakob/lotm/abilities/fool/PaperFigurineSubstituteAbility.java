package de.jakob.lotm.abilities.fool;

import de.jakob.lotm.util.abilities.AbilityItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class PaperFigurineSubstituteAbility extends AbilityItem {

    public PaperFigurineSubstituteAbility(Properties properties) {
        super(properties, .2f);
    }

    @Override
    public Map<String, Integer> getRequirements() {
        return new HashMap<>(Map.of("fool", 7));
    }

    @Override
    protected float getSpiritualityCost() {
        return 20;
    }

    @Override
    protected void onAbilityUse(Level level, LivingEntity entity) {

    }
}
