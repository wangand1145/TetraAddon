package com.wangand1145.tetra_ultimate_material.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.Level;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.ModularItem;

public class TorchCraftRecipe extends CustomRecipe {
    public static final ResourceLocation ID = new ResourceLocation("tetra_ultimate_material", "torch_craft");
    
    private final int torchCount;        // 产出火把数量
    private final int durabilityCost;    // 工具耐久消耗
    
    public TorchCraftRecipe(ResourceLocation id, int torchCount, int durabilityCost) {
        super(id, CraftingBookCategory.MISC);
        this.torchCount = torchCount;
        this.durabilityCost = durabilityCost;
    }
    
    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack tool = ItemStack.EMPTY;
        int stickCount = 0;
        
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;
            
            if (stack.getItem() instanceof ModularItem modular) {
                // 检查工具是否有 torch_craft 改进
                int level = modular.getImprovementLevel(stack, "tetra_ultimate_material/torch_craft");
                if (level > 0 && tool.isEmpty()) {
                    tool = stack;
                } else {
                    return false; // 多个工具或不匹配的模块化物品
                }
            } else if (stack.is(Items.STICK)) {
                stickCount += stack.getCount();
            } else {
                return false; // 无关物品
            }
        }
        
        // 需要恰好 1 个带 torch_craft 的工具 + 至少 1 个木棍
        return !tool.isEmpty() && stickCount >= 1;
    }
    
    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        return new ItemStack(Items.TORCH, torchCount);
    }
    
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(
            container.getContainerSize(), ItemStack.EMPTY);
        
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.getItem() instanceof ModularItem && 
                ((ModularItem) stack.getItem()).getImprovementLevel(
                    stack, "tetra_ultimate_material/torch_craft") > 0) {
                
                // 工具保留，扣耐久
                ItemStack toolCopy = stack.copy();
                toolCopy.hurtAndBreak(durabilityCost, /*dummy player*/ null, 
                    (p) -> {});
                remaining.set(i, toolCopy);
            } else if (stack.is(Items.STICK)) {
                // 木棍消耗 1 个
                ItemStack stickCopy = stack.copy();
                stickCopy.shrink(1);
                if (!stickCopy.isEmpty()) {
                    remaining.set(i, stickCopy);
                }
            }
        }
        return remaining;
    }
    
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 1 && height >= 1; // 1x1 网格即可（原版工作台也行）
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return TorchCraftRecipeSerializer.INSTANCE;
    }
    
    // ===== Serializer =====
    public static class TorchCraftRecipeSerializer implements RecipeSerializer<TorchCraftRecipe> {
        public static final TorchCraftRecipeSerializer INSTANCE = new TorchCraftRecipeSerializer();
        
        @Override
        public TorchCraftRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            int torchCount = json.has("torch_count") ? 
                json.get("torch_count").getAsInt() : 4;
            int durabilityCost = json.has("durability_cost") ? 
                json.get("durability_cost").getAsInt() : 1;
            return new TorchCraftRecipe(recipeId, torchCount, durabilityCost);
        }
        
        @Override
        public TorchCraftRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int torchCount = buffer.readVarInt();
            int durabilityCost = buffer.readVarInt();
            return new TorchCraftRecipe(recipeId, torchCount, durabilityCost);
        }
        
        @Override
        public void toNetwork(FriendlyByteBuf buffer, TorchCraftRecipe recipe) {
            buffer.writeVarInt(recipe.torchCount);
            buffer.writeVarInt(recipe.durabilityCost);
        }
    }
}
