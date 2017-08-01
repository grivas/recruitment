package com.alienvault

import com.alienvault.github.GitHubClient
import com.alienvault.github.Issue
import com.alienvault.report.GitHubIssueReporter
import spock.lang.Specification

import java.time.LocalDateTime

import static java.time.LocalDateTime.now
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric

class GitHubIssueReporterTest extends Specification {
    static NOW = now()

    def client = Mock(GitHubClient)
    def reporter = new GitHubIssueReporter(client)

    def "Should create a report for a list of repositories"() {
        def now = now()
        def issue1 = stubIssue("repo1", now)
        def issue2 = stubIssue("repo1", now.minusSeconds(1))
        def issue3 = stubIssue("repo1", now.minusDays(2))
        def issue4 = stubIssue("repo1", now.minusDays(3))
        def issue5 = stubIssue("repo2", now.minusSeconds(2))
        def issue6 = stubIssue("repo2", now.minusDays(4))
        def issue7 = stubIssue("repo3", now.minusDays(2).minusSeconds(1))
        def issue8 = stubIssue("repo4", now.minusDays(2).minusSeconds(2))

        def issuesByRepository = [issue1, issue2, issue3, issue4, issue5, issue6, issue7, issue8]
                .groupBy { it.repository }

        given:
        issuesByRepository.each {
            client.getIssuesOf(it.key as String) >> it.value
        }

        when:
        def report = reporter.createReport(issuesByRepository.keySet())

        then:
        report.issues.size() == 8
        report.issues[0] == issue6
        report.issues[1] == issue4
        report.issues[2] == issue8
        report.issues[3] == issue7
        report.issues[4] == issue3
        report.issues[5] == issue5
        report.issues[6] == issue2
        report.issues[7] == issue1

        and:
        report.top_day.getDay() == now.toLocalDate()

        report.top_day.occurrences["repo1"] == 2
        report.top_day.occurrences["repo2"] == 1
    }

    Issue stubIssue(String repository, LocalDateTime createdAt) {
        new Issue(id: randomNumeric(2).toInteger(),
                state: "open",
                title: randomAlphabetic(30),
                repository: repository,
                createdAt: createdAt)
    }
}
