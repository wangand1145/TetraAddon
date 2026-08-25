package com.wangand1145.tetra_ultimate_material.event;

import com.wangand1145.tetra_ultimate_material.world.MagicEssenceWorldData;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 魔容源质右键事件。
 * 仅确保激活标记已设置（主要逻辑在 MagicEssenceItem.use 中）。
 * 不调用任何材料覆盖逻辑——魔容 = 魔法容量，数值不变。
 */
@Mod.EventBusSubscriber(modid = "tetra_ultimate_material")
public class MagicEssenceEvents {

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getItemStack().getItem() instanceof com.wangand1145.tetra_ultimate_material.item.MagicEssenceItem) {
            // 激活逻辑已在 MagicEssenceItem.use() 中处理（设置 MagicEssenceWorldData 标记）。
            // 此处无需额外操作。
        }
    }
}
