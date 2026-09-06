package ru.yandex.practicum.sleeptracker.utils;

import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestDataUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static SleepingSession createSession(String startStr, String endStr) {
        return createSession(startStr, endStr, SleepQuality.NORMAL);
    }

    public static SleepingSession createSession(String startStr, String endStr, SleepQuality quality) {
        LocalDateTime start = LocalDateTime.parse(startStr, FORMATTER);
        LocalDateTime end = LocalDateTime.parse(endStr, FORMATTER);
        return new SleepingSession(start, end, quality);
    }
}