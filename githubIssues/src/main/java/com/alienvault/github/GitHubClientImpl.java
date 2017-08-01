package com.alienvault.github;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class GitHubClientImpl implements GitHubClient {
    private static final Logger logger = LoggerFactory.getLogger(GitHubClientImpl.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Issue> getIssuesOf(String repository) {
        String url = format("https://api.github.com/repos/%s/issues", repository);
        try {
            List<Issue> issues = asList(restTemplate.getForObject(url, Issue[].class));
            issues.forEach(issue -> issue.setRepository(repository));
            return issues;
        } catch (RestClientException e) {
            logger.info("Repository {} could not be processed. Reason: {}", repository, e.getMessage());
            return emptyList();
        }
    }
}
