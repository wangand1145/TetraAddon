package com.wangand1145.tetra_ultimate_material.command;

import com.mojang.brigadier.CommandDispatcher;
import com.wangand1145.tetra_ultimate_material.data.MagicEssenceWorldData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "tetra_ultimate_material", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MagicEssenceCommand {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("magicessence")
            .requires(src -> src.hasPermission(2)) // op 权限
            .then(Commands.literal("off")
                .executes(ctx -> {
                    MagicEssenceWorldData data = MagicEssenceWorldData.get(
                        ctx.getSource().getLevel());
                    data.setActivated(false);
                    data.markDirty(); // 关键：触发写盘
                    ctx.getSource().sendSuccess(
                        () -> Component.literal("§a[魔容] 已关闭魔容模式"), false);
                    return 1;
                }))
            .then(Commands.literal("on")
                .executes(ctx -> {
                    MagicEssenceWorldData data = MagicEssenceWorldData.get(
                        ctx.getSource().getLevel());
                    data.setActivated(true);
                    data.markDirty();
                    ctx.getSource().sendSuccess(
                        () -> Component.literal("§c[魔容] 已开启魔容模式"), false);
                    return 1;
                }))
            .then(Commands.literal("status")
                .executes(ctx -> {
                    MagicEssenceWorldData data = MagicEssenceWorldData.get(
                        ctx.getSource().getLevel());
                    ctx.getSource().sendSuccess(
                        () -> Component.literal("§e[魔容] 当前状态: " +
                            (data.isActivated() ? "开启" : "关闭")), false);
                    return 1;
                }))
        );
    }
}

