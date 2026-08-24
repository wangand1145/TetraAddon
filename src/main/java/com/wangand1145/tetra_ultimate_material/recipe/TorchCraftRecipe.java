package com.wangand1145.tetra_ultimate_material.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class TorchCraftRecipe extends CustomRecipe {
    // 修正：使用 1.20.1 的传统构造方法
    public static final ResourceLocation ID = new ResourceLocation("tetra_ultimate_material", "torch_craft");
    private final int torchCount;
    private final int durabilityCost;

    public TorchCraftRecipe(ResourceLocation id, int torchCount, int durabilityCost) {
        super(id, CraftingBookCategory.MISC);
        this.torchCount = torchCount;
        this.durabilityCost = durabilityCost;
    }

    /**
     * 检测物品是否具有 torch_craft 改进。
     * 先尝试精确的 Tetra NBT 路径，再回退到递归搜索。
     */
    private boolean hasTorchCraft(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return false;

        // 精确路径：TetraData.module.<slot>.improvement
        CompoundTag tetraData = tag.getCompound("TetraData");
        if (!tetraData.isEmpty()) {
            CompoundTag module = tetraData.getCompound("module");
            if (!module.isEmpty()) {
                for (String slot : module.getAllKeys()) {
                    CompoundTag slotData = module.getCompound(slot);
                    // 尝试单数和复数两种形式
                    CompoundTag improvement = slotData.getCompound("improvement");
                    if (!improvement.isEmpty()) {
                        for (String key : improvement.getAllKeys()) {
                            if (key.contains("torch_craft")) return true;
                        }
                    }
                    CompoundTag improvements = slotData.getCompound("improvements");
                    if (!improvements.isEmpty()) {
                        for (String key : improvements.getAllKeys()) {
                            if (key.contains("torch_craft")) return true;
                        }
                    }
                }
            }
        }

        // 回退：递归搜索整个 NBT
        return searchNbtForTorchCraft(tag);
    }

    private boolean searchNbtForTorchCraft(Tag tag) {
        if (tag == null) return false;

        if (tag instanceof CompoundTag compound) {
            for (String key : compound.getAllKeys()) {
                if (key.contains("torch_craft")) return true;
                if (searchNbtForTorchCraft(compound.get(key))) return true;
            }
        } else if (tag instanceof ListTag list) {
            for (int i = 0; i < list.size(); i++) {
                if (searchNbtForTorchCraft(list.get(i))) return true;
            }
        } else if (tag instanceof StringTag str) {
            return str.getAsString().contains("torch_craft");
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
                    return false;
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

        // 尝试获取玩家实体
        Player player = null;
        if (container instanceof Player) {
            player = (Player) container;
        } else if (container instanceof AbstractContainerMenu menu) {
            player = menu.player;
        }

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;

            if (hasTorchCraft(stack)) {
                ItemStack copy = stack.copy();
                // 如果获取到玩家，则传入玩家；否则传 null
                if (player != null) {
                    copy.hurtAndBreak(durabilityCost, player, p -> {});
                } else {
                    copy.hurtAndBreak(durabilityCost, null, p -> {});
                }
                remaining.set(i, copy);
            } else if (stack.is(Items.STICK)) {
                ItemStack copy = stack.copy();
                copy.shrink(1);
                if (!copy.isEmpty()) {
                    remaining.set(i, copy);
                }
            }
        }
        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return w >= 1 && h >= 1;
    }

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

