package de.jakob.lotm.util.abilities;

import de.jakob.lotm.util.BeyonderData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class SelectableAbilityItem extends AbilityItem{

    protected final HashMap<UUID, Integer> selectedAbilities = new HashMap<>();

    public SelectableAbilityItem(Properties properties, float cooldown) {
        super(properties, cooldown);
    }

    @Override
    public abstract Map<String, Integer> getRequirements();

    @Override
    protected abstract float getSpiritualityCost();

    protected abstract String[] getAbilityNames();

    @Override
    protected void onAbilityUse(Level level, LivingEntity entity) {
        if(!(entity instanceof Player)) {
            useAbility(level, entity, random.nextInt(getAbilityNames().length));
        }

        if(!selectedAbilities.containsKey(entity.getUUID())) {
            selectedAbilities.put(entity.getUUID(), 0);
        }

        int selectedAbility = selectedAbilities.get(entity.getUUID());
        useAbility(level, entity, selectedAbility);
    }

    public String getSelectedAbility(LivingEntity entity) {
        if(getAbilityNames().length == 0)
            return "";

        if(!selectedAbilities.containsKey(entity.getUUID())) {
            selectedAbilities.put(entity.getUUID(), 0);
        }

        int selectedAbility = selectedAbilities.get(entity.getUUID());
        return getAbilityNames()[selectedAbility];
    }

    protected abstract void useAbility(Level level, LivingEntity entity, int abilityIndex);

    public void switchAbility(LivingEntity entity) {
        if(getAbilityNames().length == 0)
            return;

        if(!selectedAbilities.containsKey(entity.getUUID())) {
            selectedAbilities.put(entity.getUUID(), 0);
        }

        int selectedAbility = selectedAbilities.get(entity.getUUID());
        selectedAbility++;
        if(selectedAbility >= getAbilityNames().length) {
            selectedAbility = 0;
        }
        selectedAbilities.put(entity.getUUID(), selectedAbility);
    }
}
