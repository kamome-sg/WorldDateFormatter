package me.seagulll.worlddateformatter.integration;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import me.seagulll.worlddateformatter.WDFConfig;
import me.seagulll.worlddateformatter.WDFUtil;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.DropdownStringControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import me.seagulll.worlddateformatter.mixin.OptionImplAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfigScreenProvider {
    public static Screen create(Screen parent) {
        WDFConfig config = WDFConfig.load();
        BiMap<String, String> localeToName = HashBiMap.create(Minecraft.getInstance().getLanguageManager().getLanguages().entrySet().stream()
                .filter(entry -> WDFUtil.codeToLocale(entry.getKey()).isPresent())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getKey() + " / " + entry.getValue().toComponent().getString()
                )));
        BiMap<String, String> nameToLocale = localeToName.inverse();
        ZonedDateTime now = ZonedDateTime.now();

        AtomicReference<String> localeRef = new AtomicReference<>("");
        Function<String, OptionDescription> formatDescriptionFunction = value -> OptionDescription.of(
                WDFUtil.safeFormat(now, value, localeRef.get())
                        .map(formatted -> Component.translatable("worlddateformatter.config.option.format.description", formatted))
                        .orElseGet(() -> Component.translatable("worlddateformatter.config.option.format.error", WDFConfig.DEFAULT_FORMAT).withStyle(ChatFormatting.RED)));

        Option<String> format = Option.<String>createBuilder()
                .name(Component.translatable("worlddateformatter.config.option.format"))
                .description(formatDescriptionFunction)
                .controller(StringControllerBuilder::create)
                .binding(WDFConfig.DEFAULT_FORMAT, config::getFormat, config::setFormat)
                .build();
        Option<String> locale = Option.<String>createBuilder()
                .name(Component.translatable("worlddateformatter.config.option.locale"))
                .description(OptionDescription.of(Component.translatable("worlddateformatter.config.option.locale.description")))
                .controller(option -> DropdownStringControllerBuilder.create(option).values(localeToName.values().stream().toList()))
                .binding(localeToName.getOrDefault(WDFConfig.DEFAULT_LOCALE, WDFConfig.DEFAULT_LOCALE),
                        () -> Optional.ofNullable(localeToName.get(config.getLocale())).orElseGet(config::getLocale),
                        value -> config.setLocale(nameToLocale.getOrDefault(value, WDFConfig.DEFAULT_LOCALE)))
                .addListener(((option, event) -> {
                    localeRef.set(nameToLocale.getOrDefault(option.pendingValue(), option.pendingValue()));
                    ((OptionImplAccessor) format).setDescription(formatDescriptionFunction.apply(format.pendingValue()));
                }))
                .build();
        Option<Boolean> isEnabled = Option.<Boolean>createBuilder()
                .name(Component.translatable("worlddateformatter.config.option.isenabled"))
                .controller(TickBoxControllerBuilder::create)
                .binding(true, config::isEnabled, config::setEnabled)
                .addListener((option, event) -> {
                    syncAvailability(format, option);
                    syncAvailability(locale, option);
                })
                .build();

        ButtonOption openConfig = ButtonOption.createBuilder()
                .name(Component.translatable("worlddateformatter.config.option.openconfig"))
                .text(Component.empty())
                .description(OptionDescription.of(Component.translatable("worlddateformatter.config.option.openconfig.description")))
                .action((yaclScreen, option) -> Util.getPlatform().openFile(WDFConfig.FILE))
                .build();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("worlddateformatter.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("worlddateformatter.config.category.general"))
                        .option(isEnabled)
                        .option(format)
                        .option(locale)
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("worlddateformatter.config.category.misc"))
                        .option(openConfig)
                        .build())
                .save(WDFConfig::save)
                .build()
                .generateScreen(parent);
    }

    private static <T> void syncAvailability(Option<T> option, Option<Boolean> available) {
        T pending = option.pendingValue();
        option.setAvailable(available.pendingValue());
        option.requestSet(pending);
    }
}