package com.wangand1145.tetra_ultimate_material;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod("tetra_ultimate_material")
public class TetraUltimateMaterial {

    public TetraUltimateMaterial() {
        IEventBus bus = ModLoadingContext.get().getActiveContainer().getEventBus();

        ModItems.ITEMS.register(bus);      
        ModBlocks.BLOCKS.register(bus);   
    }
}
