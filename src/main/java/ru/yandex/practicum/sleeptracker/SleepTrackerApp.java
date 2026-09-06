package ru.yandex.practicum.sleeptracker;
import ru.yandex.practicum.sleeptracker.analyzers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

public class SleepTrackerApp {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static void main(String[] args) {
        String filePath = args.length > 0 ? args[0] : "src/main/resources/sleep_log.txt";
        try {
            // спинок функций, которые по очереди будут применяться к данным
            List<SleepingSession> sessions = readSessions(Path.of(filePath));
            List<SleepAnalyzer> analyzers = List.of(
                    new TotalSessionsAnalyzer(),
                    new MinSleepDurationAnalyzer(),
                    new MaxSleepDurationAnalyzer(),
                    new AverageSleepDurationAnalyzer(),
                    new BadQualitySleepAnalyzer(),
                    new SleeplessNightsAnalyzer(),
                    new ChronotypeSleepAnalyzer()
            );
            analyzers.stream().map(a -> a.apply(sessions)).forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Не удалось прочитать файл: " + e.getMessage());
        }
    }

    private static List<SleepingSession> readSessions(Path path) throws IOException {
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .map(SleepTrackerApp::parseLine)
                    .toList();
        }
    }

    private static SleepingSession parseLine(String line) {
        String[] parts = line.split(";"); // разбиваем строку на начало сна, конец сна, характеристику сна
        LocalDateTime start = LocalDateTime.parse(parts[0].trim(), DATE_TIME_FORMATTER);
        LocalDateTime end = LocalDateTime.parse(parts[1].trim(), DATE_TIME_FORMATTER);
        SleepQuality quality = SleepQuality.valueOf(parts[2].trim());

        return new SleepingSession(start, end, quality);
    }
}