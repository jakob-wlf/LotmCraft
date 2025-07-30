package de.jakob.lotm.util.events;

import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.util.BeyonderData;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = LOTMCraft.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        LOTMCraft.becomeBeyonderKey = new KeyMapping("key.beyonders.become_beyonder_debug", GLFW.GLFW_KEY_B, "key.categories.beyonders");
        LOTMCraft.switchBeyonderKey = new KeyMapping("key.beyonders.switch_beyonder", GLFW.GLFW_KEY_N, "key.categories.beyonders");
        LOTMCraft.getAbilitiesKey = new KeyMapping("key.beyonders.get_abilities", GLFW.GLFW_KEY_A, "key.categories.beyonders");
        LOTMCraft.openAbilitySelectionKey = new KeyMapping("key.beyonders.open_ability_selection", GLFW.GLFW_KEY_V, "key.categories.beyonders");
        LOTMCraft.toggleGriefingKey = new KeyMapping("key.beyonders.toggle_griefing", GLFW.GLFW_KEY_G, "key.categories.beyonders");
        LOTMCraft.clearBeyonderKey = new KeyMapping("key.beyonders.clear_beyonder", GLFW.GLFW_KEY_C, "key.categories.beyonders");
        LOTMCraft.showPassiveAbilitiesKey = new KeyMapping("key.beyonders.show_passives", GLFW.GLFW_KEY_I, "key.categories.beyonders");
        LOTMCraft.switchAbilityKey = new KeyMapping("key.beyonders.switch_ability", GLFW.GLFW_KEY_V, "key.categories.beyonders");
        LOTMCraft.increaseSequenceKey = new KeyMapping("key.beyonders.increase_sequence", GLFW.GLFW_KEY_UP, "key.categories.beyonders");
        LOTMCraft.decreaseSequenceKey = new KeyMapping("key.beyonders.decrease_sequence", GLFW.GLFW_KEY_DOWN, "key.categories.beyonders");
        event.register(LOTMCraft.increaseSequenceKey);
        event.register(LOTMCraft.decreaseSequenceKey);
        event.register(LOTMCraft.becomeBeyonderKey);
        event.register(LOTMCraft.switchBeyonderKey);
        event.register(LOTMCraft.getAbilitiesKey);
        event.register(LOTMCraft.openAbilitySelectionKey);
        event.register(LOTMCraft.toggleGriefingKey);
        event.register(LOTMCraft.clearBeyonderKey);
        event.register(LOTMCraft.showPassiveAbilitiesKey);
        event.register(LOTMCraft.switchAbilityKey);
    }
}