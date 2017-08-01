package com.alienvault.report;

import com.alienvault.github.GitHubClient;
import com.alienvault.github.Issue;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.Optional.of;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class GitHubIssueReporter {
    private final GitHubClient client;

    public GitHubIssueReporter(GitHubClient client) {
        this.client = client;
    }

    public GitHubIssueReport createReport(Set<String> repositories) {
        List<Issue> allIssues = fetchListOfIssuesSortedByCreationDate(repositories);
        DaySummary topDay = summaryOfTheDayWithMoreIssues(allIssues);
        return new GitHubIssueReport(allIssues, topDay);
    }

    private List<Issue> fetchListOfIssuesSortedByCreationDate(Set<String> repositories) {
        return repositories.parallelStream()
                .map(client::getIssuesOf)
                .flatMap(List::stream)
                .sorted(comparing(Issue::getCreatedAt))
                .collect(toList());
    }

    private DaySummary summaryOfTheDayWithMoreIssues(List<Issue> issues) {
        return issues.stream()
                .collect(groupingBy(Issue::getCreatedDate))
                .entrySet().stream()
                .max(this::byNumberOfIssuesAndDate)
                .flatMap(this::asDaySummary)
                .orElse(new DaySummary());
    }

    private int byNumberOfIssuesAndDate(Map.Entry<LocalDate, List<Issue>> a, Map.Entry<LocalDate, List<Issue>> b) {
        int compare = Long.compare(a.getValue().size(), b.getValue().size());
        return compare != 0 ? compare : a.getKey().compareTo(b.getKey());
    }

    private Optional<DaySummary> asDaySummary(Map.Entry<LocalDate, List<Issue>> topDay) {
        Map<String, Long> occurrences = topDay.getValue().stream()
                .collect(groupingBy(Issue::getRepository, counting()));
        return of(new DaySummary(topDay.getKey(), occurrences));
    }
}
