package me.seagulll.worlddateformatter;

public class WDFConfig {
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
