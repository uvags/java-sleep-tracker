package ru.yandex.practicum.sleeptracker.analyzers;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class AverageSleepDurationAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long average = Math.round(sessions.stream().mapToLong(SleepingSession::getDurationMinutes)
                .average()
                .orElse(0));

        return new SleepAnalysisResult<>("Средняя длительность сна в минутах", average);
    }
}