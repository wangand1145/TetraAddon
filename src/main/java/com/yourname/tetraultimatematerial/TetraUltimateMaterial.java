package com.yourname.tetraultimatematerial;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;

@Mod(TetraUltimateMaterial.MOD_ID)
public class TetraUltimateMaterial {
    public static final String MOD_ID = "tetra_ultimate_material";

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> MOSS_COAL_INGOT =
        ITEMS.register("moss_coal_ingot",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public TetraUltimateMaterial() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        bus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // ✅ 用 getTab() 比较，类型匹配
        if (event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.accept(new ItemStack(MOSS_COAL_INGOT.get()));
        }
    }
}
