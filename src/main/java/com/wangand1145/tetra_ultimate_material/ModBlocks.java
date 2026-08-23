package com.wangand1145.tetra_ultimate_material;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, "tetra_ultimate_material");

    public static final DeferredRegister<Item> BLOCK_ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, "tetra_ultimate_material");

    // ===== 基泥块 =====
    public static final RegistryObject<Block> BASE_MUD_BLOCK =
        registerBlock("base_mud_block",
            () -> new Block(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(0.6F)
                .sound(SoundType.GRAVEL)
                .requiresCorrectToolForDrops()
            )
        );

    // ===== 致密基泥块 =====
    public static final RegistryObject<Block> COMPACT_BASE_MUD_BLOCK =
        registerBlock("compact_base_mud_block",
            () -> new Block(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(3.0F, 6.0F)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops()
            )
        );

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        RegistryObject<Block> block = BLOCKS.register(name, blockSupplier);
        BLOCK_ITEMS.register(name,
            () -> new BlockItem(block.get(), new Item.Properties())
        );
        return block;
    }
}
