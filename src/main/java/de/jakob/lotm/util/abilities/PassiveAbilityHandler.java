package de.jakob.lotm.util.abilities;

import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.abilities.abyss.passives.PhysicalEnhancementsAbyssAbility;
import de.jakob.lotm.abilities.darkness.passives.NocturnalityAbility;
import de.jakob.lotm.abilities.darkness.passives.PhysicalEnhancementsDarknessAbility;
import de.jakob.lotm.abilities.demoness.passives.PhysicalEnhancementsDemonessAbility;
import de.jakob.lotm.abilities.fool.passives.PaperDaggersAbility;
import de.jakob.lotm.abilities.fool.passives.PhysicalEnhancementsFoolAbility;
import de.jakob.lotm.abilities.red_priest.passive.FireResistanceAbility;
import de.jakob.lotm.abilities.red_priest.passive.FlamingHitAbility;
import de.jakob.lotm.abilities.red_priest.passive.PhysicalEnhancementsRedPriestAbility;
import de.jakob.lotm.abilities.sun.passives.PhysicalEnhancementsSunAbility;
import de.jakob.lotm.abilities.tyrant.passives.PhysicalEnhancementsTyrantAbility;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PassiveAbilityHandler {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LOTMCraft.MOD_ID);

    public static final DeferredItem<Item> PHYSICAL_ENHANCEMENTS_RED_PRIEST = ITEMS.registerItem("physical_enhancements_red_priest_ability", PhysicalEnhancementsRedPriestAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> PHYSICAL_ENHANCEMENTS_SUN = ITEMS.registerItem("physical_enhancements_sun_ability", PhysicalEnhancementsSunAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> PHYSICAL_ENHANCEMENTS_TYRANT = ITEMS.registerItem("physical_enhancements_tyrant_ability", PhysicalEnhancementsTyrantAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> PHYSICAL_ENHANCEMENTS_DEMONESS = ITEMS.registerItem("physical_enhancements_demoness_ability", PhysicalEnhancementsDemonessAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> PHYSICAL_ENHANCEMENTS_ABYSS = ITEMS.registerItem("physical_enhancements_abyss_ability", PhysicalEnhancementsAbyssAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> PHYSICAL_ENHANCEMENTS_FOOL = ITEMS.registerItem("physical_enhancements_fool_ability", PhysicalEnhancementsFoolAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> PHYSICAL_ENHANCEMENTS_DARKNESS = ITEMS.registerItem("physical_enhancements_darkness_ability", PhysicalEnhancementsDarknessAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> FLAMING_HIT = ITEMS.registerItem("flaming_hit_ability", FlamingHitAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<Item> FIRE_RESISTANCE = ITEMS.registerItem("fire_resistance_ability", FireResistanceAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> PAPER_DAGGERS = ITEMS.registerItem("paper_dagger_ability", PaperDaggersAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    public static final DeferredItem<Item> NOCTURNALITY = ITEMS.registerItem("nocturnality_ability", NocturnalityAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));

    public static void registerAbilities(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
