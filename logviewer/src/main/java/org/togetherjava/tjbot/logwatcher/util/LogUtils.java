package org.togetherjava.tjbot.logwatcher.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.togetherjava.tjbot.db.generated.tables.pojos.Logevents;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class LogUtils {

    /**
     * Loglevel of all Configurable Levels in the logger
     */
    public enum LogLevel {
        INFO,
        WARN,
        ERROR,
        DEBUG,
        TRACE;

        /**
         * Collects all LogLevel in a Set
         * 
         * @return A Set containing every Loglevel
         */
        @Contract(" -> new")
        public static @NotNull Set<LogLevel> getAll() {
            return EnumSet.allOf(LogLevel.class);
        }

        /**
         * Maps the LogLevel to their name and collects it in a Set
         *
         * @return A Set containing every LogLevel as String
         */
        @Unmodifiable
        public static @NotNull Set<String> getAllNames() {
            return Arrays.stream(values()).map(Enum::name).collect(Collectors.toUnmodifiableSet());
        }
    }

    /**
     * Maps a Logevent to the color-coded css-Class (css class has i.e. red as background-color for
     * ERROR events)
     *
     * @param event Logevent from the DB with a specific Logevent
     * @return The name of the CSS class to use with this Logevent.
     */
    public static @NotNull String logLevelToCssClass(final Logevents event) {
        return event.getLevel().toLowerCase(Locale.ENGLISH);
    }



    @Contract(pure = true)
    private LogUtils() {}
}
