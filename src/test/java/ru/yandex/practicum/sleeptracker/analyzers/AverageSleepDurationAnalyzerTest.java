package ru.yandex.practicum.sleeptracker.analyzers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.sleeptracker.utils.TestDataUtils.createSession;

class AverageSleepDurationAnalyzerTest {

    @Test
    void zeroWhenEmptyList() {
        assertEquals(0L, new AverageSleepDurationAnalyzer()
                .apply(List.of())
                .value());
    }

    @Test
    void durationWhenOneSession() {
        List<SleepingSession> sessions = List.of(createSession("05.09.26 23:15", "06.09.26 07:30"));
        assertEquals(495L, new AverageSleepDurationAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void averageWhenSeveralSessions() {
        List<SleepingSession> sessions = List.of(
                createSession("05.09.26 23:40", "06.09.26 08:00"),
                createSession("06.09.26 14:10", "06.09.26 15:00"),
                createSession("07.09.26 00:10", "07.09.26 06:20")
        );

        assertEquals(307L, new AverageSleepDurationAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void correctDescription() {
        assertEquals("Средняя длительность сна в минутах", new AverageSleepDurationAnalyzer()
                .apply(List.of())
                .description());
    }
}