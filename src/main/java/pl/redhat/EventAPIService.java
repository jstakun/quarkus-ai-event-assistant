package pl.redhat;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import io.smallrye.mutiny.Multi;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventAPIService {

    @Inject
    EventRepository eventRepository;

    private EventAssistant eventAssistant;

    public EventAPIService(EventAssistant eventAssistant) {
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
    public Multi<String> queryAIEvents(String question) {
        final Multi<String> response = eventAssistant.assistUser(question);
        return response;
    }
}

