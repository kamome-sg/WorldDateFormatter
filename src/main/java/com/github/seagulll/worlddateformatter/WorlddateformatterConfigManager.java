package com.github.seagulll.worlddateformatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class WorlddateformatterConfigManager {
    private static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), Worlddateformatter.MOD_ID + ".json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static WorlddateformatterConfig config = new WorlddateformatterConfig();

    public static void load() {
        try {
            if (FILE.exists()) {
                FileReader reader = new FileReader(FILE);
                config = GSON.fromJson(reader, WorlddateformatterConfig.class);
                reader.close();
            } else {
                save();
            }
        } catch (Exception e) {
            Worlddateformatter.LOGGER.error("Failed to load config. Details: {}", String.valueOf(e));
        }
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter(FILE);
            GSON.toJson(config, writer);
            writer.close();
        } catch (Exception e) {
            Worlddateformatter.LOGGER.error("Failed to save config. Details: {}", String.valueOf(e));
        }
    }
}