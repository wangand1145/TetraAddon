package com.wangand1145.tetra_ultimate_material.registry;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipes {

    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "tetra_ultimate_material");

    // 示例占位，当前未注册任何配方序列化器
    // public static final RegistryObject<RecipeSerializer<?>> EXAMPLE = RECIPE_SERIALIZERS.register("example", () -> null);

    private ModRecipes() {}

    public static void register(IEventBus modEventBus) {
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}
