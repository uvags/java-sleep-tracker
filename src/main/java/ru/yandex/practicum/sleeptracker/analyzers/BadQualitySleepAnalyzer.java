package ru.yandex.practicum.sleeptracker.analyzers;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
public class BadQualitySleepAnalyzer implements SleepAnalyzer {
    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long numOfBadSleepSessions = sessions.stream().filter(s -> s.quality() == SleepQuality.BAD)
                .count();

        return new SleepAnalysisResult<>("Сессий с плохим сном", numOfBadSleepSessions);
    }
}