package de.jakob.lotm.effect;

import de.jakob.lotm.LOTMCraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, LOTMCraft.MOD_ID);

    public static final Holder<MobEffect> ASLEEP = MOB_EFFECTS.register("asleep",
            () -> new AsleepEffect(MobEffectCategory.HARMFUL, 0x2E2E5C)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(LOTMCraft.MOD_ID, "asleep"), -10f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)); // Dark blue color

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

}
