package ru.yandex.practicum.sleeptracker.analyzers;
import ru.yandex.practicum.sleeptracker.Chronotype;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ChronotypeSleepAnalyzer implements SleepAnalyzer {
    private static final LocalTime OWL_BEDTIME = LocalTime.of(23, 0);
    private static final LocalTime OWL_WAKE = LocalTime.of(9, 0);
    private static final LocalTime LARK_BEDTIME = LocalTime.of(22, 0);
    private static final LocalTime LARK_WAKE = LocalTime.of(7, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    // true, если сессия пересекает ночной интервал 00:00-06:00
    private boolean isNightSession(SleepingSession session) {
        LocalDateTime start = session.start();
        LocalDateTime end = session.end();
        LocalDateTime startDayMidnight = LocalDateTime.of(start.toLocalDate(), LocalTime.MIDNIGHT);
        LocalDateTime startDayNightEnd = LocalDateTime.of(start.toLocalDate(), NIGHT_END);
        boolean intersectsStartDayNight = start.isBefore(startDayNightEnd) && startDayMidnight.isBefore(end);
        LocalDateTime endDayMidnight = LocalDateTime.of(end.toLocalDate(), LocalTime.MIDNIGHT);
        LocalDateTime endDayNightEnd = LocalDateTime.of(end.toLocalDate(), NIGHT_END);
        boolean intersectsEndDayNight = start.isBefore(endDayNightEnd) && endDayMidnight.isBefore(end);

        return intersectsStartDayNight || intersectsEndDayNight;
    }

    // Здесь каждая сессия соответствует одному периоду сна (ночной или дневной сон), иначе непонятно какой тип
    // Для определения типа мы учитываем лишь ночные сессии сна
    @Override
    public SleepAnalysisResult<String> apply(List<SleepingSession> sessions) {
        long owlCount = sessions.stream()
                .filter(this::isNightSession) // убираем "неночные" сессии
                .filter(s ->
                        s.start().toLocalTime().isAfter(OWL_BEDTIME) && s.end().toLocalTime().isAfter(OWL_WAKE))
                .count();
        long larkCount = sessions.stream()
                .filter(this::isNightSession)
                .filter(s ->
                        s.start().toLocalTime().isBefore(LARK_BEDTIME) && s.end().toLocalTime().isBefore(LARK_WAKE))
                .count();
        long pigeonCount = sessions.stream()
                .filter(this::isNightSession)
                .filter(s -> {
                    LocalTime bed = s.start().toLocalTime();
                    LocalTime wake = s.end().toLocalTime();
                    boolean isOwl = bed.isAfter(OWL_BEDTIME) && wake.isAfter(OWL_WAKE);
                    boolean isLark = bed.isBefore(LARK_BEDTIME) && wake.isBefore(LARK_WAKE);
                    return !isLark && !isOwl;
                })
                .count();

        Chronotype result;
        if (owlCount > larkCount && owlCount > pigeonCount) {
            result = Chronotype.OWL;
        } else if (larkCount > owlCount && larkCount > pigeonCount) {
            result = Chronotype.LARK;
        } else {
            result = Chronotype.PIGEON;
        }

        return new SleepAnalysisResult<>("Хронотип (сова, жаворонок, голубь)", result.getDisplayName());
    }
}