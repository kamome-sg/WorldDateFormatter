package me.seagulll.worlddateformatter.integration;

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
import java.util.List;

public class ConfigScreenProvider {
    public static Screen create(Screen parent) {
        WDFConfig config = WDFConfigManager.config;
        String defaultFormat = WDFConfig.DEFAULT_FORMAT;
        String defaultLocale = WDFConfig.DEFAULT_LOCALE;
        List<String> locales = Minecraft.getInstance().getLanguageManager().getLanguages().keySet().stream().toList();
        ZonedDateTime now = ZonedDateTime.now();

        Option<String> locale = Option.<String>createBuilder()
                .name(Component.translatable("text.worlddateformatter.config.option.locale"))
                .description(OptionDescription.of(Component.translatable("text.worlddateformatter.config.option.locale.tooltip")))
                .controller(option -> DropdownStringControllerBuilder.create(option).values(locales))
                .binding(defaultLocale, config::getLocale, config::setLocale)
                .build();
        Option<String> format = Option.<String>createBuilder()
                .name(Component.translatable("text.worlddateformatter.config.option.format"))
                .description(value -> OptionDescription.of(WDFUtil.isValidFormat(value)
                        ? Component.translatable("text.worlddateformatter.config.option.format.tooltip", now.format(WDFUtil.getFormatter(value, locale.pendingValue())))
                        : Component.translatable("text.worlddateformatter.config.option.format.error", defaultFormat).withStyle(ChatFormatting.RED)))
                .controller(StringControllerBuilder::create)
                .binding(defaultFormat, config::getFormat, config::setFormat)
                .build();
        Option<Boolean> isEnabled = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.worlddateformatter.config.option.isenabled"))
                .controller(TickBoxControllerBuilder::create)
                .binding(true, config::isEnabled, config::setEnabled)
                .addListener((option, event) -> {
                    String pendingFormat = format.pendingValue();
                    format.setAvailable(option.pendingValue());
                    format.requestSet(pendingFormat);
                    String pendingLocale = locale.pendingValue();
                    locale.setAvailable(option.pendingValue());
                    locale.requestSet(pendingLocale);
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