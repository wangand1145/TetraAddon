package com.wangand1145.tetra_ultimate_material.world;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * 材料专属魔容机制接口（预留）。
 * 当前系统未使用任何专属实现，所有 Tetra 工具统一走普通魔容机制。
 * 未来如需为特定材料添加独立规则，可实现此接口并在 PlayerTickMagicEffects 中注册。
 */
public interface MaterialMagicContainer {
    String getMaterialKey();
    int getCapacity(ItemStack stack);
    void applyNegativeEffect(Player player, ItemStack stack);
    boolean applyRangeEffect(Player player, ItemStack stack, double ratio);
}
