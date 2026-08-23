package com.wangand1145.tetra_ultimate_material;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.effect.ItemEffect;

@Mod.EventBusSubscriber(modid = "tetra_ultimate_material")
public class CustomEffects {

    public static final ItemEffect COAL_REPAIR =
        ItemEffect.get("tetra_ultimate_material.coal_repair");
    public static final ItemEffect TORCH_CRAFT =
        ItemEffect.get("tetra_ultimate_material.torch_craft");

    /* ===== 挖煤矿石 2.5% 概率 +2 耐久 ===== */
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof ModularItem item)) return;

        int level = item.getEffectLevel(stack, COAL_REPAIR);
        if (level <= 0) return;

        var block = event.getState().getBlock();
        if (block != Blocks.COAL_ORE && block != Blocks.DEEPSLATE_COAL_ORE) return;

        if (player.level().random.nextFloat() < 0.025f * level / 10f) {
            int damage = stack.getDamageValue();
            if (damage >= 2) {
                stack.setDamageValue(damage - 2);
                player.sendSystemMessage(Component.literal("§a[苔煤] 工具恢复了 2 点耐久"));
            } else if (damage > 0) {
                stack.setDamageValue(0);
                player.sendSystemMessage(Component.literal("§a[苔煤] 工具恢复了耐久"));
            }
        }
    }

    /* ===== 合成火把消耗耐久（精确判断 TORCH_CRAFT 效果等级）===== */
    @SubscribeEvent
    public static void onCraft(PlayerEvent.ItemCraftedEvent event) {
        if (event.getCrafting().getItem() != Items.TORCH) return;

        Player player = event.getEntity();
        if (player == null) return;

        ItemStack tool = ItemStack.EMPTY;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack slot = player.getInventory().getItem(i);
            if (slot.getItem() instanceof ModularItem mItem) {
                int lvl = mItem.getEffectLevel(slot, TORCH_CRAFT);
                if (lvl > 0) {
                    tool = slot;
                    break;
                }
            }
        }

        if (tool.isEmpty()) return;

        int dmg = tool.getDamageValue();
        if (dmg + 5 > tool.getMaxDamage()) {
            event.setCanceled(true);
            player.sendSystemMessage(Component.literal("§c[苔煤] 工具耐久不足，无法制作火把"));
            return;
        }
        tool.setDamageValue(dmg + 5);
        player.sendSystemMessage(Component.literal("§a[苔煤] 消耗 5 点耐久制作火把"));
    }
}
