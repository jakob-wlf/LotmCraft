package de.jakob.lotm.entity;

import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.entity.custom.FlamingSpearProjectileEntity;
import de.jakob.lotm.entity.custom.PaperDaggerProjectileEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, LOTMCraft.MOD_ID);

    public static Supplier<EntityType<FlamingSpearProjectileEntity>> FLAMING_SPEAR =
            ENTITY_TYPES.register("flaming_spear", () -> EntityType.Builder.<FlamingSpearProjectileEntity>of(FlamingSpearProjectileEntity::new, MobCategory.MISC)
                    .sized(.35f, .35f).build("flaming_spear"));

    public static Supplier<EntityType<PaperDaggerProjectileEntity>> PAPER_DAGGER =
            ENTITY_TYPES.register("paper_dagger", () -> EntityType.Builder.<PaperDaggerProjectileEntity>of(PaperDaggerProjectileEntity::new, MobCategory.MISC)
                    .sized(.35f, .35f).build("paper_dagger"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
