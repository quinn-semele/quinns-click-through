package dev.compasses.clickthrough.neoforge.config;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class BlockHolders implements Holders<BlockState, Block> {
    private final List<Holder<Block>> singlets = new ArrayList<>();
    private final List<HolderSet<Block>> plurals = new ArrayList<>();

    @Override
    public boolean contains(BlockState state) {
        for (Holder<Block> singlet : singlets) {
            if (state.is(singlet)) {
                return true;
            }
        }

        for (HolderSet<Block> plural : plurals) {
            if (state.is(plural)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void add(Holder<Block> holder) {
        singlets.add(holder);
    }

    @Override
    public void add(HolderSet<Block> holders) {
        plurals.add(holders);
    }

    @Override
    public void clear() {
        singlets.clear();
        plurals.clear();
    }
}
