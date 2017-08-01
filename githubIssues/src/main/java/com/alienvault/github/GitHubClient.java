package com.alienvault.github;

import java.util.List;

public interface GitHubClient {
    List<Issue> getIssuesOf(String repository);
}
