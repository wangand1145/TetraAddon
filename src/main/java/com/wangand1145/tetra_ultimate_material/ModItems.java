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

    // —— 新增 5 个物品 ——
    // 基泥苔煤锭（物品）
    public static final RegistryObject<Item> BASE_MUD_MOSS_COAL_INGOT = ITEMS.register(
        "base_mud_moss_coal_ingot",
        () -> new Item(new Item.Properties().stacksTo(64)));

    // 四个原木的方块物品（方块本身在 ModBlocks 注册）
    public static final RegistryObject<Item> FLOURISHING_LOG = ITEMS.register(
        "flourishing_log",
        () -> new BlockItem(ModBlocks.FLOURISHING_LOG.get(), new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> SECRET_FOREST_LOG = ITEMS.register(
        "secret_forest_log",
        () -> new BlockItem(ModBlocks.SECRET_FOREST_LOG.get(), new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> FOREST_ESSENCE_LOG = ITEMS.register(
        "forest_essence_log",
        () -> new BlockItem(ModBlocks.FOREST_ESSENCE_LOG.get(), new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> ULTIMATE_LOG = ITEMS.register(
        "ultimate_log",
        () -> new BlockItem(ModBlocks.ULTIMATE_LOG.get(), new Item.Properties().stacksTo(64).rarity(Rarity.EPIC)));

    public static void register(net.minecraftforge.eventbus.api.IEventBus bus) {
        ITEMS.register(bus);
    }
}
