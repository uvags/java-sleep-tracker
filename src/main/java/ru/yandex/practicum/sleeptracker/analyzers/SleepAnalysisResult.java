package ru.yandex.practicum.sleeptracker.analyzers;

public record SleepAnalysisResult<T>(String description, T value) {
    @Override
    public String toString() {
        return description + ": " + value;
    }
}