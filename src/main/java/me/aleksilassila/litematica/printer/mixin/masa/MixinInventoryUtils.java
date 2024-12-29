package me.aleksilassila.litematica.printer.mixin.masa;


import fi.dy.masa.litematica.util.InventoryUtils;
import me.aleksilassila.litematica.printer.LitematicaMixinMod;
import me.aleksilassila.litematica.printer.printer.Printer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.aleksilassila.litematica.printer.printer.Printer.items2;

@Mixin(InventoryUtils.class)
public class MixinInventoryUtils {
    @Inject(at = @At("TAIL"),method = "schematicWorldPickBlock")
    private static void schematicWorldPickBlock(ItemStack stack, BlockPos pos, World schematicWorld, MinecraftClient mc, CallbackInfo ci){
//        System.out.println(cir.getReturnValue().booleanValue());
        if (mc.player != null && !ItemStack.areItemsAndComponentsEqual(mc.player.getMainHandStack(),stack) && (LitematicaMixinMod.INVENTORY.getBooleanValue() || LitematicaMixinMod.QUICKSHULKER.getBooleanValue())) {
            items2.add(stack.getItem());
            Printer.getPrinter().switchItem();
        }
    }
}