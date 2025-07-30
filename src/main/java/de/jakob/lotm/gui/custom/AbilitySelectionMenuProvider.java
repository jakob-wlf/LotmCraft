package de.jakob.lotm.gui.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AbilitySelectionMenuProvider implements MenuProvider {
    private final List<ItemStack> items;
    private final String title;
    private final int color;
    private final int sequence;
    private final String pathway;

    public AbilitySelectionMenuProvider(List<ItemStack> items, String title, int color, int sequence, String pathway) {
        this.items = items;
        this.title = title;
        this.color = color;
        this.sequence = sequence;
        this.pathway = pathway;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal(title)
                .withStyle(style -> style.withColor(color));
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AbilitySelectionMenu(containerId, playerInventory, items, sequence, pathway);
    }
}