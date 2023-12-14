package me.aleksilassila.litematica.printer.mixin.jackf;

import me.aleksilassila.litematica.printer.printer.zxy.Utils.PinYinSearch;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import red.jackf.chesttracker.gui.util.SearchablesUtil;

import java.awt.*;

import static net.minecraft.client.item.TooltipContext.ADVANCED;

@Mixin(SearchablesUtil.class)
public class SearchablesUtilMixin {



    @Inject(at = @At(value = "INVOKE",target = "Ljava/util/stream/Stream;anyMatch(Ljava/util/function/Predicate;)Z"),method = "stackEnchantmentFilter", cancellable = true)
    private static void stackEnchantmentFilter(ItemStack stack, String filter, CallbackInfoReturnable<Boolean> cir){
        var enchantments = EnchantmentHelper.get(stack);
        if (enchantments.isEmpty()) return ;
        if(
                enchantments.keySet().stream()
                .anyMatch(ench -> {
                    if (testLang(ench.getTranslationKey(), filter)) return true;
                    var resloc = Registries.ENCHANTMENT.getKey(ench);
                    return resloc != null && PinYinSearch.getPinYin(resloc.toString()).stream().anyMatch(s -> s.contains(filter));
                })
        )
            cir.setReturnValue(true);
    }
    @Shadow(remap = false) private static boolean testLang(String key, String filter) {
        return false;
    }

    @Inject(at = @At(value = "INVOKE",target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"),method = "stackPotionFilter", cancellable = true)
    private static void stackPotionFilter(ItemStack stack, String filter, CallbackInfoReturnable<Boolean> cir){
        Potion potion = PotionUtil.getPotion(stack);
        if (potion != Potions.EMPTY) {
            var langKey = potion.finishTranslationKey(stack.getTranslationKey() + ".effect.");
            if (testLang(langKey, filter)) return;
            var resloc = Registries.POTION.getKey(potion);
            //noinspection ConstantValue
            if (resloc != null && PinYinSearch.getPinYin(resloc.toString()).stream().anyMatch(s -> s.contains(filter))) cir.setReturnValue(true);
        }
        // specific effects
        var effects = PotionUtil.getPotionEffects(stack);
        for (StatusEffectInstance effect : effects) {
            var langKey = effect.getTranslationKey();
            if (testLang(langKey, filter)) return;
            var resloc = Registries.STATUS_EFFECT.getKey(effect.getEffectType());
            if (resloc != null && PinYinSearch.getPinYin(resloc.toString()).stream().anyMatch(s -> s.contains(filter))) cir.setReturnValue(true);
        }
    }
    @Inject(at = @At("HEAD"),method = "stackTagFilter", cancellable = true)
    private static void stackTagFilter(ItemStack stack, String filter, CallbackInfoReturnable<Boolean> cir){
        if(stack.getRegistryEntry().streamTags().anyMatch(tag ->
                PinYinSearch.getPinYin(tag.id().getPath()).stream().anyMatch(s -> s.contains(filter))))
            cir.setReturnValue(true);
    }
    @Inject(at = @At("HEAD"),method = "stackTooltipFilter", cancellable = true)
    private static void stackTooltipFilter(ItemStack stack, String filter, CallbackInfoReturnable<Boolean> cir){
        var player = MinecraftClient.getInstance().player;
        if (player == null) cir.setReturnValue(false);
        var advanced = MinecraftClient.getInstance().options.advancedItemTooltips ?  ADVANCED : TooltipContext.BASIC;
        for (Text line : stack.getTooltip(player, advanced)) {
            if (line.getString().toLowerCase().contains(filter)) cir.setReturnValue(true);
        }
    }
    @Inject(at = @At("HEAD"),method = "testLang", cancellable = true,remap = false)
    private static void testLang(String key, String filter, CallbackInfoReturnable<Boolean> cir){
        if(Language.getInstance().hasTranslation(key) &&
            PinYinSearch.getPinYin(Language.getInstance().get(key).toLowerCase()).stream().anyMatch(s -> s.contains(filter)))
            cir.setReturnValue(true);
    }
    @Inject(at = @At("HEAD"),method = "stackNameFilter", cancellable = true)
    private static void stackNameFilter(ItemStack stack, String filter, CallbackInfoReturnable<Boolean> cir){
        boolean b = PinYinSearch.getPinYin(stack.getName().getString()).stream().anyMatch(s -> s.contains(filter));
        if(b) cir.setReturnValue(true);
    }
    @Inject(at = @At("HEAD"),method = "anyTextFilter", cancellable = true)
    private static void anyTextFilter(ItemStack stack, String filter, CallbackInfoReturnable<Boolean> cir){
//        System.out.println(filter);
//        cir.setReturnValue(true);
    }
}