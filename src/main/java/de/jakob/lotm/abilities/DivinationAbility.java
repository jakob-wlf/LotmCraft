package de.jakob.lotm.abilities;

import de.jakob.lotm.util.abilities.SelectableAbilityItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class DivinationAbility extends SelectableAbilityItem {
    public DivinationAbility(Properties properties) {
        super(properties, 1);
    }

    @Override
    public Map<String, Integer> getRequirements() {
        return new HashMap<>(Map.of(
                "fool", 9,
                "door", 7,
                "hermit", 9,
                "demoness", 7
        ));
    }

    @Override
    protected float getSpiritualityCost() {
        return 10;
    }

    @Override
    protected String[] getAbilityNames() {
        return new String[] {"Dowsing Rod", "Danger Premonition", "Dream Divination"};
    }

    @Override
    protected void useAbility(Level level, LivingEntity entity, int abilityIndex) {

    }
}
