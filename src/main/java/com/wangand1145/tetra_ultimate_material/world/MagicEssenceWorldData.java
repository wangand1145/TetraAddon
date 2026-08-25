package com.wangand1145.tetra_ultimate_material.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class MagicEssenceWorldData extends SavedData {
    private static final String DATA_NAME = "tetra_ultimate_material_magic_essence";
    private static final String KEY_ACTIVATED = "activated";

    private boolean activated = false;

    public static MagicEssenceWorldData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                MagicEssenceWorldData::load,
                MagicEssenceWorldData::new,
                DATA_NAME);
    }

    private MagicEssenceWorldData() {}

    private static MagicEssenceWorldData load(CompoundTag tag) {
        MagicEssenceWorldData data = new MagicEssenceWorldData();
        data.activated = tag.getBoolean(KEY_ACTIVATED);
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean(KEY_ACTIVATED, activated);
        return tag;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
        setDirty();
    }
}

