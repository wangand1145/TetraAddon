package com.wangand1145.tetra_ultimate_material.registry;

import com.wangand1145.tetra_ultimate_material.recipe.TorchCraftRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = 
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "tetra_ultimate_material");
    
    public static final RegistryObject<RecipeSerializer<TorchCraftRecipe>> TORCH_CRAFT = 
        RECIPE_SERIALIZERS.register("torch_craft", 
            () -> TorchCraftRecipe.TorchCraftRecipeSerializer.INSTANCE);
    
    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }
}
