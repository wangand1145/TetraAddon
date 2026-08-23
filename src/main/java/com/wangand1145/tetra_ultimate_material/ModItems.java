package com.wangand1145.tetra_ultimate_material;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, "tetra_ultimate_material");

    // ===== 原有物品（加上了 .tab() 修复创造栏不显示的问题）=====

    public static final RegistryObject<Item> BASE_MUD =
        ITEMS.register("base_mud",
            () -> new Item(new Item.Properties().stacksTo(64).tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)));

    public static final RegistryObject<Item> MOSS_COAL =
        ITEMS.register("moss_coal",
            () -> new Item(new Item.Properties().stacksTo(64).tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)));

    public static final RegistryObject<Item> MOSS_COAL_INGOT =
        ITEMS.register("moss_coal_ingot",
            () -> new Item(new Item.Properties().stacksTo(64).tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)));

    // ===== 新增：基泥块物品 =====

    public static final RegistryObject<Item> BASE_MUD_BLOCK_ITEM =
        ITEMS.register("base_mud_block",
            () -> new BlockItem(ModBlocks.BASE_MUD_BLOCK.get(),
                new Item.Properties().tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)));

    // ===== 新增：致密基泥块物品 =====

    public static final RegistryObject<Item> COMPACT_BASE_MUD_BLOCK_ITEM =
        ITEMS.register("compact_base_mud_block",
            () -> new BlockItem(ModBlocks.COMPACT_BASE_MUD_BLOCK.get(),
                new Item.Properties().tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)));
}

