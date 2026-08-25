package com.wangand1145.tetra_ultimate_material.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class MagicContainerData {
    public static final String NBT_KEY = "tetra_ultimate_material:magic_container";
    public static final int DEFAULT_CAPACITY = 100;

    /** 从武器 NBT 中读取魔容值。优先读自定义字段，回退到 Tetra 各模块 magicCapacity 之和。 */
    public static int getMagicContainer(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 0;

        // 优先读取我们维护的魔容字段
        if (tag.contains(NBT_KEY)) {
            return tag.getInt(NBT_KEY);
        }

        // 回退：对 Tetra 武器，尝试从各模块 magicCapacity 字段求和
        return sumMagicCapacityFromNbt(stack);
    }

    /** 写入魔容值到武器 NBT。 */
    public static void setMagicContainer(ItemStack stack, int value) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(NBT_KEY, value);
    }

    /** 判断物品是否为 Tetra 模块化工具（通过 NBT 中是否存在 TetraData 标签）。 */
    public static boolean isTetraTool(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return false;
        return tag.contains("TetraData");
    }

    /** 尝试从 Tetra 武器 NBT 的各模块 magicCapacity 字段求和。 */
    public static int sumMagicCapacityFromNbt(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 0;
        CompoundTag tetraData = tag.getCompound("TetraData");
        if (tetraData.isEmpty()) return 0;

        int total = 0;
        CompoundTag module = tetraData.getCompound("module");
        for (String slot : module.getAllKeys()) {
            CompoundTag slotData = module.getCompound(slot);
            if (slotData.contains("magicCapacity")) {
                total += slotData.getInt("magicCapacity");
            } else if (slotData.contains("magic")) {
                total += slotData.getInt("magic");
            }
        }
        return total;
    }

    /** 获取武器使用的首个材料 key（用于未来专属规则扩展）。 */
    public static String getMaterialKey(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("TetraData")) return "";
        CompoundTag module = tag.getCompound("TetraData").getCompound("module");
        for (String slot : module.getAllKeys()) {
            CompoundTag slotData = module.getCompound(slot);
            if (slotData.contains("material")) {
                return slotData.getString("material");
            }
        }
        return "";
    }
}

