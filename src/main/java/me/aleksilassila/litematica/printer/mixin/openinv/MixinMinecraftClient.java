//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.aleksilassila.litematica.printer.mixin.openinv;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.aleksilassila.litematica.printer.printer.Printer.isOpenHandler;
import static me.aleksilassila.litematica.printer.printer.zxy.ZxyUtils.adding;
import static me.aleksilassila.litematica.printer.printer.zxy.ZxyUtils.num;

@Environment(EnvType.CLIENT)
@Mixin({MinecraftClient.class})
public abstract class MixinMinecraftClient {
    public MixinMinecraftClient() {
    }
    @Inject(method = {"setScreen"}, at = {@At(value = "HEAD")})
    public void setScreen(@Nullable Screen newScreen, CallbackInfo ci) {
        if(isOpenHandler || num == 2 || num == 3 || adding){
            newScreen = null;
        }
    }
}
