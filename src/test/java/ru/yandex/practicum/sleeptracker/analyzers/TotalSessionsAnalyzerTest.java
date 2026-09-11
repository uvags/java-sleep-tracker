package ru.yandex.practicum.sleeptracker.analyzers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.sleeptracker.utils.TestDataUtils.createSession;

class TotalSessionsAnalyzerTest {

    @Test
    void zeroWhenEmptyList() {
        assertEquals(0L, new TotalSessionsAnalyzer()
                .apply(List.of())
                .value());
    }

    @Test
    void oneSession() {
        List<SleepingSession> sessions = List.of(createSession("05.09.26 23:15", "06.09.26 07:30"));
        assertEquals(1L, new TotalSessionsAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void multipleSessions() {
        List<SleepingSession> sessions = List.of(
                createSession("05.09.26 23:15", "06.09.26 07:30"),
                createSession("06.09.26 23:50", "07.09.26 06:40"),
                createSession("07.09.26 14:10", "07.09.26 15:00")
        );
        assertEquals(3L, new TotalSessionsAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void correctDescription() {
        assertEquals("Всего сессий", new TotalSessionsAnalyzer()
                .apply(List.of())
                .description());
    }
}