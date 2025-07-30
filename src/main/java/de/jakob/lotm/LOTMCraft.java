package de.jakob.lotm;

import com.mojang.logging.LogUtils;
import de.jakob.lotm.entity.ModEntities;
import de.jakob.lotm.entity.client.FlamingSpearProjectileRenderer;
import de.jakob.lotm.entity.client.PaperDaggerProjectileRenderer;
import de.jakob.lotm.gui.ModMenuTypes;
import de.jakob.lotm.gui.custom.AbilitySelectionScreen;
import de.jakob.lotm.item.ModCreativeModTabs;
import de.jakob.lotm.item.ModItems;
import de.jakob.lotm.network.PacketHandler;
import de.jakob.lotm.particle.*;
import de.jakob.lotm.util.BeyonderData;
import de.jakob.lotm.util.abilities.AbilityHandler;
import de.jakob.lotm.util.Config;
import de.jakob.lotm.util.abilities.PassiveAbilityHandler;
import de.jakob.lotm.util.scheduling.ClientScheduler;
import de.jakob.lotm.util.scheduling.ServerScheduler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(LOTMCraft.MOD_ID)
public class LOTMCraft
{
    public static final String MOD_ID = "lotmcraft";

    public static KeyMapping becomeBeyonderKey;
    public static KeyMapping switchBeyonderKey;
    public static KeyMapping getAbilitiesKey;
    public static KeyMapping clearBeyonderKey;
    public static KeyMapping openAbilitySelectionKey;
    public static KeyMapping toggleGriefingKey;
    public static KeyMapping showPassiveAbilitiesKey;
    public static KeyMapping switchAbilityKey;
    public static KeyMapping increaseSequenceKey;
    public static KeyMapping decreaseSequenceKey;


    public static final Logger LOGGER = LogUtils.getLogger();

    public LOTMCraft(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModParticles.register(modEventBus);
        ModEntities.register(modEventBus);

        AbilityHandler.registerAbilities(modEventBus);
        PassiveAbilityHandler.registerAbilities(modEventBus);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(PacketHandler::register);

        ServerScheduler.initialize();
        ClientScheduler.initialize();

        BeyonderData.initPathwayInfos();

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            EntityRenderers.register(ModEntities.FLAMING_SPEAR.get(), FlamingSpearProjectileRenderer::new);
            EntityRenderers.register(ModEntities.PAPER_DAGGER.get(), PaperDaggerProjectileRenderer::new);
        }

        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.HOLY_FLAME.get(), HolyFlameParticle.Provider::new);
            event.registerSpriteSet(ModParticles.DARKER_FLAME.get(), DarkerFlameParticle.Provider::new);
            event.registerSpriteSet(ModParticles.TOXIC_SMOKE.get(), ToxicSmokeParticle.Provider::new);
            event.registerSpriteSet(ModParticles.GREEN_FLAME.get(), GreenFlameParticle.Provider::new);
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.ABILITY_SELECTION_MENU.get(), AbilitySelectionScreen::new);
        }
    }

}
