package com.yourname.tetra_ultimate_material;

import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(TetraUltimateMaterial.MOD_ID)
public class TetraUltimateMaterial {
    public static final String MOD_ID = "tetra_ultimate_material";

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> MOSS_COAL_INGOT =
        ITEMS.register("moss_coal_ingot",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public TetraUltimateMaterial() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}

