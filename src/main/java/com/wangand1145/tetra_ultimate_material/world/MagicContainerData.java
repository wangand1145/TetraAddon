package com.wangand1145.tetra_ultimate_material.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 魔容数据工具。
 * 核心原则：魔容 = Tetra 原版魔法容量（magicCapacity），数值不变，仅改名。
 * - 魔容上限：武器各 major module 的 magicCapacity 之和（无默认值，为零则为零）。
 * - 魔容当前值：武器 NBT 中各模块 magicCapacity 剩余值之和。
 */
public class MagicContainerData {

    /** 武器级魔容自定义字段（预留，当前未使用，读取优先回退到 Tetra 原生字段） */
    public static final String NBT_KEY = "tetra_ultimate_material:magic_container";

    /* ================= 上限（capacity）================= */

    /**
     * 魔容上限 = 武器各模块 magicCapacity 上限值之和。
     * 读取 TetraData.module.<slot>.magicCapacity（材料定义的上限值）。
     * 无 magicCapacity 信息的模块不参与求和；整把武器无任何 magicCapacity 时返回 0。
     */
    public static int getMaxMagicContainer(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 0;
        CompoundTag tetraData = tag.getCompound("TetraData");
        if (tetraData.isEmpty()) return 0;

        int total = 0;
        CompoundTag module = tetraData.getCompound("module");
        for (String slot : module.getAllKeys()) {
            CompoundTag slotData = module.getCompound(slot);
            int slotCap = readMagicCapacity(slotData);
            total += slotCap; // 无 magicCapacity 的模块返回 0，不影响
        }
        return total; // 无信息则返回 0
    }

    /** 读取单个模块 slot 的 magicCapacity 上限值（多路径兼容） */
    public static int readMagicCapacity(CompoundTag slotData) {
        if (slotData == null) return 0;
        if (slotData.contains("magicCapacity")) return slotData.getInt("magicCapacity");
        if (slotData.contains("magic_capacity")) return slotData.getInt("magic_capacity");
        // 部分版本放在 material 子对象
        CompoundTag mat = slotData.getCompound("material");
        if (!mat.isEmpty()) {
            if (mat.contains("magicCapacity")) return mat.getInt("magicCapacity");
            if (mat.contains("magic_capacity")) return mat.getInt("magic_capacity");
        }
        return 0;
    }

    /* ================= 当前值（remaining）================= */

    /**
     * 魔容当前值 = 武器各模块 magicCapacity 剩余值之和。
     * 优先读自定义字段；回退到 Tetra 各模块剩余值求和。
     */
    public static int getMagicContainer(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 0;
        // 优先读取我们维护的魔容字段（若外部写入过）
        if (tag.contains(NBT_KEY)) {
            return tag.getInt(NBT_KEY);
        }
        // 回退：对 Tetra 武器，从各模块剩余 magicCapacity 求和
        return sumMagicCapacityRemaining(stack);
    }

    /** 写入魔容值到武器 NBT（自定义字段） */
    public static void setMagicContainer(ItemStack stack, int value) {
        stack.getOrCreateTag().putInt(NBT_KEY, value);
    }

    /**
     * 从武器 NBT 中各模块读取 magicCapacity 剩余值并求和。
     * 剩余值字段名因 Tetra 版本而异（magic / magicCapacity / remainingMagic 等）。
     */
    public static int sumMagicCapacityRemaining(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 0;
        CompoundTag tetraData = tag.getCompound("TetraData");
        if (tetraData.isEmpty()) return 0;

        int total = 0;
        CompoundTag module = tetraData.getCompound("module");
        for (String slot : module.getAllKeys()) {
            CompoundTag slotData = module.getCompound(slot);
            if (slotData.contains("magic")) {
                total += slotData.getInt("magic");
            } else if (slotData.contains("magicCapacity")) {
                total += slotData.getInt("magicCapacity");
            } else if (slotData.contains("remainingMagic")) {
                total += slotData.getInt("remainingMagic");
            }
        }
        return total;
    }

    /* ================= 工具方法 ================= */

    /** 判断是否为 Tetra 模块化工具（剑/镐/斧/三叉戟/矛等所有模块化物品） */
    public static boolean isTetraTool(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return false;
        return tag.contains("TetraData");
    }

    /** 获取武器上所有模块槽位名（含 major 与 minor） */
    public static List<String> getAllModuleSlots(ItemStack stack) {
        List<String> slots = new ArrayList<>();
        CompoundTag tag = stack.getTag();
        if (tag == null) return slots;
        CompoundTag module = tag.getCompound("TetraData").getCompound("module");
        for (String slot : module.getAllKeys()) slots.add(slot);
        return slots;
    }

    /** 获取首个材料 key（用于专属规则路由，当前未使用） */
    public static String getMaterialKey(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("TetraData")) return "";
        CompoundTag module = tag.getCompound("TetraData").getCompound("module");
        for (String slot : module.getAllKeys()) {
            CompoundTag slotData = module.getCompound(slot);
            if (slotData.contains("material")) {
                String key = slotData.getString("material");
                if (!key.isEmpty()) return key;
            }
            CompoundTag mat = slotData.getCompound("material");
            if (!mat.isEmpty() && mat.contains("key")) return mat.getString("key");
        }
        return "";
    }

    /* ================= 按模块独立读取（设计 A 备用）================= */

    /** 单个模块的魔容上限 */
    public static int getModuleMagicCapacity(ItemStack stack, String slot) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 0;
        CompoundTag slotData = tag.getCompound("TetraData").getCompound("module").getCompound(slot);
        return readMagicCapacity(slotData);
    }

    /** 单个模块的魔容当前剩余值 */
    public static int getModuleMagicRemaining(ItemStack stack, String slot) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 0;
        CompoundTag slotData = tag.getCompound("TetraData").getCompound("module").getCompound(slot);
        if (slotData.contains("magic")) return slotData.getInt("magic");
        if (slotData.contains("magicCapacity")) return slotData.getInt("magicCapacity");
        if (slotData.contains("remainingMagic")) return slotData.getInt("remainingMagic");
        return 0;
    }
}
