package de.jakob.lotm.item;

import de.jakob.lotm.LOTMCraft;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LOTMCraft.MOD_ID);

    public static final DeferredItem<Item> FOOL_Card = ITEMS.registerItem("fool_card", Item::new, new Item.Properties());
    public static final DeferredItem<Item> MOD_ICON = ITEMS.registerItem("lotm_icon", Item::new, new Item.Properties());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
