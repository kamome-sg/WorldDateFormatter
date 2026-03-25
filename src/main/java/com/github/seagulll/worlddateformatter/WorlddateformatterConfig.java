package com.github.seagulll.worlddateformatter;

public class WorlddateformatterConfig {
    public static final String DEFAULT_FORMAT = "yyyy/M/d H:mm";

    private boolean isEnabled = true;
    private String format = DEFAULT_FORMAT;

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        WorlddateformatterConfigManager.save();
    }

    public void setFormat(String format) {
        this.format = format;
        WorlddateformatterConfigManager.save();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getFormat() {
        return format;
    }
}
