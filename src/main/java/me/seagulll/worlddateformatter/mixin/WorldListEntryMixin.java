package me.seagulll.worlddateformatter.mixin;

import me.seagulll.worlddateformatter.WDFConfig;
import me.seagulll.worlddateformatter.WDFConfigManager;
import me.seagulll.worlddateformatter.WDFUtil;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mixin(WorldSelectionList.WorldListEntry.class)
public abstract class WorldListEntryMixin {
    @Shadow
    @Final
    private LevelSummary summary;

    @ModifyVariable(method = "<init>", at = @At(value = "STORE", ordinal = 1), name = "levelIdAndDate")
    private String modify(String levelIdAndDate) {
        WDFConfig config = WDFConfigManager.config;
        if (!config.isEnabled()) return levelIdAndDate;
        String string = summary.getLevelId();
        long lastPlayed = summary.getLastPlayed();
        if (lastPlayed != -1L) {
            ZonedDateTime lastPlayedTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(lastPlayed), ZoneId.systemDefault());
            String lastPlayedTimeText = WDFUtil.safeFormat(lastPlayedTime, config.getFormat(), config.getLocale())
                    .orElseGet(() -> WDFUtil.safeFormat(lastPlayedTime, WDFConfig.DEFAULT_FORMAT, WDFConfig.DEFAULT_LOCALE).orElse(""));
            if (!lastPlayedTimeText.isEmpty()) {
                string += " (" + lastPlayedTimeText + ")";
            }
        }
        return string;
    }
}
