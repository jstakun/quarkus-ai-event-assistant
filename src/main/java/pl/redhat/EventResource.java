package pl.redhat;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    @Inject
    EventRepository eventRepository;

    private EventAssistant eventAssistant;

    public EventResource(EventAssistant eventAssistant) {
        this.eventAssistant = eventAssistant;
    }

    @GET
    @Path("/search")
    public Response searchEvents(@QueryParam("latitude") Double latitude,
                                 @QueryParam("longitude") Double longitude,
                                 @QueryParam("type") String type,
                                 @QueryParam("startDate") String startDate,
                                 @QueryParam("endDate") String endDate) {
        List<Event> events = eventRepository.searchEvents(latitude, longitude, type, startDate, endDate);
        return Response.ok(events).build();
    }

    @GET
    @Path("/query/{question}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response queryAIEvents(String question) {
        final String response = eventAssistant.assistUser(question);
        return Response.ok(response).build();
    }
}

