package com.wangand1145.tetra_ultimate_material.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class MagicEssenceWorldData extends SavedData {
    private static final String ID = "tetra_ultimate_material_magic_essence";
    public boolean activated = false;
    public static boolean clientActivated = false;

    public static MagicEssenceWorldData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
            MagicEssenceWorldData::load,
            MagicEssenceWorldData::new,
            ID);
    }

    public static MagicEssenceWorldData load(net.minecraft.nbt.CompoundTag tag) {
        MagicEssenceWorldData data = new MagicEssenceWorldData();
        data.activated = tag.getBoolean("activated");
        clientActivated = data.activated;
        return data;
    }

    @Override
    public net.minecraft.nbt.CompoundTag save(net.minecraft.nbt.CompoundTag tag) {
        tag.putBoolean("activated", activated);
        return tag;
    }

    public void setActivated(boolean value) {
        this.activated = value;
        clientActivated = value;
        setDirty();
    }
    public boolean isActivated() {
    return this.activated;
    }
}
