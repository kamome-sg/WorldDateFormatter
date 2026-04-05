package me.seagulll.worlddateformatter;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class WDFUtil {
    private WDFUtil() {
        throw new AssertionError();
    }

    public static boolean isValidFormat(String format) {
        try {
            DateTimeFormatter.ofPattern(format);
            return true;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String validateFormat(String format) {
        return isValidFormat(format) ? format : WDFConfig.DEFAULT_FORMAT;
    }

    public static DateTimeFormatter getFormatter(String format, String code) {
        return DateTimeFormatter.ofPattern(validateFormat(format), WDFUtil.getLocale(code));
    }

    public static Locale getLocale(String code) {
        String[] parts = code.split("_", 2);
        try {
            return new Locale.Builder()
                    .setLanguageTag(parts[0] + "-" + parts[1].toUpperCase())
                    .build();
        }
        catch (Exception e) {
            return Locale.US;
        }
    }
}
