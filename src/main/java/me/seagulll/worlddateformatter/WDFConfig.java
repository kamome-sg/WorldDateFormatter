package me.seagulll.worlddateformatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class WDFConfig {
    public static final File FILE = FabricLoader.getInstance().getConfigDir().resolve(WDF.MOD_ID + ".json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static WDFConfig instance = new WDFConfig();
    private static long timestamp = 0;

    public static WDFConfig load() {
        if (FILE.exists()) {
            try (FileReader reader = new FileReader(FILE)) {
                long lastModified = FILE.lastModified();
                if (timestamp == 0 || timestamp != lastModified) {
                    WDFConfig newConfig = GSON.fromJson(reader, WDFConfig.class);
                    if (newConfig != null) {
                        instance = newConfig;
                    }
                    timestamp = lastModified;
                }
            } catch (Exception e) {
                WDF.LOGGER.error("Failed to load config", e);
            }
        } else {
            save();
        }
        return instance;
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            GSON.toJson(instance, writer);
            timestamp = FILE.lastModified();
        } catch (Exception e) {
            WDF.LOGGER.error("Failed to save config", e);
        }
    }


    public static final String DEFAULT_FORMAT = "yyyy/M/d H:mm";
    public static final String DEFAULT_LOCALE = "en_us";
    private boolean isEnabled = true;
    private String format = DEFAULT_FORMAT;
    private String locale = DEFAULT_LOCALE;

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getFormat() {
        return format;
    }

    public String getLocale() {
        return locale;
    }
}
