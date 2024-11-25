package me.aleksilassila.litematica.printer.config;

import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.minecraft.client.MinecraftClient;

//#if MC >= 12001
//#else
//$$ import net.minecraft.text.Text;
//$$ import net.minecraft.util.Identifier;
//$$ import me.aleksilassila.litematica.printer.printer.zxy.memory.MemoryDatabase;
//#endif


public class KeyCallbackHotkeys implements IHotkeyCallback {
    private final MinecraftClient client;

    public KeyCallbackHotkeys(MinecraftClient client) {
        this.client = client;
    }

    @Override
    public boolean onKeyAction(KeyAction action, IKeybind key) {
        if (this.client.player == null || this.client.world == null) return false;

        return false;
    }
}
