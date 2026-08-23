package com.wangand1145.tetra_ultimate_material;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod("tetra_ultimate_material")
public class TetraUltimateMaterial {

    public TetraUltimateMaterial() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(bus);      // ← 物品 + BlockItem 都在这一个里
        ModBlocks.BLOCKS.register(bus);    // ← 方块本体
        // ⚠️ 删掉 ModBlocks.BLOCK_ITEMS.register(bus); 这行！
    }
}
