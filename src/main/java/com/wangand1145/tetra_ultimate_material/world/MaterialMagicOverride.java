package com.wangand1145.tetra_ultimate_material.world;

import se.mickelus.tetra.module.data.MaterialData;
import se.mickelus.tetra.module.ItemUpgradeRegistry;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MaterialMagicOverride {
    public static boolean overrideActive = false;
    private static final Set<String> overriddenMaterials = ConcurrentHashMap.newKeySet();

    public static void applyOverrideToAll() {
        overrideActive = true;
        overriddenMaterials.clear();
        Collection<MaterialData> all = ItemUpgradeRegistry.instance.getMaterials();
        for (MaterialData m : all) {
            if (m != null && m.key != null) {
                applyToOne(m);
                overriddenMaterials.add(m.key);
            }
        }
    }

    public static void ensureAllOverridden() {
        if (!overrideActive) return;
        Collection<MaterialData> all = ItemUpgradeRegistry.instance.getMaterials(); // ✅ 修正
        for (MaterialData m : all) {
            if (m != null && m.key != null && overriddenMaterials.add(m.key)) {
                applyToOne(m);
            }
        }
    }

    private static void applyToOne(MaterialData material) {
        try {
            // 反射 setMagicCapacity(int)
            MaterialData.class.getMethod("setMagicCapacity", int.class)
                .invoke(material, getOverrideValue(material.key));
        } catch (Exception ignored) {}
    }

    private static int getOverrideValue(String key) {
        // 按你的要求：魔容上限按武器魔法容量上限，不设默认值，没有就是零
        // 这里返回 0 意味着"覆盖为 0"，即魔容上限由武器 NBT 中的 magicCapacity 决定
        return 0;
    }
}
