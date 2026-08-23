package com.wangand1145.tetra_ultimate_material;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod("tetra_ultimate_material")
public class TetraUltimateMaterial {

    public TetraUltimateMaterial() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(bus);    // ✅ 注册所有物品（含基泥、苔煤团、苔煤锭、两个方块物品）
        ModBlocks.BLOCKS.register(bus);  // ✅ 注册所有方块（基泥块、致密基泥块）
    }
}
