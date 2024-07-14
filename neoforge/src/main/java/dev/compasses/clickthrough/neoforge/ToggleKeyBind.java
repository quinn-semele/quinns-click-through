package dev.compasses.clickthrough.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;

public class ToggleKeyBind extends KeyMapping {
    private boolean modActive = true;

    public ToggleKeyBind(String description, IKeyConflictContext keyConflictContext, InputConstants.Type inputType, int keyCode, String category) {
        super(description, keyConflictContext, inputType, keyCode, category);
    }

    @Override
    public void setDown(boolean value) {
        super.setDown(value);

        if (value) {
            modActive = !modActive;

            if (modActive) {
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("text.quinnsclickthrough.enabled").withStyle(ChatFormatting.GREEN), true);
            } else {
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("text.quinnsclickthrough.disabled").withStyle(ChatFormatting.RED), true);
            }
        }
    }

    public boolean isModInactive() {
        return !modActive;
    }
}
