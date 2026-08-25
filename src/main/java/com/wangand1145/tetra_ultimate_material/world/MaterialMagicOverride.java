package com.wangand1145.tetra_ultimate_material.world;

import se.mickelus.tetra.module.data.MaterialData;
import se.mickelus.tetra.module.ItemUpgradeRegistry;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MaterialMagicOverride {
    public static boolean overrideActive = false;
    private static final Set<String> overriddenMaterials = ConcurrentHashMap.newKeySet();

    /** 通过反射获取所有材料，兼容不同 Tetra 版本/API */
    @SuppressWarnings("unchecked")
    private static Collection<MaterialData> getAllMaterialsSafe() {
        try {
            // 尝试 ItemUpgradeRegistry 上的常见方法名
            for (String methodName : new String[]{"getAllMaterials", "getMaterials", "getMaterialData", "values"}) {
                try {
                    java.lang.reflect.Method m = ItemUpgradeRegistry.class.getMethod(methodName);
                    Object result = m.invoke(ItemUpgradeRegistry.instance);
                    if (result instanceof Collection) {
                        return (Collection<MaterialData>) result;
                    }
                } catch (NoSuchMethodException ignored) {
                    // 尝试下一个
                }
            }
            // 尝试 MaterialRegistry（如果存在）
            Class<?> matRegClass = Class.forName("se.mickelus.tetra.module.MaterialRegistry");
            java.lang.reflect.Method m = matRegClass.getMethod("getInstance");
            Object instance = m.invoke(null);
            for (String methodName : new String[]{"getAllMaterials", "getMaterials", "values"}) {
                try {
                    java.lang.reflect.Method m2 = matRegClass.getMethod(methodName);
                    Object result = m2.invoke(instance);
                    if (result instanceof Collection) {
                        return (Collection<MaterialData>) result;
                    }
                } catch (NoSuchMethodException ignored) {}
            }
        } catch (Exception ignored) {}
        return Collections.emptyList();
    }

    public static void applyOverrideToAll() {
        overrideActive = true;
        overriddenMaterials.clear();
        Collection<MaterialData> all = getAllMaterialsSafe();
        for (MaterialData m : all) {
            if (m != null && m.key != null) {
                applyToOne(m);
                overriddenMaterials.add(m.key);
            }
        }
    }

    public static void ensureAllOverridden() {
        if (!overrideActive) return;
        Collection<MaterialData> all = getAllMaterialsSafe();
        for (MaterialData m : all) {
            if (m != null && m.key != null && overriddenMaterials.add(m.key)) {
                applyToOne(m);
            }
        }
    }

    private static void applyToOne(MaterialData material) {
        try {
            // 反射 setMagicCapacity(int)，兼容不同版本方法名
            for (String methodName : new String[]{"setMagicCapacity", "setMagicCapacity"}) {
                try {
                    java.lang.reflect.Method m = MaterialData.class.getMethod(methodName, int.class);
                    m.invoke(material, getOverrideValue(material.key));
                    return;
                } catch (NoSuchMethodException ignored) {}
            }
        } catch (Exception ignored) {}
    }

    /** 魔容上限按武器魔法容量上限，不设默认值，没有就是零 → 返回 0 */
    private static int getOverrideValue(String key) {
        return 0;
    }
}

