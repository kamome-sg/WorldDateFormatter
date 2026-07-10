package me.seagulll.worlddateformatter.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.seagulll.worlddateformatter.WDFConfig;
import me.seagulll.worlddateformatter.WDFUtil;
import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import java.time.ZonedDateTime;

@Mixin(WorldSelectionList.WorldListEntry.class)
public abstract class WorldListEntryMixin {
    @Shadow
    @Final
    /*? if >=26.1 >>*/private LevelSummary summary;

    @ModifyVariable(
            method = "<init>", at = @At(value = "STORE", ordinal = 1),
            /*? if >=26.1 {*/name = "levelIdAndDate"/*?} else {*//*ordinal = 0*//*?}*/
    )
    private String modifyLevelIdAndDate(
            String levelIdAndDate,
            @Local(/*? if >=26.1 {*/name = "lastPlayed"/*?} else {*//*ordinal = 0*//*?}*/) long lastPlayed,
            @Local(/*? if >=26.1 {*/name = "lastPlayedTime"/*?} else {*//*ordinal = 0*//*?}*/) ZonedDateTime lastPlayedTime
    ) {
        WDFConfig config = WDFConfig.load();
        if (!config.isEnabled()) return levelIdAndDate;
        String string = summary.getLevelId();
        if (lastPlayed != -1L) {
            String lastPlayedTimeText = WDFUtil.safeFormat(lastPlayedTime, config.getFormat(), config.getLocale())
                    .orElseGet(() -> WDFUtil.safeFormat(lastPlayedTime, WDFConfig.DEFAULT_FORMAT, WDFConfig.DEFAULT_LOCALE).orElse(""));
            if (!lastPlayedTimeText.isEmpty()) {
                string += " (" + lastPlayedTimeText + ")";
            }
        }
        return string;
    }
}
