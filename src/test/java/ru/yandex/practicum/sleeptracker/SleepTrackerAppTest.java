package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SleepTrackerAppTest {
    // https://habr.com/ru/companies/otus/articles/920200/
    // https://www.baeldung.com/junit-5-temporary-directory
    @TempDir
    Path tempDir;

    private String runAppAndCaptureSystemOutOutput(String fileContent) throws Exception {
        Path file = tempDir.resolve("sleep-log.txt");
        Files.writeString(file, fileContent, StandardCharsets.UTF_8);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

        try {
            SleepTrackerApp.main(new String[]{file.toString()});
        } finally {
            System.setOut(originalOut);
        }

        return output.toString(StandardCharsets.UTF_8);
    }

    private String runAppAndCaptureSystemErrOutput(String filePath) {
        ByteArrayOutputStream error = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(error, true, StandardCharsets.UTF_8));

        try {
            SleepTrackerApp.main(new String[]{filePath});
        } finally {
            System.setErr(originalErr);
        }

        return error.toString(StandardCharsets.UTF_8);
    }

    private String runAppWithFileContentAndCaptureSystemErrOutput(String content) throws Exception {
        Path file = tempDir.resolve("sleep_tracker_test_file");
        Files.writeString(file, content, StandardCharsets.UTF_8);

        return runAppAndCaptureSystemErrOutput(file.toString());
    }

    @Test
    void printsAllStatistics() throws Exception {
        String result = runAppAndCaptureSystemOutOutput("05.09.26 23:15;06.09.26 07:30;GOOD\n");

        assertTrue(result.contains("Всего сессий: 1"));
        assertTrue(result.contains("Минимальная продолжительность сна в минутах: 495"));
        assertTrue(result.contains("Максимальная продолжительность сна в минутах: 495"));
        assertTrue(result.contains("Средняя длительность сна в минутах: 495"));
        assertTrue(result.contains("Сессий с плохим сном: 0"));
        assertTrue(result.contains("Бессонные ночи: 0"));
        assertTrue(result.contains("Хронотип (сова, жаворонок, голубь): голубь"));
    }

    @Test
    void analyzesSeveralSessions() throws Exception {
        String result = runAppAndCaptureSystemOutOutput(
                "05.09.26 23:15;06.09.26 07:30;GOOD\n" +
                        "06.09.26 14:10;06.09.26 15:00;BAD\n" +
                        "07.09.26 23:40;08.09.26 08:00;BAD\n"
        );

        assertTrue(result.contains("Всего сессий: 3"));
        assertTrue(result.contains("Минимальная продолжительность сна в минутах: 50"));
        assertTrue(result.contains("Максимальная продолжительность сна в минутах: 500"));
        assertTrue(result.contains("Средняя длительность сна в минутах: 348"));
        assertTrue(result.contains("Сессий с плохим сном: 2"));
    }

    @Test
    void printsErrorWhenFileDoesNotExist() {
        String result = runAppAndCaptureSystemErrOutput("not-exist-sleep-log.txt");
        assertTrue(result.contains("Не удалось прочитать файл:"));
    }

    @Test
    void parsesSleepQualityCorrectly() throws Exception {
        String result = runAppAndCaptureSystemOutOutput("05.09.26 23:15;06.09.26 07:30;BAD\n");
        assertTrue(result.contains("Сессий с плохим сном: 1"));
    }

    @Test
    void errorWhenFileNotExist() {
        String result = runAppAndCaptureSystemErrOutput("sleeptrackerthisfilenotexistihope.txt");
        assertTrue(result.contains("Не удалось прочитать файл"));
    }

    @Test
    void errorWhenFilePathIsEmpty() {
        String result = runAppAndCaptureSystemErrOutput("");
        assertTrue(result.contains("Пустой путь к файлу"));
    }

    @Test
    void errorWhenFilePathIsDirectory() throws Exception {
        Path directory = tempDir.resolve("sleep_tracker_temp_dir");
        Files.createDirectory(directory);
        String result = runAppAndCaptureSystemErrOutput(directory.toString());
        assertTrue(result.contains("Путь указывает на директорию, а не файл"));
    }

    @Test
    void errorWhenWakeBeforeBeginningOfSleep() throws Exception {
        String result = runAppWithFileContentAndCaptureSystemErrOutput(
                "06.09.26 23:15;05.09.26 07:30;BAD");
        assertTrue(result.contains("Время начала сна должно быть раньше окончания сна"));
    }

    @Test
    void errorWhenInvalidDateFormat() throws Exception {
        String result = runAppWithFileContentAndCaptureSystemErrOutput(
                "05.09.26 23:15;06.5009.26 07:30;BAD");
        assertTrue(result.contains("Неверный формат времени в строке данных в файле"));
    }

    @Test
    void errorWhenInvalidSleepQuality() throws Exception {
        String result = runAppWithFileContentAndCaptureSystemErrOutput(
                "05.09.26 23:15;06.09.26 07:30;notexistedsleepquaality");
        assertTrue(result.contains("Неверно указано качество сна"));
    }

    @Test
    void errorWhenNotAllDataGiven() throws Exception {
        String result = runAppWithFileContentAndCaptureSystemErrOutput(
                "05.09.26 23:15;BAD");
        assertTrue(result.contains("Неверный формат строки данных в файле"));
    }
}