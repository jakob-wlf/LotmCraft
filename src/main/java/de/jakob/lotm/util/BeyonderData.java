package de.jakob.lotm.util;

import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.network.PacketHandler;
import de.jakob.lotm.network.packets.SyncBeyonderDataPacket;
import de.jakob.lotm.util.pathways.PathwayInfos;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.List;

public class BeyonderData {
    private static final String NBT_PATHWAY = "beyonder_pathway";
    private static final String NBT_SEQUENCE = "beyonder_sequence";
    private static final String NBT_SPIRITUALITY = "beyonder_spirituality";
    private static final String NBT_GRIEFING_ENABLED = "beyonder_griefing_enabled";

    private static final int[] spiritualityLookup = {150000, 20000, 10000, 5000, 3900, 1900, 1200, 780, 200, 180};
    private static final double[] multiplier = {5, 3.5, 3, 2.4, 2.0, 1.6, 1.4, 1.2, 1.0, 1.0};

    public static final List<String> pathways = List.of(
            "fool",
            "error",
            "door",
            "visionary",
            "sun",
            "tyrant",
            "white_tower",
            "hanged_man",
            "darkness",
            "death",
            "twilight_giant",
            "demoness",
            "red_priest",
            "hermit",
            "paragon",
            "wheel_of_fortune",
            "mother",
            "moon",
            "abyss",
            "chained",
            "black_emperor",
            "justiciar"
    );

    public static final HashMap<String, PathwayInfos> pathwayInfos = new HashMap<>();

    public static void initPathwayInfos() {
        pathwayInfos.put("fool", new PathwayInfos("Fool", "fool", 0xFF864ec7, new String[]{}));
        pathwayInfos.put("error", new PathwayInfos("Error", "error", 0xFF0018b8, new String[]{}));
        pathwayInfos.put("door", new PathwayInfos("Door", "door", 0xFF89f5f5, new String[]{}));
        pathwayInfos.put("visionary", new PathwayInfos("Visionary", "visionary", 0xFFe3ffff, new String[]{}));
        pathwayInfos.put("sun", new PathwayInfos("Sun", "sun", 0xFFffad33, new String[]{}));
        pathwayInfos.put("tyrant", new PathwayInfos("Tyrant", "tyrant", 0xFF336dff, new String[]{}));
        pathwayInfos.put("white_tower", new PathwayInfos("White Tower", "white_tower", 0xFF8cadff, new String[]{}));
        pathwayInfos.put("hanged_man", new PathwayInfos("Hanged Man", "hanged_man", 0xFF8a0a0a, new String[]{}));
        pathwayInfos.put("darkness", new PathwayInfos("Darkness", "darkness", 0xFF3300b5, new String[]{}));
        pathwayInfos.put("death", new PathwayInfos("Death", "death", 0xFF334f23, new String[]{}));
        pathwayInfos.put("twilight_giant", new PathwayInfos("Twilight Giant", "twilight_giant", 0xFF944b16, new String[]{}));
        pathwayInfos.put("demoness", new PathwayInfos("Demoness", "demoness", 0xFFc014c9, new String[]{}));
        pathwayInfos.put("red_priest", new PathwayInfos("Red Priest", "red_priest", 0xFFb80000, new String[]{}));
        pathwayInfos.put("hermit", new PathwayInfos("Hermit", "hermit", 0xFF832ed9, new String[]{}));
        pathwayInfos.put("paragon", new PathwayInfos("Paragon", "paragon", 0xFFf58e40, new String[]{}));
        pathwayInfos.put("wheel_of_fortune", new PathwayInfos("Wheel of Fortune", "wheel_of_fortune", 0xFFbad2f5, new String[]{}));
        pathwayInfos.put("mother", new PathwayInfos("Mother", "mother", 0xFF6bdb94, new String[]{}));
        pathwayInfos.put("moon", new PathwayInfos("Moon", "moon", 0xFFf5384b, new String[]{}));
        pathwayInfos.put("abyss", new PathwayInfos("Abyss", "abyss", 0xFFa3070c, new String[]{}));
        pathwayInfos.put("chained", new PathwayInfos("Chained", "chained", 0xFFb18fbf, new String[]{}));
        pathwayInfos.put("black_emperor", new PathwayInfos("Black Emperor", "black_emperor", 0xFF181040, new String[]{}));
        pathwayInfos.put("justiciar", new PathwayInfos("Justiciar", "justiciar", 0xFFfcd99f, new String[]{}));
        pathwayInfos.put("debug", new PathwayInfos("Debug", "debug", 0xFFf5baba, new String[]{}));

    }

    public static void setBeyonder(LivingEntity entity, String pathway, int sequence) {
        CompoundTag tag = entity.getPersistentData();
        tag.putString(NBT_PATHWAY, pathway);
        tag.putInt(NBT_SEQUENCE, sequence);
        tag.putFloat(NBT_SPIRITUALITY, getMaxSpirituality(sequence));
        tag.putBoolean(NBT_GRIEFING_ENABLED, false);

        if(entity instanceof Player player)
            SpiritualityProgressTracker.setProgress(player, 1.0f);

        // Sync to client if this is server-side
        if (!entity.level().isClientSide() && entity instanceof ServerPlayer serverPlayer) {
            PacketHandler.syncBeyonderDataToPlayer(serverPlayer);
        }
    }

    public static String getPathway(LivingEntity entity) {
        String pathway = entity.getPersistentData().getString(NBT_PATHWAY);
        if(pathway.isBlank() || pathway.equalsIgnoreCase("") || pathway.isEmpty())
            return "none";
        return pathway;
    }

