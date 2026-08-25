package com.wangand1145.tetra_ultimate_material.event;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.wangand1145.tetra_ultimate_material.world.MagicContainerData;
import com.wangand1145.tetra_ultimate_material.world.MaterialMagicOverride;

@Mod.EventBusSubscriber(modid = "tetra_ultimate_material")
public class PlayerTickMagicEffects {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide()) return;

        // 延迟注册材料增量覆盖（每 40 tick）
        if (player.tickCount % 40 == 0) {
            MaterialMagicOverride.ensureAllOverridden();
        }

        // 效果判定
        handleEffects(player);
    }

    private static void handleEffects(Player player) {
        int worstLevel = 0; // 0=无效果，1~5=效果等级
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || !MagicContainerData.isTetraTool(stack)) continue;
            int value = MagicContainerData.getMagicContainer(stack);
            int cap = MagicContainerData.getMaxMagicContainer(stack);
            if (cap <= 0) continue;
            double ratio = (double) value / cap;
            int lvl = computeEffectLevel(ratio, i < 9); // 快捷栏索引 0~8
            if (lvl > worstLevel) worstLevel = lvl;
        }

        applyEffectLevel(player, worstLevel);
    }

    /** 根据魔容比例计算效果等级（0=无） */
    static int computeEffectLevel(double ratio, boolean inHotbar) {
        if (ratio > 0.70) return 0;
        if (ratio > 0.65) return 0; // 正区间：攻击+2（属性修饰符，见下方 apply）
        if (ratio > 0.50) return 0; // 正区间：距离+1
        if (ratio > 0) return 0;
        if (ratio > -0.30) return inHotbar ? 1 : 0;
        if (ratio > -0.40) return 2;
        if (ratio > -0.50) return 3;
        if (ratio > -0.55) return 4;
        return 5;
    }

    private static void applyEffectLevel(Player player, int level) {
        // 清除旧效果
        removeEffect(player, MobEffects.WITHER);
        removeEffect(player, MobEffects.POISON);
        removeEffect(player, MobEffects.MOVEMENT_SLOWDOWN);
        removeEffect(player, MobEffects.WEAKNESS);
        removeEffect(player, MobEffects.DAMAGE_BOOST); // 力量（伤害加成）
        removeEffect(player, MobEffects.DIG_SPEED);
        removeEffect(player, MobEffects.LEVITATION);
        removeEffect(player, MobEffects.CONFUSION);
        removeEffect(player, MobEffects.BLINDNESS);
        removeEffect(player, MobEffects.HUNGER);
        removeEffect(player, MobEffects.INVISIBILITY);
        removeEffect(player, MobEffects.MOVEMENT_SPEED); // 急迫? 用 SPEED 近似
        removeEffect(player, MobEffects.HEAL); // 漂浮? 无直接对应，用 LEVITATION 已处理

        if (level == 0) return;

        switch (level) {
            case 1:
                player.getFoodData().addExhaustion(5.0f);
                player.hurt(player.damageSources().magic(), player.getMaxHealth() * 0.05f);
                break;
            case 2:
                player.getFoodData().addExhaustion(5.0f);
                player.hurt(player.damageSources().magic(), player.getMaxHealth() * 0.10f);
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 0));
                break;
            case 3:
                player.getFoodData().addExhaustion(5.0f);
                player.hurt(player.damageSources().magic(), player.getMaxHealth() * 0.20f);
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 1));
                break;
            case 4:
                player.hurt(player.damageSources().magic(), player.getMaxHealth() * 0.15f);
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, 2));
                player.addEffect(new MobEffectInstance(MobEffects.WITHER, 20, 4));
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 20, 4));
                player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 4));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 1));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20, 0));
                player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20, 0));
                break;
            case 5:
                player.kill();
                break;
        }
    }

    private static void removeEffect(Player player, net.minecraft.world.effect.MobEffect effect) {
        if (player.hasEffect(effect)) player.removeEffect(effect);
    }
}

