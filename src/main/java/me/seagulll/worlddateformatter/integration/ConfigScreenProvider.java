package me.seagulll.worlddateformatter.integration;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import me.seagulll.worlddateformatter.WDFConfig;
import me.seagulll.worlddateformatter.WDFConfigManager;
import me.seagulll.worlddateformatter.WDFUtil;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.DropdownStringControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConfigScreenProvider {
    public static Screen create(Screen parent) {
        WDFConfig config = WDFConfigManager.config;
        String defaultFormat = WDFConfig.DEFAULT_FORMAT;
        String defaultLocale = WDFConfig.DEFAULT_LOCALE;
        BiMap<String, String> localeMap = HashBiMap.create(Minecraft.getInstance().getLanguageManager().getLanguages().entrySet().stream()
                .filter(entry -> WDFUtil.codeToLocale(entry.getKey()).isPresent())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getKey() + " / " + entry.getValue().toComponent().getString()
                )));
        BiMap<String, String> inverseLocaleMap = localeMap.inverse();
        ZonedDateTime now = ZonedDateTime.now();

        Option<String> locale = Option.<String>createBuilder()
                .name(Component.translatable("text.worlddateformatter.config.option.locale"))
                .description(OptionDescription.of(Component.translatable("text.worlddateformatter.config.option.locale.tooltip")))
                .controller(option -> DropdownStringControllerBuilder.create(option).values(localeMap.values().stream().toList()))
                .binding(localeMap.getOrDefault(defaultLocale, defaultLocale),
                        () -> Optional.ofNullable(localeMap.get(config.getLocale())).orElseGet(config::getLocale),
                        value -> config.setLocale(inverseLocaleMap.getOrDefault(value, defaultLocale)))
                .build();
        Option<String> format = Option.<String>createBuilder()
                .name(Component.translatable("text.worlddateformatter.config.option.format"))
                .description(value -> OptionDescription.of(WDFUtil.safeFormat(now, value, inverseLocaleMap.getOrDefault(locale.pendingValue(), locale.pendingValue()))
                        .map(formatted -> Component.translatable("text.worlddateformatter.config.option.format.tooltip", formatted))
                        .orElseGet(() -> Component.translatable("text.worlddateformatter.config.option.format.error", defaultFormat).withStyle(ChatFormatting.RED))))
                .controller(StringControllerBuilder::create)
                .binding(defaultFormat, config::getFormat, config::setFormat)
                .build();
        Option<Boolean> isEnabled = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.worlddateformatter.config.option.isenabled"))
                .controller(TickBoxControllerBuilder::create)
                .binding(true, config::isEnabled, config::setEnabled)
                .addListener((option, event) -> {
                    WDFUtil.syncAvailability(format, option.pendingValue());
                    WDFUtil.syncAvailability(locale, option.pendingValue());
                })
                .build();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("text.worlddateformatter.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("text.worlddateformatter.config.category.general"))
                        .option(isEnabled)
                        .option(format)
                        .option(locale)
                        .build())
                .save(WDFConfigManager::save)
                .build()
                .generateScreen(parent);
    }
}