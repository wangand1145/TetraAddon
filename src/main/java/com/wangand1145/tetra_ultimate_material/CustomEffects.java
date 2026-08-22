package com.wangand1145.tetra_ultimate_material;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
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

    // 注册两个效果标识符
    public static final ItemEffect COAL_REPAIR =
        ItemEffect.get("tetra_ultimate_material.coal_repair");
    public static final ItemEffect TORCH_CRAFT =
        ItemEffect.get("tetra_ultimate_material.torch_craft");

    /* ============ 效果1：挖煤矿石 2.5% 概率 +2 耐久 ============ */
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof ModularItem item)) return;

        int level = item.getEffectLevel(stack, COAL_REPAIR);
        if (level <= 0) return;

        // 用 forge:ores/coal 标签判定（覆盖所有模组的煤矿石）
        var coalOresTag = BlockTags.create(
            new ResourceLocation("forge", "ores/coal")
        );
        if (!event.getState().is(coalOresTag)) return;

        // 2.5% 概率，level 可微调概率倍率
        if (player.level().random.nextFloat() < 0.025f * level / 10f) {
            int damage = stack.getDamageValue();
            if (damage >= 2) {
                stack.setDamageValue(damage - 2);
                player.sendSystemMessage(
                    Component.literal("§a[苔煤] 工具恢复了 2 点耐久")
                );
            } else if (damage > 0) {
                stack.setDamageValue(0);
                player.sendSystemMessage(
                    Component.literal("§a[苔煤] 工具恢复了耐久")
                );
            }
        }
    }

    /* ============ 效果2：工具+木棍合成火把，消耗 5 耐久 ============ */
    @SubscribeEvent
    public static void onCraft(PlayerEvent.ItemCraftedEvent event) {
        if (event.getCrafting().getItem() != Items.TORCH) return;

        Player player = event.getPlayer();

        // 找背包里的 Tetra 工具（排除木棍）
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

        if (tool.isEmpty()) return; // 没有带效果的工具，不干预（走原版合成）

        int maxDmg = tool.getMaxDamage();
        int dmg = tool.getDamageValue();
        if (dmg + 5 > maxDmg) {
            event.setCanceled(true);
            player.sendSystemMessage(
                Component.literal("§c[苔煤] 工具耐久不足，无法制作火把")
            );
            return;
        }
        tool.setDamageValue(dmg + 5);
        player.sendSystemMessage(
            Component.literal("§a[苔煤] 消耗 5 点耐久制作火把")
        );
    }
}
