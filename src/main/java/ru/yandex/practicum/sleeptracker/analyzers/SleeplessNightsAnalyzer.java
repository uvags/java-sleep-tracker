package ru.yandex.practicum.sleeptracker.analyzers;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.LongStream;

public class SleeplessNightsAnalyzer implements SleepAnalyzer {
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);
    private static final LocalTime NOON = LocalTime.of(12, 0);

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>("Бессонные ночи", 0L);
        }

        LocalDateTime firstSleepStart = sessions.stream()
                .map(SleepingSession::start)
                .min(LocalDateTime::compareTo)
                .orElseThrow();
        LocalDateTime lastSleepEnd = sessions.stream()
                .map(SleepingSession::end)
                .max(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDate firstNight = firstNight(firstSleepStart);
        LocalDate lastNight = lastNight(lastSleepEnd);

        long numOfNights = Duration.between(firstNight.atStartOfDay(), lastNight.atStartOfDay()).toDays() + 1;

        long sleepNights = LongStream
                .range(0, numOfNights)
                .mapToObj(firstNight::plusDays)
                .filter(night -> sessions.stream()
                        .anyMatch(session -> intersectsNightTime(session, night)))
                .count();
        long sleeplessNights = numOfNights - sleepNights;

        return new SleepAnalysisResult<>("Бессонные ночи", sleeplessNights);
    }

    private LocalDate firstNight(LocalDateTime firstStart) {
        if (firstStart.toLocalTime().isAfter(NOON)) {
            return firstStart.toLocalDate().plusDays(1);
        } else {
            return firstStart.toLocalDate();
        }
    }

    private LocalDate lastNight(LocalDateTime lastEnd) {
        if (lastEnd.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return lastEnd.toLocalDate().minusDays(1); // граничный случай
        }
        return lastEnd.toLocalDate();
    }

    private boolean intersectsNightTime(SleepingSession session, LocalDate night) {
        LocalDateTime nightStart = LocalDateTime.of(night, LocalTime.MIDNIGHT);
        LocalDateTime nightEnd = LocalDateTime.of(night, NIGHT_END);

        return session.start().isBefore(nightEnd) && nightStart.isBefore(session.end());
    }
}