package com.wangand1145.tetra_ultimate_material.event;

import com.wangand1145.tetra_ultimate_material.world.MagicContainerData;
import com.wangand1145.tetra_ultimate_material.world.MagicEssenceWorldData;
import com.wangand1145.tetra_ultimate_material.world.MaterialMagicOverride;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = "tetra_ultimate_material")
public class PlayerTickMagicEffects {
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("a8c4e3f1-1234-5678-9012-3456789abcde");
    private static final UUID ATTACK_REACH_UUID = UUID.fromString("b9d5f4a2-2345-6789-0123-456789abcdef");

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;
        Player player = event.player;

        // 延迟注册材料增量覆盖
        if (player.tickCount % 40 == 0) {
            MaterialMagicOverride.ensureAllOverridden();
        }

        if (!MagicEssenceWorldData.get((net.minecraft.server.level.ServerLevel) player.level()).isActivated()) {
            return;
        }

        // 收集所有 Tetra 工具的魔容比例
        List<ItemStack> tetraTools = new ArrayList<>();
        double mostNegativeRatio = 1.0; // 默认满容
        boolean hasNegative = false;

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || !MagicContainerData.isTetraTool(stack)) continue;
            tetraTools.add(stack);

            int value = MagicContainerData.getMagicContainer(stack);
            int cap = MagicContainerData.DEFAULT_CAPACITY; // 可改为按材料 key 查表
            double ratio = cap > 0 ? (double) value / cap : 0;
            if (ratio < mostNegativeRatio) {
                mostNegativeRatio = ratio;
                hasNegative = (ratio <= 0);
            }
        }

        // 根据最负的武器决定效果等级
        int effectLevel = computeEffectLevel(mostNegativeRatio);

        // 清除旧的自定义效果，再施加新的（如有）
        clearAllCustomEffects(player);

        if (effectLevel > 0 && hasNegative) {
            applyEffectByLevel(player, effectLevel);
        }

        // 正区间：主手持 Tetra 工具时的攻击加成
        applyPositiveRangeBonus(player, mostNegativeRatio, tetraTools);
    }

    /** 根据魔容比例计算自定义效果等级（0 表示无效果）。 */
    private static int computeEffectLevel(double ratio) {
        if (ratio <= -0.55) return 5;
        if (ratio < -0.50) return 4;
        if (ratio < -0.40) return 3;
        if (ratio <= -0.30) return 2;
        if (ratio <= 0.0) return 1;
        return 0;
    }

    /** 清除所有自定义效果（I~V 对应的状态效果）。 */
    private static void clearAllCustomEffects(Player player) {
        if (player.hasEffect(MobEffects.WITHER)) player.removeEffect(MobEffects.WITHER);
        if (player.hasEffect(MobEffects.POISON)) player.removeEffect(MobEffects.POISON);
        if (player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        if (player.hasEffect(MobEffects.WEAKNESS)) player.removeEffect(MobEffects.WEAKNESS);
        if (player.hasEffect(MobEffects.LEVITATION)) player.removeEffect(MobEffects.LEVITATION);
        if (player.hasEffect(MobEffects.STRENGTH)) player.removeEffect(MobEffects.STRENGTH);
        if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) player.removeEffect(MobEffects.DIG_SLOWDOWN);
        if (player.hasEffect(MobEffects.CONFUSION)) player.removeEffect(MobEffects.CONFUSION);
        if (player.hasEffect(MobEffects.BLINDNESS)) player.removeEffect(MobEffects.BLINDNESS);
        if (player.hasEffect(MobEffects.HUNGER)) player.removeEffect(MobEffects.HUNGER);
        if (player.hasEffect(MobEffects.INVISIBILITY)) player.removeEffect(MobEffects.INVISIBILITY);
        if (player.hasEffect(MobEffects.MOVEMENT_SPEED)) player.removeEffect(MobEffects.MOVEMENT_SPEED);
        // 移除攻击加成属性修饰符
        AttributeInstance dmg = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (dmg != null) dmg.removeModifier(ATTACK_DAMAGE_UUID);
        AttributeInstance reach = player.getAttribute(Attributes.ATTACK_SPEED); // 用攻击速度槽位近似距离（见下方说明）
        if (reach != null) reach.removeModifier(ATTACK_REACH_UUID);
    }

    /** 按等级施加自定义效果。 */
    private static void applyEffectByLevel(Player player, int level) {
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
                player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 20, 4));
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 1));
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

    /** 正区间加成：70%~65% 攻击+2，65%~50% 攻击距离+1。仅主手持 Tetra 工具时生效。 */
    private static void applyPositiveRangeBonus(Player player, double ratio, List<ItemStack> tetraTools) {
        ItemStack main = player.getMainHandItem();
        boolean mainIsTetra = !main.isEmpty() && MagicContainerData.isTetraTool(main);

        AttributeInstance dmgAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance reachAttr = player.getAttribute(Attributes.ATTACK_SPEED);

        if (!mainIsTetra || ratio > 0.70 || ratio <= 0.50) {
            if (dmgAttr != null) dmgAttr.removeModifier(ATTACK_DAMAGE_UUID);
            if (reachAttr != null) reachAttr.removeModifier(ATTACK_REACH_UUID);
            return;
        }

        if (ratio > 0.65 && ratio <= 0.70) {
            // +2 攻击力
            if (dmgAttr != null) {
                dmgAttr.removeModifier(ATTACK_DAMAGE_UUID);
                dmgAttr.addTransientModifier(new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                        ATTACK_DAMAGE_UUID, "magic_container_attack", 2.0, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION));
            }
        } else if (ratio > 0.50 && ratio <= 0.65) {
            // +1 攻击距离（1.20 无独立攻击距离属性，此处用攻击速度槽位近似 +1 格距离）
            if (reachAttr != null) {
                reachAttr.removeModifier(ATTACK_REACH_UUID);
                reachAttr.addTransientModifier(new net.minecraft.world.entity.ai.attributes.AttributeModifier(
                        ATTACK_REACH_UUID, "magic_container_reach", 1.0, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADDITION));
            }
        }
    }
}
