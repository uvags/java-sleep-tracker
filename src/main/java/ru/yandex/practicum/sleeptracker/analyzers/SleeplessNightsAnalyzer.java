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

        SleepLocalDateTimeBounds bounds = sessions.stream()
                .collect(
                        () -> new SleepLocalDateTimeBounds(
                                sessions.getFirst().start(),
                                sessions.getFirst().end()
                        ),
                        SleepLocalDateTimeBounds::accept,
                        (left, right) -> {
                            if (right.firstStart.isBefore(left.firstStart)) {
                                left.firstStart = right.firstStart;
                            }

                            if (right.lastEnd.isAfter(left.lastEnd)) {
                                left.lastEnd = right.lastEnd;
                            }
                        }
                );

        LocalDateTime firstSleepStart = bounds.getFirstStart();
        LocalDateTime lastSleepEnd = bounds.getLastEnd();

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

    // вспомогательный класс, чтобы при обходе не создавать новый массив, например
    private static class SleepLocalDateTimeBounds {
        private LocalDateTime firstStart;
        private LocalDateTime lastEnd;

        public SleepLocalDateTimeBounds(LocalDateTime firstStart, LocalDateTime lastEnd) {
            this.firstStart = firstStart;
            this.lastEnd = lastEnd;
        }

        public void accept(SleepingSession session) {
            if (session.start().isBefore(firstStart)) {
                firstStart = session.start();
            }

            if (session.end().isAfter(lastEnd)) {
                lastEnd = session.end();
            }
        }

        public LocalDateTime getFirstStart() {
            return firstStart;
        }

        public LocalDateTime getLastEnd() {
            return lastEnd;
        }
    }
}