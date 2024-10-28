package dev.compasses.clickthrough.neoforge.config;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JavaOps;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Optional;

public class Config {
//    public static BlockHolders interactableBlocks = new BlockHolders();
    public static BlockHolders clickThroughBlocks = new BlockHolders();
    public static EntityHolders clickThroughEntities = new EntityHolders();
//    public static boolean includeMenus = false;
    public static boolean includeSigns = false;
    public static boolean includeItemFrames = false;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

//    public static final ModConfigSpec.ConfigValue<List<? extends String>> INTERACTABLE_BLOCKS = BUILDER
//            .comment("A list of blocks or tags to allow the user to interact with when clicking on a sign or a item frame")
//            .defineListAllowEmpty("interactable_blocks", List.of(ResourceLocation.withDefaultNamespace("composter").toString()), string -> true);

//    public static final ModConfigSpec.BooleanValue INCLUDE_MENUS = BUILDER
//            .comment("Should blocks that have guis all be considered interactable when behind a sign?")
//            .define("include_menus", true);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> CLICK_THROUGH_BLOCKS = BUILDER
            .comment("A list of blocks or tags that can be clicked through.")
            .defineListAllowEmpty("click_through_blocks", List.of("framedblocks:framed_item_frame", "framedblocks:framed_glowing_item_frame"), string -> true);

    public static final ModConfigSpec.BooleanValue INCLUDE_SIGNS = BUILDER
            .comment("Should all sign blocks be allowed to be clicked through?")
            .define("include_signs", true);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> CLICK_THROUGH_ENTITIES = BUILDER
            .comment("A list of entities or tags that can be clicked through.")
            .defineListAllowEmpty("click_through_entities", List.of(), string -> true);

    public static final ModConfigSpec.BooleanValue INCLUDE_ITEM_FRAMES = BUILDER
            .comment("Should all item frames be allowed to be clicked through?")
            .define("include_item_frames", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static void loadConfig() {
//        includeMenus = INCLUDE_MENUS.getAsBoolean();
        includeSigns = INCLUDE_SIGNS.getAsBoolean();
        includeItemFrames = INCLUDE_ITEM_FRAMES.getAsBoolean();

//        /* Interactable blocks */ {
//            interactableBlocks.clear();
//            var elements = ExtraCodecs.TAG_OR_ELEMENT_ID.listOf().parse(new Dynamic<>(JavaOps.INSTANCE, INTERACTABLE_BLOCKS.get())).getOrThrow();
//            for (ExtraCodecs.TagOrElementLocation element : elements) {
//                if (element.tag()) {
//                    Optional<HolderSet.Named<Block>> tag = BuiltInRegistries.BLOCK.getTag(TagKey.create(Registries.BLOCK, element.id()));
//
//                    tag.ifPresent(interactableBlocks::add);
//                } else {
//                    BuiltInRegistries.BLOCK.getHolder(element.id()).ifPresent(interactableBlocks::add);
//                }
//            }
//        }

        /* Click through blocks */ {
            clickThroughBlocks.clear();
            var elements = ExtraCodecs.TAG_OR_ELEMENT_ID.listOf().parse(new Dynamic<>(JavaOps.INSTANCE, CLICK_THROUGH_BLOCKS.get())).getOrThrow();
            for (ExtraCodecs.TagOrElementLocation element : elements) {
                if (element.tag()) {
                    Optional<HolderSet.Named<Block>> tag = BuiltInRegistries.BLOCK.get(TagKey.create(Registries.BLOCK, element.id()));

                    tag.ifPresent(clickThroughBlocks::add);
                } else {
                    BuiltInRegistries.BLOCK.get(element.id()).ifPresent(clickThroughBlocks::add);
                }
            }
        }

        /* Click through entities */ {
            clickThroughEntities.clear();
            var elements = ExtraCodecs.TAG_OR_ELEMENT_ID.listOf().parse(new Dynamic<>(JavaOps.INSTANCE, CLICK_THROUGH_ENTITIES.get())).getOrThrow();
            for (ExtraCodecs.TagOrElementLocation element : elements) {
                if (element.tag()) {
                    Optional<HolderSet.Named<EntityType<?>>> tag = BuiltInRegistries.ENTITY_TYPE.get(TagKey.create(Registries.ENTITY_TYPE, element.id()));

                    tag.ifPresent(clickThroughEntities::add);
                } else {
                    BuiltInRegistries.ENTITY_TYPE.get(element.id()).ifPresent(clickThroughEntities::add);
                }
            }
        }
    }
}
