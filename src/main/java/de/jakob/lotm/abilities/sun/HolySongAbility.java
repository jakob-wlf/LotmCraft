package de.jakob.lotm.abilities.sun;

import de.jakob.lotm.LOTMCraft;
import de.jakob.lotm.util.abilities.AbilityItem;
import de.jakob.lotm.util.helper.ParticleUtil;
import de.jakob.lotm.util.scheduling.ClientScheduler;
import de.jakob.lotm.util.scheduling.ServerScheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HolySongAbility extends AbilityItem {
    public HolySongAbility(Properties properties) {
        super(properties, 20);
    }

    @Override
    public Map<String, Integer> getRequirements() {
        Map<String, Integer> reqs = new HashMap<>();
        reqs.put("sun", 9);
        return reqs;
    }

    @Override
    protected float getSpiritualityCost() {
        return 12;
    }

    private final int duration = 20 * 20;
    DustParticleOptions dustOptions = new DustParticleOptions(
            new Vector3f(255 / 255f, 180 / 255f, 66 / 255f),
            1.5f
    );

    @Override
    protected void onAbilityUse(Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, 0, false, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, 1, false, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, duration, 0, false, false, false));

            ServerScheduler.scheduleForDuration(0, 5, duration, () -> {
                for(int i = 0; i < 6; i++) {
                    ((ServerLevel) level).sendParticles(
                            ParticleTypes.NOTE,
                            entity.getX() + random.nextFloat(-1.5f, 1.5f), entity.getY() + entity.getEyeHeight() + random.nextFloat(-.5f, .5f), entity.getZ() + random.nextFloat(-1.5f, 1.5f),
                            1,
                            0f, 0f, 0f, 1f
                    );
                }

                ParticleUtil.spawnParticles((ServerLevel) level, dustOptions, entity.getEyePosition().subtract(0, entity.getEyeHeight() / 2, 0), 2, 1);
            });

            level.playSound(null, entity, SoundEvents.MUSIC_DISC_PIGSTEP.value(), entity.getSoundSource(), 1.0f, 1.0f);

            ServerScheduler.scheduleDelayed(duration, () -> {
                if (level instanceof ServerLevel serverLevel) {
                    // Stop the sound for all players in range
                    for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers()) {
                        if (player.distanceToSqr(entity) <= 64 * 64) { // Within hearing range
                            player.connection.send(new ClientboundStopSoundPacket(
                                    ResourceLocation.fromNamespaceAndPath("minecraft", "music_disc.pigstep"),
                                    entity.getSoundSource()
                            ));
                        }
                    }
                }
            });
        }
    }
}
