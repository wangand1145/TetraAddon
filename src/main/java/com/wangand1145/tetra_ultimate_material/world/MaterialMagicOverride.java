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
        Collection<MaterialData> all = ItemUpgradeRegistry.instance.getAllMaterials();
        for (MaterialData m : all) {
            if (m != null && m.key != null) {
                applyToOne(m);
                overriddenMaterials.add(m.key);
            }
        }
    }

    public static void ensureAllOverridden() {
        if (!overrideActive) return;
        Collection<MaterialData> all = ItemUpgradeRegistry.instance.getAllMaterials();
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
        // 可按材料 key 定制，默认返回原材料 magicCapacity（即"覆盖为自身"，等价于启用魔容机制）
        // 这里返回 100 作为统一魔容上限示例
        return 100;
    }
}
