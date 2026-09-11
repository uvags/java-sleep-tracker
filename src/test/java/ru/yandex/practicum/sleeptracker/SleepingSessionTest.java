package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SleepingSessionTest {

    @Test
    void getDurationMinutesWhenNormalSession() {
        LocalDateTime start = LocalDateTime.of(2026, 9, 7, 22, 0);
        LocalDateTime end = start.plusHours(7).plusMinutes(30);
        SleepingSession session = new SleepingSession(start, end, SleepQuality.NORMAL);
        assertEquals(450, session.getDurationMinutes());
    }

    @Test
    void getDurationMinutesWhenOvernightSession() {
        LocalDateTime start = LocalDateTime.of(2026, 9, 7, 23, 15);
        LocalDateTime end = LocalDateTime.of(2026, 9, 8, 6, 45);
        SleepingSession session = new SleepingSession(start, end, SleepQuality.GOOD);
        assertEquals(450, session.getDurationMinutes());
    }

    @Test
    void getDurationMinutesWhenSessionLessThanHour() {
        LocalDateTime start = LocalDateTime.of(2026, 9, 7, 14, 0);
        LocalDateTime end = start.plusMinutes(45);
        SleepingSession session = new SleepingSession(start, end, SleepQuality.BAD);
        assertEquals(45, session.getDurationMinutes());
    }

    @Test
    void getDurationMinutesWhenSessionSpanningMultipleDays() {
        LocalDateTime start = LocalDateTime.of(2026, 9, 7, 22, 0);
        LocalDateTime end = LocalDateTime.of(2026, 9, 9, 6, 0);
        SleepingSession session = new SleepingSession(start, end, SleepQuality.NORMAL);
        assertEquals(32 * 60, session.getDurationMinutes());
    }
}