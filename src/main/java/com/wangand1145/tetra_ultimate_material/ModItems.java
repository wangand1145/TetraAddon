package com.wangand1145.tetra_ultimate_material;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, "tetra_ultimate_material");

    public static final RegistryObject<Item> BASE_MUD =
        ITEMS.register("base_mud",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> MOSS_COAL =
        ITEMS.register("moss_coal",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> MOSS_COAL_INGOT =
        ITEMS.register("moss_coal_ingot",
            () -> new Item(new Item.Properties().stacksTo(64)));
}
