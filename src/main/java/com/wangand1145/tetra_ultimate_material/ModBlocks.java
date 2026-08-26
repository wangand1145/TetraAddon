package com.wangand1145.tetra_ultimate_material;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(ForgeRegistries.BLOCKS, "tetra_ultimate_material");

    // 基泥块
    public static final RegistryObject<Block> BASE_MUD_BLOCK = BLOCKS.register("base_mud_block",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(1.5f, 6.0f)));  // 硬度/抗性，按需调整

    // 致密基泥块
    public static final RegistryObject<Block> COMPACT_BASE_MUD_BLOCK = BLOCKS.register("compact_base_mud_block",
        () -> new Block(BlockBehaviour.Properties.of()
            .strength(3.0f, 12.0f)));  // 更硬  DeferredRegister.create(ForgeRegistries.BLOCKS, TetraUltimateMaterial.MODID);

    public static final RegistryObject<Block> FLOURISHING_LOG = BLOCKS.register(
        "flourishing_log",
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0f)
            .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> SECRET_FOREST_LOG = BLOCKS.register(
        "secret_forest_log",
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0f)
            .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> FOREST_ESSENCE_LOG = BLOCKS.register(
        "forest_essence_log",
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0f)
            .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> ULTIMATE_LOG = BLOCKS.register(
        "ultimate_log",
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PURPLE) // 究极原木用紫色
            .strength(5.0f)
            .requiresCorrectToolForDrops()));

    public static void register(net.minecraftforge.eventbus.api.IEventBus bus) {
        BLOCKS.register(bus);
    }
}
