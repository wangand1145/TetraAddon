package com.wangand1145.tetra_ultimate_material.world;

import se.mickelus.tetra.module.data.MaterialData;
import se.mickelus.tetra.module.ItemUpgradeRegistry;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MaterialMagicOverride {
    /** 魔容上限系数（相对于原材料 magicCapacity）。设为 1.0 表示保持原值，仅改名称为魔容。 */
    public static final double CAPACITY_MULTIPLIER = 1.0;
    /** 激活标记：为 true 时对新材料做增量覆盖。 */
    public static boolean overrideActive = false;

    private static final Set<String> overriddenMaterials = ConcurrentHashMap.newKeySet();

    /** 首次激活：覆盖当前所有已注册材料。 */
    public static void applyOverrideToAll() {
        Collection<MaterialData> all = ItemUpgradeRegistry.instance.getAllMaterials();
        for (MaterialData material : all) {
            if (material == null) continue;
            if (overriddenMaterials.add(getMaterialKey(material))) {
                applyOverrideToSingle(material);
            }
        }
        overrideActive = true;
    }

    /** 增量覆盖：覆盖自上次以来新注册的材料。 */
    public static void ensureAllOverridden() {
        if (!overrideActive) return;
        Collection<MaterialData> all = ItemUpgradeRegistry.instance.getAllMaterials();
        for (MaterialData material : all) {
            if (material == null) continue;
            if (overriddenMaterials.add(getMaterialKey(material))) {
                applyOverrideToSingle(material);
            }
        }
    }

    private static void applyOverrideToSingle(MaterialData material) {
        try {
            Method getter = findMethod(material.getClass(), "getMagicCapacity", "getMagic", "magicCapacity");
            if (getter == null) return;
            Object raw = getter.invoke(material);
            int original = (raw instanceof Number) ? ((Number) raw).intValue() : 0;
            int newCap = (int) Math.max(1, Math.round(original * CAPACITY_MULTIPLIER));

            Method setter = findMethod(material.getClass(), "setMagicCapacity", "setMagic");
            if (setter != null) {
                setter.invoke(material, newCap);
            }
        } catch (Exception ignored) {
            // 反射失败静默忽略，不影响其他材料
        }
    }

    private static String getMaterialKey(MaterialData material) {
        try {
            Method m = findMethod(material.getClass(), "getKey", "key");
            if (m != null) {
                Object key = m.invoke(material);
                return key == null ? String.valueOf(material.hashCode()) : key.toString();
            }
        } catch (Exception ignored) {}
        return String.valueOf(material.hashCode());
    }

    private static Method findMethod(Class<?> clazz, String... names) {
        for (String name : names) {
            try {
                Method m = clazz.getMethod(name);
                if (m != null) return m;
            } catch (NoSuchMethodException ignored) {}
            // 尝试带 int 参数的 setter
            try {
                Method m = clazz.getMethod(name, int.class);
                if (m != null) return m;
            } catch (NoSuchMethodException ignored) {}
        }
        return null;
    }
}

