package dev.compasses.clickthrough.neoforge.config;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;

public interface Holders<Input, Stored> {
    boolean contains(Input input);

    void add(Holder<Stored> holder);
    void add(HolderSet<Stored> holders);

    void clear();
}
