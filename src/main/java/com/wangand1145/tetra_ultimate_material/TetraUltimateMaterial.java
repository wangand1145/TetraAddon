package com.wangand1145.tetra_ultimate_material;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "tetra_ultimate_material", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandlers {

    @SubscribeEvent
    public static void onBuildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        // 只往"功能物品"标签页里加（你在创造栏点金色铁砧那个 tab）
        if (event.getTabKey() != CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            return;
        }

        // ===== 材料物品 =====
        event.accept(ModItems.BASE_MUD.get());          // 基泥
        event.accept(ModItems.MOSS_COAL.get());         // 苔煤团
        event.accept(ModItems.MOSS_COAL_INGOT.get());   // 苔煤锭

        // ===== 方块 =====
        event.accept(ModBlocks.BASE_MUD_BLOCK.get());           // 基泥块
        event.accept(ModBlocks.COMPACT_BASE_MUD_BLOCK.get());   // 致密基泥块
    }
}
