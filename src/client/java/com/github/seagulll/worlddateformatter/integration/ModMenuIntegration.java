package com.github.seagulll.worlddateformatter.integration;

import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return isYACLLoaded() ? ConfigScreenProvider::create : null;
    }

    private boolean isYACLLoaded() {
        return FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3");
    }
}