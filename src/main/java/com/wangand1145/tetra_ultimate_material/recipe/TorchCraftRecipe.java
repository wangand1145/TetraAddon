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
import net.minecraft.world.level.Level;

public class TorchCraftRecipe extends CustomRecipe {
    public static final ResourceLocation ID = ResourceLocation.parse("tetra_ultimate_material:torch_craft");
    private final int torchCount;
    private final int durabilityCost;

    public TorchCraftRecipe(ResourceLocation id, int torchCount, int durabilityCost) {
        super(id, CraftingBookCategory.MISC);
        this.torchCount = torchCount;
        this.durabilityCost = durabilityCost;
    }

    // 根据截图 NBT 路径：TetraData.module.slot/binding.improvement
    private boolean hasTorchCraft(ItemStack stack) {
        var tag = stack.getTag();
        if (tag == null) return false;

        var tetraData = tag.getCompound("TetraData");
        if (tetraData.isEmpty()) return false;

        var module = tetraData.getCompound("module");
        if (module.isEmpty()) return false;

        // 遍历所有槽位，只要某个槽位的 improvement 中包含 torch_craft 就算
        for (String slot : module.getAllKeys()) {
            var slotData = module.getCompound(slot);
            var improvement = slotData.getCompound("improvement"); // 注意单数！
            for (String key : improvement.getAllKeys()) {
                if (key.contains("torch_craft")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        boolean hasTool = false;
        int stickCount = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;

            if (hasTorchCraft(stack)) {
                if (!hasTool) {
                    hasTool = true;
                } else {
                    return false; // 不允许放多个工具
                }
            } else if (stack.is(Items.STICK)) {
                stickCount += stack.getCount();
            } else {
                return false;
            }
        }
        return hasTool && stickCount >= 1;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
        return new ItemStack(Items.TORCH, torchCount);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;

            if (hasTorchCraft(stack)) {
                ItemStack copy = stack.copy();
                copy.hurtAndBreak(durabilityCost, null, p -> {});
                remaining.set(i, copy);
            } else if (stack.is(Items.STICK)) {
                ItemStack copy = stack.copy();
                copy.shrink(1);
                if (!copy.isEmpty()) remaining.set(i, copy);
            }
        }
        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) { return w >= 1 && h >= 1; }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TorchCraftRecipeSerializer.INSTANCE;
    }

    // ---------- 序列化器 ----------
    public static class TorchCraftRecipeSerializer implements RecipeSerializer<TorchCraftRecipe> {
        public static final TorchCraftRecipeSerializer INSTANCE = new TorchCraftRecipeSerializer();

        @Override
        public TorchCraftRecipe fromJson(ResourceLocation id, JsonObject json) {
            int count = json.has("torch_count") ? json.get("torch_count").getAsInt() : 4;
            int cost  = json.has("durability_cost") ? json.get("durability_cost").getAsInt() : 1;
            return new TorchCraftRecipe(id, count, cost);
        }

        @Override
        public TorchCraftRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new TorchCraftRecipe(id, buf.readVarInt(), buf.readVarInt());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, TorchCraftRecipe recipe) {
            buf.writeVarInt(recipe.torchCount);
            buf.writeVarInt(recipe.durabilityCost);
        }
    }
}

