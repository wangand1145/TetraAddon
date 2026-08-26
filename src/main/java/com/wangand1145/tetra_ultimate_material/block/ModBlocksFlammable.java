package com.wangand1145.tetra_ultimate_material.Block; // 若包名是 Block 则保留，否则改为 block

import com.wangand1145.tetra_ultimate_material.block.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = "tetra_ultimate_material", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocksFlammable {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            registerFlammable(ModBlocks.FLOURISHING_LOG.get(), 5, 20);
            registerFlammable(ModBlocks.SECRET_FOREST_LOG.get(), 5, 20);
        });
    }

    private static void registerFlammable(net.minecraft.world.level.block.Block block, int encouragement, int flammability) {
        FireBlock fire = (FireBlock) Blocks.FIRE;
        fire.setFlammable(block, encouragement, flammability);
    }
}

