package de.jakob.lotm.util.abilities;

import de.jakob.lotm.abilities.CogitationAbility;
import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.abilities.DivinationAbility;
import de.jakob.lotm.abilities.SpiritVisionAbility;
import de.jakob.lotm.abilities.abyss.PoisonousFlameAbility;
import de.jakob.lotm.abilities.abyss.ToxicSmokeAbility;
import de.jakob.lotm.abilities.darkness.MidnightPoemAbility;
import de.jakob.lotm.abilities.fool.*;
import de.jakob.lotm.abilities.red_priest.ProvokingAbility;
import de.jakob.lotm.abilities.red_priest.Pyrokinesis;
import de.jakob.lotm.abilities.red_priest.TrapAbility;
import de.jakob.lotm.abilities.tyrant.WaterManipulation;
import de.jakob.lotm.abilities.sun.*;
import de.jakob.lotm.abilities.tyrant.RagingBlowsAbility;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AbilityHandler {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LOTMCraft.MOD_ID);
    public static final DeferredItem<Item> COGITATION = ITEMS.registerItem("cogitation_ability", CogitationAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static final DeferredItem<Item> DIVINATION = ITEMS.registerItem("divination_ability", DivinationAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static final DeferredItem<Item> SPIRIT_VISION = ITEMS.registerItem("spirit_vision_ability", SpiritVisionAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static final DeferredItem<Item> HOLY_SONG = ITEMS.registerItem("holy_song_ability", HolySongAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static final DeferredItem<Item> ILLUMINATE = ITEMS.registerItem("illuminate_ability", IlluminateAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static DeferredItem<Item> HOLY_LIGHT = ITEMS.registerItem("holy_light_ability",  HolyLightAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static DeferredItem<Item> FIRE_OF_LIGHT = ITEMS.registerItem("fire_of_light_ability",  FireOfLightAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static DeferredItem<Item> HOLY_LIGHT_SUMMONING = ITEMS.registerItem("holy_light_summoning_ability",  HolyLightSummoningAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static DeferredItem<Item> CLEAVE_OF_PURIFICATION = ITEMS.registerItem("cleave_of_purification_ability",  CleaveOfPurificationAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));


    public static DeferredItem<Item> TRAP = ITEMS.registerItem("trap_ability", TrapAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static DeferredItem<Item> PROVOKING = ITEMS.registerItem("provoking_ability", ProvokingAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static DeferredItem<Item> PYROKINESIS = ITEMS.registerItem("pyrokinesis_ability", Pyrokinesis::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static DeferredItem<Item> RAGING_BLOWS = ITEMS.registerItem("raging_blows_ability", RagingBlowsAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static DeferredItem<Item> WATER_MANIPULATION = ITEMS.registerItem("water_manipulation_ability", WaterManipulation::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static DeferredItem<Item> TOXIC_SMOKE = ITEMS.registerItem("toxic_smoke_ability", ToxicSmokeAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static DeferredItem<Item> POISONOUS_FLAME = ITEMS.registerItem("poisonous_flame_ability", PoisonousFlameAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static DeferredItem<Item> AIR_BULLET = ITEMS.registerItem("air_bullet_ability", AirBulletAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static DeferredItem<Item> FLAME_CONTROLLING = ITEMS.registerItem("flame_controlling_ability", FlameControllingAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static DeferredItem<Item> PAPER_FIGURINE_SUBSTITUTE = ITEMS.registerItem("paper_figurine_substitute_ability", PaperFigurineSubstituteAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static DeferredItem<Item> FLAMING_JUMP = ITEMS.registerItem("flaming_jump_ability", FlamingJumpAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static DeferredItem<Item> UNDERWATER_BREATHING = ITEMS.registerItem("underwater_breathing_ability", UnderWaterBreathingAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static DeferredItem<Item> MIDNIGHT_POEM = ITEMS.registerItem("midnight_poem_ability", MidnightPoemAbility::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    public static void registerAbilities(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
