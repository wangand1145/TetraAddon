package com.wangand1145.tetra_ultimate_material.network;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class KillPacket {
    public static final ResourceKey<?> MAGIC_BACKFIRE =
        ResourceKey.create(
            Registries.DAMAGE_TYPE,
            new ResourceLocation("tetra_ultimate_material", "magic_backfire")
        );

    /** 直接伤害计算机最大数字上限（约 21 亿） */
    public static final float LETHAL_DAMAGE = Integer.MAX_VALUE; // 2147483647

    public KillPacket() {}

    public static void encode(KillPacket packet, FriendlyByteBuf buf) {}

    public static KillPacket decode(FriendlyByteBuf buf) {
        return new KillPacket();
    }

    public static void handle(KillPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.isAlive()) {
                RegistryAccess registryAccess = player.serverLevel().registryAccess();
                DamageSources damageSources = new DamageSources(registryAccess);
                DamageSource magicBackfire = damageSources.source(
                    (ResourceKey) MAGIC_BACKFIRE
                );
                // 用 Integer.MAX_VALUE 直接致命伤害，死亡消息为"被魔咒反噬了"
                player.hurt(magicBackfire, LETHAL_DAMAGE);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

