package ru.yandex.practicum.sleeptracker.analyzers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.sleeptracker.utils.TestDataUtils.createSession;

class MaxSleepDurationAnalyzerTest {

    @Test
    void zeroWhenEmptyList() {
        assertEquals(0L, new MaxSleepDurationAnalyzer()
                .apply(List.of())
                .value());
    }

    @Test
    void singleSession() {
        List<SleepingSession> sessions = List.of(createSession("05.09.26 23:15", "06.09.26 07:30"));
        assertEquals(495L, new MaxSleepDurationAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void maxAmongMany() {
        List<SleepingSession> sessions = List.of(
                createSession("05.09.26 22:30", "06.09.26 05:50"),
                createSession("06.09.26 23:50", "07.09.26 07:10"),
                createSession("07.09.26 13:00", "07.09.26 14:30")
        );
        assertEquals(440L, new MaxSleepDurationAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void correctDescription() {
        assertEquals("Максимальная продолжительность сна в минутах", new MaxSleepDurationAnalyzer()
                .apply(List.of())
                .description());
    }
}