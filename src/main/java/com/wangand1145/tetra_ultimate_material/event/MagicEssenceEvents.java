package com.wangand1145.tetra_ultimate_material.event;

import com.wangand1145.tetra_ultimate_material.world.MagicEssenceWorldData;
import com.wangand1145.tetra_ultimate_material.world.MaterialMagicOverride;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "tetra_ultimate_material")
public class MagicEssenceEvents {

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getLevel().isClientSide()) return;
        if (!event.getItemStack().is(net.minecraft.world.item.Items.AIR) &&
            event.getItemStack().getItem() instanceof com.wangand1145.tetra_ultimate_material.item.MagicEssenceItem) {
            // 激活逻辑在 MagicEssenceItem.use() 中已处理；此处确保覆盖材料。
            MagicEssenceWorldData data = MagicEssenceWorldData.get((net.minecraft.server.level.ServerLevel) event.getLevel());
            if (data.isActivated()) {
                MaterialMagicOverride.applyOverrideToAll();
            }
        }
    }
}

