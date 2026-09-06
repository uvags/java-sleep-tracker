package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.time.LocalDateTime;

public record SleepingSession(LocalDateTime start, LocalDateTime end, SleepQuality quality) {
    public long getDurationMinutes() {
        return Duration.between(start, end).toMinutes();
    }
}