package me.seagulll.worlddateformatter;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WDF implements ClientModInitializer {
    public static final String MOD_ID = "worlddateformatter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        WDFConfigManager.load();
    }
}
