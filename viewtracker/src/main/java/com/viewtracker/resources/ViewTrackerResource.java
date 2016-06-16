package com.viewtracker.resources;

import com.viewtracker.dto.View;
import com.viewtracker.repository.ViewTrackerRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.joda.time.DateTime.now;

/**
 * Created by gerriv on 28/08/2014.
 */
@Path("/viewtracker")
@Produces(MediaType.APPLICATION_JSON)
public class ViewTrackerResource {
    private final ViewTrackerRepository viewTrackerRepo;

    public ViewTrackerResource(ViewTrackerRepository repository) {
        this.viewTrackerRepo = repository;
    }

    @GET
    @Path("/{userId}")
    public Response listViewers(@PathParam("userId") long userId){
        return Response.ok().entity(viewTrackerRepo.listViewersOf(userId,10, now().minusDays(10))).build();
    }

    @POST
    public Response logView(View view){
        viewTrackerRepo.logView(view.getUserId(), view.getViewerId(), view.getViewedOn());
        return Response.ok().build();
    }
}
