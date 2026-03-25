package com.github.seagulll.worlddateformatter.integration;

import com.github.seagulll.worlddateformatter.Util;
import com.github.seagulll.worlddateformatter.WorlddateformatterConfig;
import com.github.seagulll.worlddateformatter.WorlddateformatterConfigManager;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ConfigScreenProvider {
    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(Text.translatable("text.worlddateformatter.config.title"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("text.worlddateformatter.config.category.general"));
        ZonedDateTime now = ZonedDateTime.now();

        general.addEntry(
                entryBuilder.startBooleanToggle(Text.translatable("text.worlddateformatter.config.option.isenabled"), WorlddateformatterConfigManager.config.isEnabled())
                        .setDefaultValue(true)
                        .setSaveConsumer(WorlddateformatterConfigManager.config::setEnabled)
                        .build()
        );
        general.addEntry(
                entryBuilder.startStrField(Text.translatable("text.worlddateformatter.config.option.format"), WorlddateformatterConfigManager.config.getFormat())
                        .setDefaultValue(WorlddateformatterConfig.DEFAULT_FORMAT)
                        .setTooltipSupplier(value -> {
                            String preview = Util.isValidFormat(value) ? now.format(DateTimeFormatter.ofPattern(value)) : "";
                            return Optional.of(new Text[]{ Text.translatable("text.worlddateformatter.config.option.format.tooltip", preview) });
                        })
                        .setSaveConsumer(WorlddateformatterConfigManager.config::setFormat)
                        .setErrorSupplier(value -> Util.isValidFormat(value) ? Optional.empty() : Optional.of(Text.translatable("text.worlddateformatter.config.option.format.error", WorlddateformatterConfig.DEFAULT_FORMAT)))
                        .build()
        );
        return builder.build();
    }
}