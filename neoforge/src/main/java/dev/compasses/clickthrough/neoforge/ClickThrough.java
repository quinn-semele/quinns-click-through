package dev.compasses.clickthrough.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import dev.compasses.clickthrough.neoforge.config.Config;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import org.slf4j.Logger;

import static dev.compasses.clickthrough.neoforge.ClickThrough.MOD_ID;

@Mod(value = MOD_ID, dist = Dist.CLIENT)
public class ClickThrough {
    public static final String MOD_ID = "quinnsclickthrough";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Lazy<ToggleKeyBind> TOGGLE_MOD_KEY = Lazy.of(() -> new ToggleKeyBind("key.quinnsclickthrough.toggle", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_PERIOD, "key.categories.gameplay"));

    public ClickThrough(IEventBus bus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);

        bus.addListener(this::registerKeyBinds);
        bus.addListener(this::onConfigLoaded);
        NeoForge.EVENT_BUS.addListener(this::onTagsUpdated);
    }

    private void registerKeyBinds(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_MOD_KEY.get());
    }

    private void onConfigLoaded(final ModConfigEvent event) {
        Config.loadConfig();
    }

    private void onTagsUpdated(final TagsUpdatedEvent event) {
        if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED) {
            Config.loadConfig();
        }
    }

    public static BlockPos canClickThroughBlock(final ClientLevel level, final BlockPos pos) {
        if (TOGGLE_MOD_KEY.get().isModInactive()) {
            return null;
        }

        BlockState state = level.getBlockState(pos);

        if ((state.is(BlockTags.WALL_SIGNS) || state.getBlock() instanceof SignBlock) && state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            return pos.relative(facing.getOpposite());
        }

        return null;
    }

    public static BlockPos canClickThroughEntity(final ClientLevel level, final LocalPlayer player, final EntityHitResult hit) {
        if (TOGGLE_MOD_KEY.get().isModInactive()) {
            return null;
        }

        if (Config.includeItemFrames && hit.getEntity() instanceof ItemFrame frame) {
            return frame.getPos().relative(frame.getDirection().getOpposite());
        }

        Entity entity = hit.getEntity();
        if (Config.clickThroughEntities.contains(entity)) {
            return entity.blockPosition().relative(entity.getDirection().getOpposite());
        }

        return null;
    }

    public static boolean shouldInteractWith(final ClientLevel level, final BlockPos pos) {
//        BlockState state = level.getBlockState(pos);

//        if (Config.includeMenus) {
//            if (state.getMenuProvider(level, pos) != null) {
//                return true;
//            }
//
//            BlockEntity entity = level.getBlockEntity(pos);
//
//            if (entity instanceof Container) {
//                return true;
//            }
//        }
//
//        if (Config.interactableBlocks.contains(state)) {
//            return true;
//        }

        return true;
    }
}
