package de.jakob.lotm.abilities;

import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.util.abilities.ToggleAbilityItem;
import de.jakob.lotm.util.helper.AbilityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.*;

public class SpiritVisionAbility extends ToggleAbilityItem {

    private static final HashMap<UUID, List<LivingEntity>> glowingEntities = new HashMap<>();

    public SpiritVisionAbility(Properties properties) {
        super(properties);

        canBeCopied = false;
        canBeUsedByNPC = false;
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
    protected void start(Level level, LivingEntity entity) {
        if(!level.isClientSide)
            return;
        entity.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1, 1);
    }


    @Override
    protected void tick(Level level, LivingEntity entity) {
        if(!level.isClientSide()) {
            if(!glowingEntities.containsKey(entity.getUUID())) {
                glowingEntities.put(entity.getUUID(), new ArrayList<>());
                LOTMCraft.LOGGER.debug("Created new glow list for entity: {}", entity.getUUID());
            }

            entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 12, 1, false, false, false));

            List<LivingEntity> nearbyEntities = AbilityUtil.getNearbyEntities(entity, (ServerLevel) level, entity.getEyePosition(), 30)
                    .stream()
                    .filter(e -> !glowingEntities.get(entity.getUUID()).contains(e))
                    .toList();

            for (LivingEntity nearbyEntity : nearbyEntities) {
                int color = getRandomColor();
            }
            return;
        }
    }

    @Override
    protected void stop(Level level, LivingEntity entity) {
        if(level.isClientSide) {
            entity.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1, 1);
            return;
        }

        LOTMCraft.LOGGER.debug("Stopping Spirit Vision for entity: {}", entity.getUUID());

        // Get the list of glowing entities for this specific entity
        List<LivingEntity> entityGlowList = glowingEntities.get(entity.getUUID());
        if (entityGlowList != null && entity instanceof ServerPlayer serverPlayer) {
            for (LivingEntity glowingEntity : entityGlowList) {
                if (glowingEntity.isAlive()) {
                }
            }
            // Clear the list for this entity
            glowingEntities.remove(entity.getUUID());
        }
    }

    private int getRandomColor() {
        Random random = new Random();
        int alpha = 0xFF; // Full opacity
        int red = random.nextInt(256);   // 0 to 255
        int green = random.nextInt(256); // 0 to 255
        int blue = random.nextInt(256);  // 0 to 255

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    @Override
    protected float getSpiritualityCost() {
        return 0;
    }

}
