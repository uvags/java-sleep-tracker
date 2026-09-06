package ru.yandex.practicum.sleeptracker.analyzers;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class MaxSleepDurationAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long max = sessions.stream()
                .map(SleepingSession::getDurationMinutes)
                .max(Long::compareTo)
                .orElse(0L);

        return new SleepAnalysisResult<>("Максимальная продолжительность сна в минутах", max);
    }
}