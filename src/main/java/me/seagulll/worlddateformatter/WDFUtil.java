package me.seagulll.worlddateformatter;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.IllformedLocaleException;
import java.util.Locale;
import java.util.Optional;

public final class WDFUtil {
    public static Optional<String> safeFormat(ZonedDateTime dateTime, String format, String localeCode) {
        try {
            return Optional.of(dateTime.format(DateTimeFormatter.ofPattern(format, codeToLocale(localeCode).orElse(Locale.US))));
        } catch (IllegalArgumentException | DateTimeException e) {
            return Optional.empty();
        }
    }

    public static Optional<Locale> codeToLocale(String localeCode) {
        try {
            return Optional.of(new Locale.Builder()
                    .setLanguageTag(localeCode.replace("_", "-"))
                    .build());
        } catch (IllformedLocaleException e) {
            return Optional.empty();
        }
    }
}
