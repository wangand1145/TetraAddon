package com.wangand1145.tetra_ultimate_material;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.level.BlockEvent;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.module.ItemEffect;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = "tetra_ultimate_material")
public class CustomEffects {

    public static final ItemEffect COAL_REPAIR = ItemEffect.get("tetra_ultimate_material.coal_repair");

    private static final Map<UUID, Long> lastRepairMsg = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.level().isClientSide()) return;

        // 只处理煤矿石（含深层）
        if (!event.getState().is(net.minecraft.tags.BlockTags.COAL_ORES)) return;

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof ModularItem modItem)) return;

        int level = modItem.getEffectLevel(stack, COAL_REPAIR);
        if (level <= 0) return;

        // 概率 = 0.25% * level（level=10 时为 2.5%）
        float chance = 0.0025f * level;
        if (player.getRandom().nextFloat() >= chance) return;

        // 恢复耐久 = max(1, level / 5)（level=10 时恢复 2）
        int heal = Math.max(1, level / 5);
        if (stack.isDamageableItem()) {
            stack.setDamageValue(Math.max(0, stack.getDamageValue() - heal));
        }

        // 10 秒内只提示一次
        long now = player.level().getGameTime() / 20;
        long last = lastRepairMsg.getOrDefault(player.getUUID(), -10L);
        if (now - last >= 10) {
            lastRepairMsg.put(player.getUUID(), now);
            player.displayClientMessage(
                net.minecraft.network.chat.Component.literal(
                    "§a[苔煤修复] §7工具恢复了 " + heal + " 点耐久"),
                false); // false = 不在 action bar 显示，而是在聊天栏
        }
    }
}
