package com.github.seagulll.worlddateformatter.integration;

import com.github.seagulll.worlddateformatter.Util;
import com.github.seagulll.worlddateformatter.WorlddateformatterConfig;
import com.github.seagulll.worlddateformatter.WorlddateformatterConfigManager;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ConfigScreenProvider {
    public static Screen create(Screen parent) {
        WorlddateformatterConfig config = WorlddateformatterConfigManager.config;
        String defaultFormat = WorlddateformatterConfig.DEFAULT_FORMAT;
        ZonedDateTime now = ZonedDateTime.now();

        Option<String> format = Option.<String>createBuilder()
                .name(Component.translatable("text.worlddateformatter.config.option.format"))
                .description(value -> OptionDescription.of(Util.isValidFormat(value)
                        ? Component.translatable("text.worlddateformatter.config.option.format.tooltip", now.format(DateTimeFormatter.ofPattern(value)))
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
                })
                .build();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("text.worlddateformatter.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("text.worlddateformatter.config.category.general"))
                        .option(isEnabled)
                        .option(format)
                        .build())
                .save(WorlddateformatterConfigManager::save)
                .build()
                .generateScreen(parent);
    }
}