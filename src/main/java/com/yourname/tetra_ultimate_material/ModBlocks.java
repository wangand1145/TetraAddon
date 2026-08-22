package com.yourname.tetra_ultimate_material;

import net.minecraft.sounds.SoundType; 
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, "tetra_ultimate_material");

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, "tetra_ultimate_material");

    // 基泥方块
    public static final RegistryObject<Block> BASE_MUD_BLOCK =
        registerBlock("base_mud_block",
            () -> new Block(BlockBehaviour.Properties.of()
                .strength(2.0f, 4.0f)              // 硬度2.0，略低于石头
                .sound(net.minecraft.sounds.SoundType.GRAVEL)
                .requiresCorrectToolForDrops()
            )
        );

    // 通用注册：同时注册 Block + BlockItem
    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block) {
        RegistryObject<Block> registered = BLOCKS.register(name, block);
        ITEMS.register(name, () -> new BlockItem(registered.get(), new Item.Properties()));
        return registered;
    }
}

