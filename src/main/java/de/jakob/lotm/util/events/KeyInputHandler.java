package de.jakob.lotm.util.events;

import de.jakob.lotm.network.PacketHandler;
import de.jakob.lotm.network.packets.*;
import de.jakob.lotm.util.ClientBeyonderCache;
import de.jakob.lotm.util.abilities.AbilityHandler;
import de.jakob.lotm.util.BeyonderData;
import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.util.abilities.SelectableAbilityItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = LOTMCraft.MOD_ID, value = Dist.CLIENT)
public class KeyInputHandler {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (LOTMCraft.becomeBeyonderKey != null && LOTMCraft.becomeBeyonderKey.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                // Send packet to server instead of directly modifying data
                PacketHandler.sendToServer(new BecomeBeyonderPacket("debug", 1));
            }
        }
        if (LOTMCraft.getAbilitiesKey != null && LOTMCraft.getAbilitiesKey.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                // Send packet to server instead of directly modifying data
                PacketHandler.sendToServer(new ReceiveAbilityItemsPacket());
            }
        }
        if (LOTMCraft.openAbilitySelectionKey != null && LOTMCraft.openAbilitySelectionKey.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                int sequence = ClientBeyonderCache.getSequence(player.getUUID());
                String pathway = ClientBeyonderCache.getPathway(player.getUUID());
                PacketHandler.sendToServer(new OpenAbilitySelectionPacket(sequence, pathway));
            }
        }

        if (LOTMCraft.switchBeyonderKey != null && LOTMCraft.switchBeyonderKey.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                String currentPathway = ClientBeyonderCache.getPathway(player.getUUID());
                if (currentPathway.equalsIgnoreCase("none")) {
                    PacketHandler.sendToServer(new BecomeBeyonderPacket("debug", 1));
                } else {
                    int index = BeyonderData.pathways.indexOf(currentPathway) + 1;
                    String pathway = "debug";
                    if (index < BeyonderData.pathways.size()) {
                        pathway = BeyonderData.pathways.get(index);
                    }

                    PacketHandler.sendToServer(new BecomeBeyonderPacket(pathway, 1));
                }
            }
        }

        if (LOTMCraft.increaseSequenceKey != null && LOTMCraft.increaseSequenceKey.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                int currentSequence = ClientBeyonderCache.getSequence(player.getUUID());
                String currentPathway = ClientBeyonderCache.getPathway(player.getUUID());
                if(currentSequence > 1)
                    currentSequence--;
                PacketHandler.sendToServer(new BecomeBeyonderPacket(currentPathway, currentSequence));
            }
        }

        if (LOTMCraft.decreaseSequenceKey != null && LOTMCraft.decreaseSequenceKey.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                int currentSequence = ClientBeyonderCache.getSequence(player.getUUID());
                String currentPathway = ClientBeyonderCache.getPathway(player.getUUID());
                if(currentSequence < 9)
                    currentSequence++;
                PacketHandler.sendToServer(new BecomeBeyonderPacket(currentPathway, currentSequence));
            }
        }

        if(LOTMCraft.toggleGriefingKey != null && LOTMCraft.toggleGriefingKey.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                // Send packet to server instead of directly modifying data
                PacketHandler.sendToServer(new ToggleGriefingPacket());
            }
        }

        if(LOTMCraft.clearBeyonderKey != null && LOTMCraft.clearBeyonderKey.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                // Send packet to server instead of directly modifying data
                PacketHandler.sendToServer(new ClearBeyonderDataPacket());
            }
        }

        if(LOTMCraft.switchAbilityKey != null && LOTMCraft.switchAbilityKey.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if(player != null && ClientBeyonderCache.isBeyonder(player.getUUID())) {
                if(player.getMainHandItem().getItem() instanceof SelectableAbilityItem abilityItem && abilityItem.canUse(player))
                    abilityItem.switchAbility(player);
            }
        }
    }
}
