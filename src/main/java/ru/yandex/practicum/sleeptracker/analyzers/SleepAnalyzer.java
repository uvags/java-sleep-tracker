package ru.yandex.practicum.sleeptracker.analyzers;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

// Среда предложила wildcard вместо Object первоначально
@FunctionalInterface
public interface SleepAnalyzer extends Function<List<SleepingSession>, SleepAnalysisResult<?>> {
}