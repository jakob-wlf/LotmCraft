package de.jakob.lotm.gui.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.network.PacketHandler;
import de.jakob.lotm.network.packets.OpenAbilitySelectionPacket;
import de.jakob.lotm.util.ClientBeyonderCache;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class AbilitySelectionScreen extends AbstractContainerScreen<AbilitySelectionMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = 
        ResourceLocation.fromNamespaceAndPath(LOTMCraft.MOD_ID, "textures/gui/ability_selection_gui.png");
    
    private Button leftButton;
    private Button rightButton;
    private Button debugButton;
    
    public AbilitySelectionScreen(AbilitySelectionMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        
        // Calculate screen size based on rows
        this.imageHeight = 114 + menu.getRows() * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }
    
    @Override
    protected void init() {
        super.init();

        if(this.minecraft == null) return;

        Player player = this.minecraft.player;
        if(!ClientBeyonderCache.isBeyonder(player.getUUID()))
            return;

        int sequence = this.menu.getSequence();
        String pathway = this.menu.getPathway();
        
        // Left button
        if(sequence < 9) {
            this.leftButton = Button.builder(
                            Component.literal("<"),
                            button -> {
                                int newSequence = sequence + 1;
                                player.closeContainer();
                                PacketHandler.sendToServer(new OpenAbilitySelectionPacket(newSequence, pathway));
                            })
                    .bounds(this.leftPos - 25, this.topPos + 20, 20, 20)
                    .build();
            this.addRenderableWidget(leftButton);
        }

        if(sequence > ClientBeyonderCache.getSequence(player.getUUID())) {
            // Right button
            this.rightButton = Button.builder(
                            Component.literal(">"),
                            button -> {
                                int newSequence = sequence - 1;
                                player.closeContainer();
                                PacketHandler.sendToServer(new OpenAbilitySelectionPacket(newSequence, pathway));
                            })
                    .bounds(this.leftPos + this.imageWidth + 5, this.topPos + 20, 20, 20)
                    .build();
            this.addRenderableWidget(rightButton);
        }
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    public void updateMenuData(int sequence, String pathway) {
        // Update the menu's data
        this.menu.updateData(sequence, pathway);
        // Recreate buttons with new data
        this.clearWidgets();
        this.init();
    }
    
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        
        // Render the background texture
        // Top part
        guiGraphics.blit(CONTAINER_BACKGROUND, x, y, 0, 0, this.imageWidth, 17);
        
        // Middle part (repeated for each row)
        for (int i = 0; i < this.menu.getRows(); i++) {
            guiGraphics.blit(CONTAINER_BACKGROUND, x, y + 17 + i * 18, 0, 17, this.imageWidth, 19);
        }
        
        // Bottom part (player inventory background)
        guiGraphics.blit(CONTAINER_BACKGROUND, x, y + 18 + this.menu.getRows() * 18, 0, 126, this.imageWidth, 96);
    }
    
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }
}