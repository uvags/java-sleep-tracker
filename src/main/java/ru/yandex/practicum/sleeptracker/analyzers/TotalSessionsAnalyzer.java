package ru.yandex.practicum.sleeptracker.analyzers;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class TotalSessionsAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult<>("Всего сессий", (long) sessions.size());
    }
}