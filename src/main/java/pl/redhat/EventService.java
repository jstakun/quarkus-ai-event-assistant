package pl.redhat;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class EventService {
 
    @Inject
    EventRepository eventRepository;    

    //@Tool("Search for events based on extracted details from user input")
    @Tool("Znajdź wydarzenia w oparciu o informacje podane przez użytkownika")
    @Transactional
    public List<Event> searchEvents(@P(required = false, value = "0") Double latitude, @P(required = false, value = "0") Double longitude, @P(required = false, value = "typ wydarzenia") String type, @P(required = false, value = "data rozpoczęcia") String startDate, @P(required = false, value = "data zakończenia") String endDate) {
        return eventRepository.searchEvents(latitude, longitude, type, startDate, endDate);
    }
}
