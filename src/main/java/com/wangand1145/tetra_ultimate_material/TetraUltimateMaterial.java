package com.wangand1145.tetra_ultimate_material;

import com.wangand1145.tetra_ultimate_material.registry.ModItemsMagic;
import com.wangand1145.tetra_ultimate_material.registry.ModRecipes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("tetra_ultimate_material")
public class TetraUltimateMaterial {

    public TetraUltimateMaterial() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(bus);      // 物品
        ModBlocks.BLOCKS.register(bus);    // 方块
        ModRecipes.register(bus);          // 配方序列化器 ← 新加的这一行
        ModItemsMagic.register(bus);      
    }
}
