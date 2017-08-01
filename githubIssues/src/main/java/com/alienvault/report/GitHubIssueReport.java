package com.alienvault.report;

import com.alienvault.github.Issue;

import java.util.List;

public class GitHubIssueReport {
    private List<Issue> issues;
    private DaySummary top_day;

    public GitHubIssueReport() {
    }

    GitHubIssueReport(List<Issue> issues, DaySummary top_day) {
        this.issues = issues;
        this.top_day = top_day;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public DaySummary getTop_day() {
        return top_day;
    }
}
