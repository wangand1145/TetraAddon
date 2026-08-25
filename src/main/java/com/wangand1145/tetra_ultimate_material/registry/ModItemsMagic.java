package com.wangand1145.tetra_ultimate_material.registry;

import com.wangand1145.tetra_ultimate_material.registry.ModItemsMagic;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItemsMagic {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, "tetra_ultimate_material");

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}