    public static int getSequence(LivingEntity entity) {
        if (!entity.getPersistentData().contains(NBT_SEQUENCE)) {
            return -1;
        }
        return entity.getPersistentData().getInt(NBT_SEQUENCE);
    }

    public static float getSpirituality(Player player) {
        float spirituality = player.getPersistentData().getFloat(NBT_SPIRITUALITY);
        float maxSpirituality = getMaxSpirituality(getSequence(player));

        if(maxSpirituality <= 0) {
            return 0.0f;
        }

        float progress = spirituality / maxSpirituality;
        SpiritualityProgressTracker.setProgress(player, progress);

        return Math.max(0, spirituality);
    }

    public static void reduceSpirituality(Player player, float amount) {
        float current = getSpirituality(player);
        player.getPersistentData().putFloat(NBT_SPIRITUALITY, Math.max(0, current - amount));

        float maxSpirituality = getMaxSpirituality(getSequence(player));

        if(maxSpirituality <= 0) {
            return;
        }

        float progress = (current - amount) / maxSpirituality;
        SpiritualityProgressTracker.setProgress(player, progress);

        // Sync to client if this is server-side
        if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            PacketHandler.syncBeyonderDataToPlayer(serverPlayer);
        }
    }

    public static double getMultiplier(LivingEntity entity) {
        int sequence = getSequence(entity);
        if (sequence < 0 || sequence >= multiplier.length) {
            return 1.0; // Default multiplier if sequence is invalid
        }
        return multiplier[sequence];
    }

    public static void incrementSpirituality(Player player, float amount) {
        float current = getSpirituality(player);
        float newAmount = Math.min(getMaxSpirituality(getSequence(player)), current + amount);
        player.getPersistentData().putFloat(NBT_SPIRITUALITY, newAmount);

        float maxSpirituality = getMaxSpirituality(getSequence(player));

        if(maxSpirituality <= 0) {
            return;
        }

        float progress = newAmount / maxSpirituality;
        SpiritualityProgressTracker.setProgress(player, progress);

        // Sync to client if this is server-side
        if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            PacketHandler.syncBeyonderDataToPlayer(serverPlayer);
        }
    }

    public static void resetSpirituality(Player player) {
        int sequence = getSequence(player);
        player.getPersistentData().putFloat(NBT_SPIRITUALITY, getMaxSpirituality(sequence));

        if(getMaxSpirituality(sequence) <= 0) {
            return;
        }

        float progress = player.getPersistentData().getFloat(NBT_SPIRITUALITY) / getMaxSpirituality(sequence);
        SpiritualityProgressTracker.setProgress(player, progress);
    }

    public static float getMaxSpirituality(int sequence) {
        return sequence >= 0 && sequence < spiritualityLookup.length ? spiritualityLookup[sequence] : 0.0f;
    }

    public static void clearBeyonderData(LivingEntity entity) {
        entity.getPersistentData().remove(NBT_PATHWAY);
        entity.getPersistentData().remove(NBT_SEQUENCE);
        entity.getPersistentData().remove(NBT_SPIRITUALITY);
        entity.getPersistentData().remove(NBT_GRIEFING_ENABLED);
        if(entity instanceof Player player)
            SpiritualityProgressTracker.removeProgress(player);

        // Sync to client if this is server-side
        if (!entity.level().isClientSide() && entity instanceof ServerPlayer serverPlayer) {
            // Send empty data to clear client cache
            SyncBeyonderDataPacket packet = new SyncBeyonderDataPacket("none", -1, 0.0f, false);
            PacketHandler.sendToPlayer(serverPlayer, packet);
        }
    }

    public static boolean isBeyonder(LivingEntity entity) {
        return entity.getPersistentData().contains(NBT_PATHWAY) && entity.getPersistentData().contains(NBT_SEQUENCE);
    }

    public static boolean isGriefingEnabled(Player player) {
        if (player.level().isClientSide()) {
            // On client side, read from cache instead of NBT
            return ClientBeyonderCache.isGriefingEnabled(player.getUUID());
        }
        return player.getPersistentData().getBoolean(NBT_GRIEFING_ENABLED);
    }

    public static boolean isGriefingEnabled(LivingEntity entity) {
        if(!(entity instanceof Player player)) {
            return false;
        }
        return player.getPersistentData().getBoolean(NBT_GRIEFING_ENABLED);
    }

    public static void setPathway(LivingEntity entity, String pathway) {
        entity.getPersistentData().putString(NBT_PATHWAY, pathway);

        // Sync to client if this is server-side
        if (!entity.level().isClientSide() && entity instanceof ServerPlayer serverPlayer) {
            PacketHandler.syncBeyonderDataToPlayer(serverPlayer);
        }
    }

    public static void setSequence(LivingEntity entity, int sequence) {
        entity.getPersistentData().putInt(NBT_SEQUENCE, sequence);
        entity.getPersistentData().putFloat(NBT_SPIRITUALITY, getMaxSpirituality(sequence));

        // Sync to client if this is server-side
        if (!entity.level().isClientSide() && entity instanceof ServerPlayer serverPlayer) {
            PacketHandler.syncBeyonderDataToPlayer(serverPlayer);
        }
    }

    private static float getRelativeSpirituality(Player player) {
        float maxSpirituality = getMaxSpirituality(getSequence(player));
        if (maxSpirituality <= 0) {
            return 0.0f;
        }
        return getSpirituality(player) / maxSpirituality;
    }

    public static void setGriefingEnabled(Player player, boolean enabled) {
        player.getPersistentData().putBoolean(NBT_GRIEFING_ENABLED, enabled);

        // Sync to client if this is server-side
        if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            PacketHandler.syncBeyonderDataToPlayer(serverPlayer);
        }
    }
}