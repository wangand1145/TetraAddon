package com.wangand1145.tetra_ultimate_material.world;

/**
 * 材料魔容覆盖引擎（已废弃 / 不再使用）。
 *
 * 设计变更：魔容 = Tetra 原版魔法容量（magicCapacity），数值不变，仅改名。
 * 因此不再需要将材料的 magicCapacity 覆盖为 0 或其他值。
 * 本类保留仅为兼容旧引用（如有），所有方法均为空实现，不产生任何副作用。
 *
 * 如需为特定材料添加专属魔容规则，请实现 MaterialMagicContainer 接口并在
 * PlayerTickMagicEffects 中注册，而非修改材料数值。
 */
public class MaterialMagicOverride {

    private MaterialMagicOverride() {}

    /** @deprecated 不再覆盖任何材料数值。魔容 = 魔法容量，数值不变。 */
    @Deprecated
    public static void applyOverrideToAll() {}

    /** @deprecated 不再增量覆盖。 */
    @Deprecated
    public static void ensureAllOverridden() {}
}
