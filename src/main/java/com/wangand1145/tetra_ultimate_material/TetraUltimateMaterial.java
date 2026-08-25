package com.wangand1145.tetra_ultimate_material;

import com.wangand1145.tetra_ultimate_material.registry.ModItemsMagic;
import com.wangand1145.tetra_ultimate_material.registry.ModRecipes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TetraUltimateMaterial.MODID)  // ← 改用常量
public class TetraUltimateMaterial {

    public static final String MODID = "tetra_ultimate_material";  // ← 新增这一行

    public TetraUltimateMaterial() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(bus);      // 物品
        ModBlocks.BLOCKS.register(bus);    // 方块
        ModRecipes.register(bus);          // 配方序列化器
        ModItemsMagic.register(bus);       // 魔容源质
    }
}
