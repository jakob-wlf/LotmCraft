package de.jakob.lotm.abilities;

import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.util.BeyonderData;
import de.jakob.lotm.util.abilities.ToggleAbilityItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class CogitationAbility extends ToggleAbilityItem {

    public CogitationAbility(Item.Properties properties) {
        super(properties);

        canBeCopied = false;
        canBeUsedByNPC = false;
    }

    @Override
    public Map<String, Integer> getRequirements() {
        Map<String, Integer> reqs = new HashMap<>();
        for(String pathway : BeyonderData.pathways) {
            reqs.put(pathway, 9);
        }
        return reqs;
    }

    @Override
    protected void start(Level level, LivingEntity entity) {
    }

    @Override
    protected void tick(Level level, LivingEntity entity) {
        if(!(entity instanceof Player player))
            return;

        // Only perform server-side operations on the server
        if (!level.isClientSide) {
            BeyonderData.incrementSpirituality(player, BeyonderData.getMaxSpirituality(BeyonderData.getSequence(player)) / 335);

            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 2, 3, false, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 5, 1, false, false, false));

            // Send message to client from server
            Component message = Component.translatable("lotm.ability.cogitation.tick");
            player.displayClientMessage(message, true);
        }
    }

    @Override
    protected void stop(Level level, LivingEntity entity) {
    }

    @Override
    protected float getSpiritualityCost() {
        return 0;
    }

}
