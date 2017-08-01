package com.alienvault.report;

import java.time.LocalDate;
import java.util.Map;


class DaySummary {
    private LocalDate day;
    private Map<String, Long> occurrences;

    DaySummary(LocalDate day, Map<String, Long> occurrences) {
        this.day = day;
        this.occurrences = occurrences;
    }

    DaySummary() {
    }

    public LocalDate getDay() {
        return day;
    }

    public Map<String, Long> getOccurrences() {
        return occurrences;
    }
}
