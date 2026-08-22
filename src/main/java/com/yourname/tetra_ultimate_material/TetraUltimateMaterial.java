package com.yourname.tetra_ultimate_material;
import com.yourname.tetra_ultimate_material.ModItems;   // ← 加这行
import com.yourname.tetra_ultimate_material.ModBlocks; 
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("tetra_ultimate_material")
public class TetraUltimateMaterial {

    public TetraUltimateMaterial() {
        // 物品注册
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        // 方块注册
        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModBlocks.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
