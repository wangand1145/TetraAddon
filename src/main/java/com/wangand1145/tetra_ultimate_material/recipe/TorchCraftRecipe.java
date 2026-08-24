package com.wangand1145.tetra_ultimate_material.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class TorchCraftRecipe extends CustomRecipe {
    private final int torchCount;
    private final int durabilityCost;

    public TorchCraftRecipe(ResourceLocation id, int torchCount, int durabilityCost) {
        super(id, CraftingBookCategory.MISC);
        this.torchCount = torchCount;
        this.durabilityCost = durabilityCost;
    }

    // 判断合成是否匹配：需要 1 个带 torch_craft 的工具 + 至少 1 根木棍
    @Override
    public boolean matches(CraftingContainer container, Level level) {
        boolean hasTool = false;
        int stickCount = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof IModularItem) {
                if (!hasTool && hasTorchCraft(stack)) {
                    hasTool = true;
                } else {
                    return false; // 多个工具或工具没有 torch_craft
                }
            } else if (stack.is(Items.STICK)) {
                stickCount += stack.getCount();
            } else {
                return false;
            }
        }
        return hasTool && stickCount >= 1;
    }

    // 合成结果：火把
    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
        return new ItemStack(Items.TORCH, torchCount);
    }

    // 剩余物品：工具保留并扣耐久，木棍消耗 1 根
    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof IModularItem && hasTorchCraft(stack)) {
                ItemStack copy = stack.copy();
                copy.hurtAndBreak(durabilityCost, null, p -> {});
                remaining.set(i, copy);
            } else if (stack.is(Items.STICK)) {
                // 木棍只消耗 1 根，多余的退回
                ItemStack copy = stack.copy();
                copy.shrink(1);
                if (!copy.isEmpty()) remaining.set(i, copy);
            }
        }
        return remaining;
    }

    // 检查工具 NBT 中是否有 torch_craft 改进
    private boolean hasTorchCraft(ItemStack stack) {
        var tag = stack.getTag();
        if (tag == null) return false;
        // 尝试两种常见的 NBT 路径
        var improvements = tag.getCompound("Improvements");
        if (!improvements.isEmpty()) {
            for (String key : improvements.getAllKeys()) {
                if (key.contains("torch_craft")) return true;
            }
        }
        var tetraData = tag.getCompound("TetraData");
        if (!tetraData.isEmpty()) {
            var module = tetraData.getCompound("module");
            for (String slot : module.getAllKeys()) {
                var imps = module.getCompound(slot).getCompound("improvements");
                for (String key : imps.getAllKeys()) {
                    if (key.contains("torch_craft")) return true;
                }
            }
        }
        return false;
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
