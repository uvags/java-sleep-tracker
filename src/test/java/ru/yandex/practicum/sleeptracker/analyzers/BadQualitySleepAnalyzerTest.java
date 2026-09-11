package ru.yandex.practicum.sleeptracker.analyzers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.sleeptracker.utils.TestDataUtils.createSession;

class BadQualitySleepAnalyzerTest {

    @Test
    void zeroWhenEmptyList() {
        assertEquals(0L, new BadQualitySleepAnalyzer()
                .apply(List.of())
                .value());
    }

    @Test
    void noBadSessions() {
        List<SleepingSession> sessions = List.of(
                createSession("05.09.26 23:15", "06.09.26 07:30", SleepQuality.GOOD),
                createSession("06.09.26 23:50", "07.09.26 06:40", SleepQuality.NORMAL)
        );
        assertEquals(0L, new BadQualitySleepAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void onlyBadSessions() {
        List<SleepingSession> sessions = List.of(
                createSession("05.09.26 23:40", "06.09.26 08:00", SleepQuality.BAD),
                createSession("07.09.26 23:10", "08.09.26 07:00", SleepQuality.BAD)
        );
        assertEquals(2L, new BadQualitySleepAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void differentQualities() {
        List<SleepingSession> sessions = List.of(
                createSession("05.09.26 23:15", "06.09.26 07:30", SleepQuality.GOOD),
                createSession("06.09.26 23:40", "07.09.26 08:00", SleepQuality.BAD),
                createSession("07.09.26 00:10", "07.09.26 06:20", SleepQuality.NORMAL),
                createSession("07.09.26 23:10", "08.09.26 07:00", SleepQuality.BAD)
        );
        assertEquals(2L, new BadQualitySleepAnalyzer()
                .apply(sessions)
                .value());
    }

    @Test
    void correctDescription() {
        assertEquals("Сессий с плохим сном", new BadQualitySleepAnalyzer()
                .apply(List.of())
                .description());
    }
}