    package me.seagulll.worlddateformatter;

    import com.google.gson.Gson;
    import com.google.gson.GsonBuilder;
    import net.fabricmc.loader.api.FabricLoader;

    import java.io.File;
    import java.io.FileReader;
    import java.io.FileWriter;

    public class WDFConfigManager {
        private static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), WDF.MOD_ID + ".json");
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        private static WDFConfig config = new WDFConfig();
        private static long timestamp = 0;

        public static WDFConfig load() {
            if (FILE.exists()) {
                try (FileReader reader = new FileReader(FILE)) {
                    long lastModified = FILE.lastModified();
                    if (timestamp == 0 || timestamp != lastModified) {
                        WDFConfig newConfig = GSON.fromJson(reader, WDFConfig.class);
                        if (newConfig != null) {
                            config = newConfig;
                        }
                        timestamp = lastModified;
                    }
                }
                catch (Exception e) {
                    WDF.LOGGER.error("Failed to load config", e);
                }
            }
            else {
                save();
            }
            return config;
        }

        public static void save() {
            try (FileWriter writer = new FileWriter(FILE)) {
                GSON.toJson(config, writer);
                timestamp = FILE.lastModified();
            } catch (Exception e) {
                WDF.LOGGER.error("Failed to save config", e);
            }
        }
    }