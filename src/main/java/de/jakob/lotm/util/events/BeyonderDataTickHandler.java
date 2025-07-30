package de.jakob.lotm.util.events;

import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.util.BeyonderData;
import de.jakob.lotm.util.abilities.PassiveAbilityHandler;
import de.jakob.lotm.util.abilities.PassiveAbilityItem;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = LOTMCraft.MOD_ID)
public class BeyonderDataTickHandler {

    private static long tickCounter = 0;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        // Only run on server side
        if (player.level().isClientSide) return;

        if (BeyonderData.isBeyonder(player)) {
            float amount = BeyonderData.getMaxSpirituality(BeyonderData.getSequence(player)) * 0.0006f;
            BeyonderData.incrementSpirituality(player, amount);

            // Passive abilities
            if(tickCounter % 5 == 0)
                tickPassiveAbilities(player);
        }

        tickCounter++;
        if(tickCounter > 2000000000)
            tickCounter = 0;
    }

    private static void tickPassiveAbilities(Player player) {
        PassiveAbilityHandler.ITEMS.getEntries().forEach(itemHolder -> {
            if (itemHolder.get() instanceof PassiveAbilityItem abilityItem) {
                if (abilityItem.shouldApplyTo(player)) {
                    abilityItem.tick(player.level(), player);
                }
            }
        });
    }
}