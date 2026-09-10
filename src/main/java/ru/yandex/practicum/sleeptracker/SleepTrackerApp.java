package ru.yandex.practicum.sleeptracker;
import ru.yandex.practicum.sleeptracker.analyzers.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Stream;

public class SleepTrackerApp {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static void main(String[] args) {
        String filePath = args.length > 0 ? args[0] : "src/main/resources/sleep_log.txt";

        try {
            Path path = Path.of(filePath);
            if (args.length > 0 && filePath.trim().isEmpty()) {
                throw new IOException("Пустой путь к файлу");
            }
            if (!Files.exists(path)) {
                throw new FileNotFoundException("Файла не существует: "+ filePath);
            }
            if (Files.isDirectory(path)) {
                throw new FileNotFoundException("Путь указывает на директорию, а не файл: " + filePath);
            }
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
        } catch (IllegalArgumentException e) {
            System.err.println("Неверные данные в файле: " + e.getMessage());
        }
    }

    private static List<SleepingSession> readSessions(Path path) throws IOException {
        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .filter(line -> !line.isBlank()) // пустую строку/просто пробелы пропустим
                    .map(SleepTrackerApp::parseLine)
                    .toList();
        }
    }

    private static SleepingSession parseLine(String line) {
        String[] parts = line.split(";"); // разбиваем строку на начало сна, конец сна, характеристику сна

        if (parts.length != 3) {
            throw new IllegalArgumentException("Неверный формат строки данных в файле: " + line);
        }

        LocalDateTime start;
        LocalDateTime end;
        SleepQuality quality;

        try {
            start = LocalDateTime.parse(parts[0].trim(), DATE_TIME_FORMATTER);
            end = LocalDateTime.parse(parts[1].trim(), DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат времени в строке данных в файле: " + line);
        }

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Время начала сна должно быть раньше окончания сна");
        }

        try {
            quality = SleepQuality.valueOf(parts[2].trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверно указано качество сна: " + parts[2].trim());
        }

        return new SleepingSession(start, end, quality);
    }
}