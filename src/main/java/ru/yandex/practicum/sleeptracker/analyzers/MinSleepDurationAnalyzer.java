package ru.yandex.practicum.sleeptracker.analyzers;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class MinSleepDurationAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long min = sessions.stream()
                .map(SleepingSession::getDurationMinutes)
                .min((a, b) -> a.compareTo(b))
                .orElse(0L);

        return new SleepAnalysisResult<>("Минимальная продолжительность сна в минутах", min);
    }
}