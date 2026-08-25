package com.wangand1145.tetra_ultimate_material.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class KillPacket {
    public KillPacket() {}

    public static void encode(KillPacket packet, FriendlyByteBuf buf) {}

    public static KillPacket decode(FriendlyByteBuf buf) {
        return new KillPacket();
    }

    public static void handle(KillPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.kill();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
