package com.wangand1145.tetra_ultimate_material.world;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = "tetra_ultimate_material")
public class MagicContainerTracker {
    private static final Map<UUID, Map<Integer, Integer>> lastMagicMap = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;

        Player player = event.player;
        UUID uuid = player.getUUID();
        Map<Integer, Integer> lastValues = lastMagicMap.computeIfAbsent(uuid, k -> new HashMap<>());

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || !MagicContainerData.isTetraTool(stack)) continue;

            int current = MagicContainerData.getMagicContainer(stack);
            int identity = System.identityHashCode(stack);
            Integer last = lastValues.get(identity);
            if (last != null && last > current) {
                int consumed = last - current;
                // 魔容减少了 consumed。效果判定由 PlayerTickMagicEffects 基于当前值处理。
            }
            lastValues.put(identity, current);
        }
    }
}


