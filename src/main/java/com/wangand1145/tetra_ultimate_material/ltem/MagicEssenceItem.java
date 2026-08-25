package com.wangand1145.tetra_ultimate_material.item;

import com.wangand1145.tetra_ultimate_material.world.MagicEssenceWorldData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MagicEssenceItem extends Item {
    public MagicEssenceItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }

        // ✅ 修复：获取 ServerLevel
        ServerLevel serverLevel = level.getServer().getLevel(level.dimension());
        MagicEssenceWorldData data = MagicEssenceWorldData.get(serverLevel);

        if (data.isActivated()) {   // ✅ 现在 isActivated() 可用
            player.displayClientMessage(Component.literal("§c[魔容源质] §f当前世界魔容模式已激活。"), false);
            return InteractionResultHolder.success(stack);
        }

        data.setActivated(true);
        // data.setDirty();  // 可选，因为 setActivated 内部已调用 setDirty()
        player.displayClientMessage(Component.literal("§c[魔容源质] §6魔容模式已激活！所有 Tetra 材料的魔法容量已被覆盖为魔容。"), false);
        player.displayClientMessage(Component.literal("§3附魔时将按 Tetra 原版魔力容量规则消耗魔容。"), false);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.consume(stack);
    }
}
