package com.github.seagulll.worlddateformatter.integration;

import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return isClothConfigLoaded() ? ConfigScreenProvider::create : null;
    }

    private boolean isClothConfigLoaded() {
        return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("cloth-config2");
    }
}