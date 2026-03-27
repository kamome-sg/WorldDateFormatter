package com.github.seagulll.worlddateformatter.mixin;

import com.github.seagulll.worlddateformatter.Util;
import com.github.seagulll.worlddateformatter.WorlddateformatterConfig;
import com.github.seagulll.worlddateformatter.WorlddateformatterConfigManager;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Mixin(WorldSelectionList.WorldListEntry.class)
public abstract class WorldEntryMixin {
    private static WorlddateformatterConfig config = WorlddateformatterConfigManager.config;
    @Shadow
    @Final
    private LevelSummary summary;

    @ModifyVariable(
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Ljava/time/format/DateTimeFormatter;format(Ljava/time/temporal/TemporalAccessor;)Ljava/lang/String;")
            ),
            method = "<init>", at = @At("STORE"), name = "levelIdAndDate"
    )
    private String modify(String originalLevelIdAndDate) {
        if (!config.isEnabled()) return originalLevelIdAndDate;
        String levelIdAndDate = summary.getLevelId();
        long lastPlayed = summary.getLastPlayed();
        ZonedDateTime lastPlayedTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(lastPlayed), ZoneId.systemDefault());
        String lastPlayedTimeText = lastPlayedTime.format(DateTimeFormatter.ofPattern(Util.validateFormat(config.getFormat())));
        if (!lastPlayedTimeText.isEmpty()) {
            levelIdAndDate += " (" + lastPlayedTimeText + ")";
        }
        return levelIdAndDate;
    }
}
