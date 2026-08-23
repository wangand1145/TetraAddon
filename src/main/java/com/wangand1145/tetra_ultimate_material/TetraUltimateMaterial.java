package com.wangand1145.tetra_ultimate_material;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod("tetra_ultimate_material")
public class TetraUltimateMaterial {

    public TetraUltimateMaterial() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(bus);          // 纯物品
        ModBlocks.BLOCKS.register(bus);        // 方块
        ModBlocks.BLOCK_ITEMS.register(bus);   // 方块对应的物品（关键！）
    }
}
