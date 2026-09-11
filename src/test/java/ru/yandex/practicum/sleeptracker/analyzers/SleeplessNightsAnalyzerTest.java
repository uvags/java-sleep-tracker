package ru.yandex.practicum.sleeptracker.analyzers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.sleeptracker.utils.TestDataUtils.createSession;

class SleeplessNightsAnalyzerTest {

    @Test
    void zeroWhenEmptyList() {
        assertEquals(0L, new SleeplessNightsAnalyzer()
                .apply(List.of())
                .value());
    }

    @Test
    void oneNightCovered() {
        List<SleepingSession> sessions = List.of(createSession("05.09.26 23:15", "06.09.26 07:30"));
        assertEquals(0L, new SleeplessNightsAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void nightsDecreasedBy1WhenSingleDayOverlay() {
        List<SleepingSession> sessions = List.of(createSession("06.09.26 14:10", "06.09.26 15:00"));
        assertEquals(0L, new SleeplessNightsAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void correctNumberNightsWithoutNightSleep() {
        List<SleepingSession> sessions = List.of(
                createSession("05.09.26 23:40", "06.09.26 08:00"),
                createSession("06.09.26 13:30", "06.09.26 14:15"),
                createSession("07.09.26 22:30", "08.09.26 05:50")
        );

        assertEquals(1L, new SleeplessNightsAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void sessionEndsAtSixOfDay() {
        List<SleepingSession> sessions = List.of(createSession("07.09.26 00:00", "07.09.26 06:00"));
        assertEquals(0L, new SleeplessNightsAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void correctDescription() {
        assertEquals("Бессонные ночи", new SleeplessNightsAnalyzer()
                .apply(List.of())
                .description());
    }
}