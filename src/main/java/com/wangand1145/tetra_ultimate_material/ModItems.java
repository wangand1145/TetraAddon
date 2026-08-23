package com.wangand1145.tetra_ultimate_material;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, "tetra_ultimate_material");

    // ===== 材料物品 =====

    public static final RegistryObject<Item> BASE_MUD =
        ITEMS.register("base_mud",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> MOSS_COAL =
        ITEMS.register("moss_coal",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> MOSS_COAL_INGOT =
        ITEMS.register("moss_coal_ingot",
            () -> new Item(new Item.Properties().stacksTo(64)));

    // ===== 方块物品（基泥块 / 致密基泥块）=====
    // 方块本体在 ModBlocks.BLOCKS 注册；这里只注册它们对应的 BlockItem，
    // 让它们能以"物品"形式出现在物品栏/JEI/创造栏。
    // 注意：BlockItem 本身不加 .tab()，创造栏挂载交给 ModEventHandlers 事件。

    public static final RegistryObject<Item> BASE_MUD_BLOCK_ITEM =
        ITEMS.register("base_mud_block",
            () -> new BlockItem(ModBlocks.BASE_MUD_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> COMPACT_BASE_MUD_BLOCK_ITEM =
        ITEMS.register("compact_base_mud_block",
            () -> new BlockItem(ModBlocks.COMPACT_BASE_MUD_BLOCK.get(), new Item.Properties()));
}

