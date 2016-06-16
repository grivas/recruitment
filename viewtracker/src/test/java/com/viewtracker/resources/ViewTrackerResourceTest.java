package com.viewtracker.resources;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.jersey.api.client.GenericType;
import com.viewtracker.dto.View;
import com.viewtracker.repository.ViewTrackerRepository;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

import static com.viewtracker.dto.View.newBuilder;
import static io.dropwizard.jackson.Jackson.newObjectMapper;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by gerriv on 28/08/2014.
 */
public class ViewTrackerResourceTest{

    private static final ViewTrackerRepository repository = mock(ViewTrackerRepository.class);
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ViewTrackerResource(repository))
            .build();
    
    @Before
    public void setUp(){

    }

    @Test
    public void log_view_succeed() throws JsonProcessingException {
        DateTime now = now();
        View view = View.newBuilder().withUserId(3).withViewerId(5).viewedOn(now).build();
        resources.client().resource("/viewtracker").type(MediaType.APPLICATION_JSON_TYPE).post(newObjectMapper().writeValueAsString(view));
        verify(repository).logView(eq(3l),eq(5l),any(DateTime.class));
    }

    @Test
    @Ignore("Need more time to check this case")
    public void log_view_runtime_exception() throws JsonProcessingException {
        doThrow(RuntimeException.class).when(repository).logView(eq(3l),eq(5l),any(DateTime.class));
        DateTime now = now();
        View view = View.newBuilder().withUserId(3).withViewerId(5).viewedOn(now).build();
        resources.client().resource("/viewtracker").type(MediaType.APPLICATION_JSON_TYPE).post(newObjectMapper().writeValueAsString(view));
        verify(repository).logView(eq(3l),eq(5l),any(DateTime.class));
    }

    @Test
    public void list_views(){
        List<View> mockList = mockViewList();
        when(repository.listViewersOf(eq(2l),eq(10), any(DateTime.class))).thenReturn(mockList);
        List<View> result = resources.client().resource("/viewtracker/2").get(new GenericType<List<View>>(){});
        assertEquals(mockList, result);
    }

    private List<View> mockViewList() {
        return Arrays.asList(
                newBuilder().withUserId(2).withViewerId(4).viewedOn(now().minusDays(1)).build(),
                newBuilder().withUserId(2).withViewerId(3).viewedOn(now().minusDays(6)).build(),
                newBuilder().withUserId(2).withViewerId(6).viewedOn(now().minusDays(4)).build()
        );
    }
}
