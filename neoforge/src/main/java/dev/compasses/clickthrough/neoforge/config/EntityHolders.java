package dev.compasses.clickthrough.neoforge.config;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class EntityHolders implements Holders<Entity, EntityType<?>> {
    private final List<Holder<EntityType<?>>> singlets = new ArrayList<>();
    private final List<HolderSet<EntityType<?>>> plurals = new ArrayList<>();

    @Override
    public boolean contains(Entity entity) {
        for (Holder<EntityType<?>> singlet : singlets) {
            if (singlet.value().equals(entity.getType())) {
                return true;
            }
        }

        for (HolderSet<EntityType<?>> plurals : plurals) {
            if (plurals.contains(entity.getType().builtInRegistryHolder())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void add(Holder<EntityType<?>> holder) {
        singlets.add(holder);
    }

    @Override
    public void add(HolderSet<EntityType<?>> holders) {
        plurals.add(holders);
    }

    @Override
    public void clear() {
        singlets.clear();
        plurals.clear();
    }
}
