package ru.yandex.practicum.sleeptracker.analyzers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.sleeptracker.utils.TestDataUtils.createSession;

class ChronotypeSleepAnalyzerTest {

    @Test
    void pigeonWhenNoNightSessions() {
        List<SleepingSession> sessions = List.of(
                createSession("07.09.26 10:00", "07.09.26 11:00"),
                createSession("08.09.26 14:00", "08.09.26 15:00")
        );
        assertEquals("голубь", new ChronotypeSleepAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void owlWhenSingleOwlSession() {
        List<SleepingSession> sessions = List.of(
                createSession("07.09.26 23:15", "08.09.26 09:30")
        );
        assertEquals("сова", new ChronotypeSleepAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void larkWhenSingleLarkSession() {
        List<SleepingSession> sessions = List.of(
                createSession("07.09.26 21:00", "08.09.26 06:00")
        );
        assertEquals("жаворонок", new ChronotypeSleepAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void pigeonWhenSinglePigeonSession() {
        List<SleepingSession> sessions = List.of(
                createSession("07.09.26 22:30", "08.09.26 08:00")
        );
        assertEquals("голубь", new ChronotypeSleepAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void pigeonWhenEqualOppositeChronotypes() {
        List<SleepingSession> sessions = List.of(
                createSession("07.09.26 23:15", "08.09.26 09:30"),
                createSession("08.09.26 21:00", "09.09.26 06:00")
        );
        assertEquals("голубь", new ChronotypeSleepAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void owlWhenMoreOwl() {
        List<SleepingSession> sessions = List.of(
                createSession("06.09.26 23:15", "07.09.26 09:30"),
                createSession("07.09.26 22:30", "08.09.26 08:00"),
                createSession("08.09.26 23:45", "09.09.26 10:00")
        );

        assertEquals("сова", new ChronotypeSleepAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void correctDescription() {
        assertEquals("Хронотип (сова, жаворонок, голубь)", new ChronotypeSleepAnalyzer()
                .apply(List.of())
                .description());
    }
}