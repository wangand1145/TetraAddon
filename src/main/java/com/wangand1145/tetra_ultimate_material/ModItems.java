package com.wangand1145.tetra_ultimate_material;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;  // 按需
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "tetra_ultimate_material")  // 如需要
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, "tetra_ultimate_material");

    // ===== 普通物品 =====
    // 基泥（普通物品）
    public static final RegistryObject<Item> BASE_MUD = ITEMS.register("base_mud",
        () -> new Item(new Item.Properties().stacksTo(64)));

    // 苔煤团（原有）
    public static final RegistryObject<Item> MOSS_COAL = ITEMS.register("moss_coal",
        () -> new Item(new Item.Properties().stacksTo(64)));

    // 苔煤锭（原有）
    public static final RegistryObject<Item> MOSS_COAL_INGOT = ITEMS.register("moss_coal_ingot",
        () -> new Item(new Item.Properties().stacksTo(64)));

    // ===== 方块物品（BlockItem）=====
    // 基泥块的物品形态
    public static final RegistryObject<Item> BASE_MUD_BLOCK_ITEM = ITEMS.register("base_mud_block",
        () -> new BlockItem(ModBlocks.BASE_MUD_BLOCK.get(), new Item.Properties()));

    // 致密基泥块的物品形态
    public static final RegistryObject<Item> COMPACT_BASE_MUD_BLOCK_ITEM = ITEMS.register("compact_base_mud_block",
        () -> new BlockItem(ModBlocks.COMPACT_BASE_MUD_BLOCK.get(), new Item.Properties()));
}
