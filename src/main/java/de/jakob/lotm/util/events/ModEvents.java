package de.jakob.lotm.util.events;

import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.entity.client.FlamingSpearProjectileModel;
import de.jakob.lotm.entity.client.PaperDaggerProjectileModel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = LOTMCraft.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FlamingSpearProjectileModel.LAYER_LOCATION, FlamingSpearProjectileModel::createBodyLayer);
        event.registerLayerDefinition(PaperDaggerProjectileModel.LAYER_LOCATION, PaperDaggerProjectileModel::createBodyLayer);
    }

}
