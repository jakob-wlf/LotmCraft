package de.jakob.lotm.item;

import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.util.abilities.AbilityHandler;
import de.jakob.lotm.util.abilities.AbilityItem;
import de.jakob.lotm.util.abilities.PassiveAbilityHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LOTMCraft.MOD_ID);

    public static final Supplier<CreativeModeTab> LOTM_TAB = CREATIVE_MODE_TABS.register("lotm_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MOD_ICON.get()))
                    .title(Component.translatable("creativetab.lotmcraft.lotm_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.FOOL_Card.get());
                    })
                    .build());

    public static final Supplier<CreativeModeTab> ABILITIES_TAB = CREATIVE_MODE_TABS.register("abilities_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(AbilityHandler.COGITATION.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(LOTMCraft.MOD_ID, "lotm_tab"))
                    .title(Component.translatable("creativetab.lotmcraft.abilities_tab"))
                    .displayItems((parameters, output) -> {
                        AbilityHandler.ITEMS.getEntries().forEach(itemHolder -> {
                            output.accept(itemHolder.get());
                        });
                    })
                    .build());

    public static final Supplier<CreativeModeTab> PASSIVE_ABILITIES_TAB = CREATIVE_MODE_TABS.register("passive_abilities_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(PassiveAbilityHandler.PHYSICAL_ENHANCEMENTS_RED_PRIEST.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(LOTMCraft.MOD_ID, "abilities_tab"))
                    .title(Component.translatable("creativetab.lotmcraft.passive_abilities_tab"))
                    .displayItems((parameters, output) -> {
                        PassiveAbilityHandler.ITEMS.getEntries().forEach(itemHolder -> {
                            output.accept(itemHolder.get());
                        });
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
