package com.github.seagulll.worlddateformatter;

public class WorlddateformatterConfig {
    public static final String DEFAULT_FORMAT = "yyyy/M/d H:mm";

    private boolean isEnabled = true;
    private String format = DEFAULT_FORMAT;

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getFormat() {
        return format;
    }
}
