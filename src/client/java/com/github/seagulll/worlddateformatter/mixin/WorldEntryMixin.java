package com.github.seagulll.worlddateformatter.mixin;

import com.github.seagulll.worlddateformatter.Util;
import com.github.seagulll.worlddateformatter.WorlddateformatterConfigManager;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.text.Text;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Mixin(WorldListWidget.WorldEntry.class)
public abstract class WorldEntryMixin {
    @Shadow @Final LevelSummary level;

    @ModifyVariable(method = "<init>", at = @At("STORE"), ordinal = 1)
    // コンストラクタ内のtext2を書き換え
    private Text modifyText2(Text originalText2) {
        if (!WorlddateformatterConfigManager.config.isEnabled()) return originalText2;
        String string = level.getName();
        long l = level.getLastPlayed();
        if (l != -1L) {
            ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault());
            String dateTimeText = dateTime.format(DateTimeFormatter.ofPattern(Util.validateFormat(WorlddateformatterConfigManager.config.getFormat())));
            if (!dateTimeText.isEmpty()) {
                string += " (" + dateTimeText + ")";
            }
        }
        return Text.literal(string).withColor(-8355712);
    }
}
