package de.jakob.lotm.util.events;

import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.util.abilities.ToggleAbilityItem;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = LOTMCraft.MOD_ID, value = Dist.DEDICATED_SERVER)
public class PlayerEvents {

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
//        if (event.getEntity() instanceof ServerPlayer player) {
//            ToggleAbilityItem.cleanupPlayer(player.getUUID());
//        }
    }
}