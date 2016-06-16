package com.viewtracker.dto;

import org.joda.time.DateTime;
import org.junit.Test;

import static io.dropwizard.jackson.Jackson.newObjectMapper;
import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by gerriv on 29/08/2014.
 */
public class ViewTest {
    @Test
    public void serializes_to_json() throws Exception {
        final View person = View.newBuilder().
                withUserId(5).withViewerId(9).
                viewedOn(new DateTime(1409296931051l)).build();
        assertThat(newObjectMapper().writeValueAsString(person))
                .isEqualTo(fixture("view.json"));
    }

    @Test
    public void serializes_from_json() throws Exception {
        final View view = View.newBuilder().
                withUserId(5).withViewerId(9).
                viewedOn(new DateTime(1409296931051l)).build();
        assertThat(newObjectMapper().readValue(fixture("view.json"), View.class))
                .isEqualTo(view);
    }
}
