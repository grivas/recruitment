package com.alienvault;

import com.alienvault.github.GitHubClient;
import com.alienvault.github.GitHubClientImpl;
import com.alienvault.report.GitHubIssueReport;
import com.alienvault.report.GitHubIssueReporter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Github Issues
 * -------------
 *
 * Create a program that generates a report about the the Issues belonging to a list of github repositories ordered by
 * creation time, and information about the day when most Issues were created.
 *
 * Input:
 * -----
 *   List of 1 to n Strings with Github repositories references with the format "owner/repository"
 *
 *
 * Output:
 * ------
 * String representation of a Json dictionary with the following content:
 *
 * - "issues": List containing all the Issues related to all the repositories provided.
 *             The list should be ordered by the Issue "created_at" field (From oldest to newest)
 *             Each entry of the list will be a dictionary with basic Issue information:
 *             "id", "state", "title", "repository" and "created_at" fields.
 *             Issue entry example:
 *      {
 *          "id": 1,
 *          "state": "open",
 *          "title": "Found a bug",
 *          "repository": "owner1/repository1",
 *          "created_at": "2011-04-22T13:33:48Z"
 *      }
 *
 * - "top_day": Dictionary with the information of the day when most Issues were created.
 *              It will contain the day and the number of Issues that were created on each repository this day
 *              If there are more than one "top_day", the latest one should be used.
 *              example:
 *     {
 *         "day": "2011-04-22",
 *         "occurrences": {
 *             "owner1/repository1": 8,
 *             "owner2/repository2": 0,
 *             "owner3/repository3": 2
 *         }
 *     }
 *
 *
 * Output example:
 * --------------
 *
 * {
 *     "issues": [
 *         {
 *              "id": 38,
 *              "state": "open",
 *              "title": "Found a bug",
 *              "repository": "owner1/repository1",
 *              "created_at": "2011-04-22T13:33:48Z"
 *         },
 *         {
 *              "id": 23,
 *              "state": "open",
 *              "title": "Found a bug 2",
 *              "repository": "owner1/repository1",
 *              "created_at": "2011-04-22T18:24:32Z"
 *         },
 *         {
 *              "id": 24,
 *              "state": "closed",
 *              "title": "Feature request",
 *              "repository": "owner2/repository2",
 *              "created_at": "2011-05-08T09:15:20Z"
 *         }
 *     ],
 *     "top_day": {
 *         "day": "2011-04-22",
 *         "occurrences": {
 *             "owner1/repository1": 2,
 *             "owner2/repository2": 0
 *         }
 *     }
 * }
 *
 * --------------------------------------------------------
 *
 * You can create the classes and methods you consider.
 * You can use any library you need.
 * Good modularization, error control and code style will be taken into account.
 * Memory usage and execution time will be taken into account.
 *
 * Good Luck!
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    /**
     * @param args String array with Github repositories with the format "owner/repository"
     */
    public static void main(String[] args) throws JsonProcessingException {
        logger.info("Started");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        GitHubClient gitHubClient = new GitHubClientImpl();
        GitHubIssueReporter reporter = new GitHubIssueReporter(gitHubClient);
        GitHubIssueReport report = reporter.createReport(new HashSet<>(Arrays.asList(args)));

        logger.info(objectMapper.writeValueAsString(report));
    }
}
