package org.togetherjava.tjbot.logwatcher.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.stereotype.Component;
import org.togetherjava.tjbot.logwatcher.config.Config;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Component
public final class LogReader {

    @NotNull
    private final Path logPath;

    public LogReader(@NotNull Config config) {
        this.logPath = Path.of(config.getLogPath());
    }

    /**
     * Returns all log Files in the configured Path {@link Config#logPath}
     *
     * @return Names of the Logfiles
     */
    @Unmodifiable
    public @NotNull List<Path> getLogs() {
        try (final Stream<Path> stream = Files.list(this.logPath)) {
            return stream.filter(Files::isRegularFile)
                .filter(s -> s.toString().endsWith(".log") || s.toString().endsWith(".log.gz"))
                .toList();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Read's the content of the given Logfile in the configured Logging path
     *
     * @param log Name of the Logfile
     * @return The Content of the Log
     */
    public List<String> readLog(final @NotNull Path log) {
        try {
            return Files.readAllLines(log);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
