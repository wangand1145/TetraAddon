package com.wangand1145.tetra_ultimate_material.registry;

import com.wangand1145.tetra_ultimate_material.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "tetra_ultimate_material", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModFuelHandlers {

    /**
     * 前两个原木（繁茂原木、秘林原木）作为熔炉燃料的燃烧时间。
     * 熔炉每烧 1 个物品消耗 200 tick，所以烧 50 个物品 = 10000 tick。
     * 对比：原版原木只有 300 tick（烧 1.5 个物品）。
     */
    private static final int MAGIC_LOG_BURN_TIME = 50 * 200; // 10000 tick

    @SubscribeEvent
    public static void onFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        Block block = Block.byItem(event.getItemStack().getItem());
        if (block == ModBlocks.FLOURISHING_LOG.get()
                || block == ModBlocks.SECRET_FOREST_LOG.get()) {
            event.setBurnTime(MAGIC_LOG_BURN_TIME);
        }
    }
}

