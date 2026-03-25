package com.github.seagulll.worlddateformatter;

import java.time.format.DateTimeFormatter;

public class Util {
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
        return isValidFormat(format) ? format : WorlddateformatterConfig.DEFAULT_FORMAT;
    }
}
