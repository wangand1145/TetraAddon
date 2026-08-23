package com.wangand1145.tetra_ultimate_material;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoader;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.effect.ItemEffect;

@Mod.EventBusSubscriber(modid = "tetra_ultimate_material")
public class CustomEffects {

    // 注册效果标识符（与材料 JSON 的 effects 字段 key 一一对应）
    public static final ItemEffect COAL_REPAIR =
        ItemEffect.get("tetra_ultimate_material.coal_repair");
    public static final ItemEffect TORCH_CRAFT =
        ItemEffect.get("tetra_ultimate_material.torch_craft");

    /* ========== 效果1：挖煤矿石 2.5% 概率 +2 耐久 ========== */
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof ModularItem item)) return;

        // 精确判断：工具是否携带 coal_repair 效果
        int level = item.getEffectLevel(stack, COAL_REPAIR);
        if (level <= 0) return;

        // 判断挖的是否为煤矿石（原版两种）
        var block = event.getState().getBlock();
        if (block != Blocks.COAL_ORE && block != Blocks.DEEPSLATE_COAL_ORE) return;

        // 2.5% * (level/10) 概率
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

    /* ========== 效果2：合成火把消耗耐久 ========== */
    @SubscribeEvent
    public static void onCraft(PlayerEvent.ItemCraftedEvent event) {
        // 只在合成火把时处理
        if (event.getCrafting().getItem() != Items.TORCH) return;

        Player player = event.getEntity();
        if (player == null) return;

        // 在背包里找"带有 TORCH_CRAFT 效果的 Tetra 工具"
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

        // 没找到带效果的苔煤工具，不干预（可能是用煤炭+木棍原版合成）
        if (tool.isEmpty()) return;

        // 扣 5 点耐久
        int dmg = tool.getDamageValue();
        int maxDmg = tool.getMaxDamage();
        if (dmg + 5 > maxDmg) {
            // 耐久不足：取消合成（注意 Forge 论坛建议谨慎取消此事件）
            // 参考：https://forums.minecraftforge.net/topic/80433-solved-how-to-cancel-playereventitemcraftedevent/
            event.setCanceled(true);
            player.sendSystemMessage(Component.literal("§c[苔煤] 工具耐久不足，无法制作火把"));
            return;
        }
        tool.setDamageValue(dmg + 5);
        player.sendSystemMessage(Component.literal("§a[苔煤] 消耗 5 点耐久制作火把"));
    }

    /* ========== 注册"苔煤物品 + 木棍 → 火把"配方 ========== */
    // 注意：要让"工具+木棍"合成火把，需要额外注册自定义配方
    // 下面是一个简化方案：在 mod 主类构造里调用此方法注册
    public static void registerTorchRecipe(net.minecraftforge.event.OnDatapackSyncEvent event) {
        // 实际配方注册较复杂，涉及 RecipeSerializer
        // 推荐改用数据包方式（见说明）
    }
}
