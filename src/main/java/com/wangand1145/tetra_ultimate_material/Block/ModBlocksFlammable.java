package com.wangand1145.tetra_ultimate_material.block;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = "tetra_ultimate_material", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocksFlammable {
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            registerFlammable(ModBlocks.FLOURISHING_LOG.get(), 5, 20);
            registerFlammable(ModBlocks.SECRET_FOREST_LOG.get(), 5, 20);
        });
    }

    private static void registerFlammable(Block block, int encouragement, int flammability) {
        net.minecraftforge.common.extensions.IForgeBlock forgeBlock =
            (net.minecraftforge.common.extensions.IForgeBlock) block;
        forgeBlock.setFlammable(encouragement, flammability);
    }
}
