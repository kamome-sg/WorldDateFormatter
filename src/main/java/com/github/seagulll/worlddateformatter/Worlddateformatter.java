package com.github.seagulll.worlddateformatter;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worlddateformatter implements ModInitializer {
    public static final String MOD_ID = "worlddateformatter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        WorlddateformatterConfigManager.load();
    }
}
