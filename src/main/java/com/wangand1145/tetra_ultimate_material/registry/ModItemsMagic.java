package com.wangand1145.tetra_ultimate_material.registry;

import com.wangand1145.tetra_ultimate_material.TetraUltimateMaterial;
import com.wangand1145.tetra_ultimate_material.item.MagicEssenceItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemsMagic {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, TetraUltimateMaterial.MODID);

    // ↓↓↓ 这就是缺失的注册：魔容源质
    public static final RegistryObject<Item> MAGIC_ESSENCE = ITEMS.register(
        "magic_essence",
        () -> new MagicEssenceItem(
            new Item.Properties()
                .stacksTo(1)                    // 不可堆叠
                .rarity(Rarity.EPIC)            // 史诗品质（紫色）
                .fireResistant()               // 防火/抗火（你提到的"防火"）
        )
    );

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
