package dev.compasses.clickthrough.neoforge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.compasses.clickthrough.neoforge.ClickThrough;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class ClickThroughMixin {
    @Shadow @Nullable
    public LocalPlayer player;

    @Shadow @Nullable public ClientLevel level;

    @Shadow @Nullable public HitResult hitResult;

    @Shadow protected abstract void startUseItem();

    @WrapOperation(
            method = "startUseItem()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;useItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;")
    )
    private InteractionResult allowClickThroughBlock(MultiPlayerGameMode instance, LocalPlayer player, InteractionHand hand, BlockHitResult hit, Operation<InteractionResult> original) {
        if (player.isShiftKeyDown()) {
            return original.call(instance, player, hand, hit);
        }

        BlockPos supportingBlockPos = ClickThrough.canClickThroughBlock(level, hit);

        if (supportingBlockPos != null) {
            if (ClickThrough.shouldInteractWith(level, supportingBlockPos)) {
                return original.call(instance, player, hand, new BlockHitResult(Vec3.atCenterOf(supportingBlockPos), hit.getDirection(), supportingBlockPos, hit.isInside()));
            }
        }

        return original.call(instance, player, hand, hit);
    }

    @Inject(
            method = "startUseItem()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;interactAt(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/EntityHitResult;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"),
            cancellable = true
    )
    private void allowClickThroughEntity(CallbackInfo ci, @Local EntityHitResult hit) {
        if (player.isShiftKeyDown()) {
            return;
        }

        BlockPos supportingBlockPos = ClickThrough.canClickThroughEntity(level, player, hit);

        if (supportingBlockPos != null) {
            if (ClickThrough.shouldInteractWith(level, supportingBlockPos)) {
                float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
                var closest = level.getBlockState(supportingBlockPos).getShape(level, supportingBlockPos).move(supportingBlockPos.getX(), supportingBlockPos.getY(), supportingBlockPos.getZ()).closestPointTo(player.getEyePosition(partialTicks));

                hitResult = new BlockHitResult(closest.orElse(Vec3.atCenterOf(supportingBlockPos)), Direction.getNearest(player.getLookAngle()), supportingBlockPos, false);
                startUseItem();
                hitResult = hit;

                ci.cancel();
            }
        }
    }
}
