package pl.redhat;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
public class EventRepository implements PanacheRepository<Event> {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.of("pl"));
    
    @PersistenceContext
    EntityManager entityManager;

    public List<Event> searchEvents(Double latitude, Double longitude, String type, String startDateStr, String endDateStr) {
        StringBuilder queryStr = new StringBuilder("SELECT e FROM Event e WHERE 1=1");

        Date startDate = null;
        if (startDateStr != null) {
            try {
                startDate = formatter.parse(startDateStr);
            } catch (Exception e) {
                Log.error("Invalid date format " + startDateStr);
            }
        }

        Date endDate = null;
        if (endDateStr != null) {
            try {
                endDate = formatter.parse(endDateStr);
            } catch (Exception e) {
                Log.error("Invalid date format " + endDateStr);
            }
        }
        
        if (latitude != null && longitude != null && latitude != 0 && longitude != 0) {
            //queryStr.append(" AND e.latitude = :latitude AND e.longitude = :longitude");
            queryStr.append(" AND ABS(e.latitude - :latitude) < 0.5 AND ABS(e.longitude - :longitude) < 0.5");
        }
        if (type != null && !type.isEmpty()) {
            queryStr.append(" AND e.type = :type");
        }
        if (startDate != null) {
            queryStr.append(" AND e.eventDate >= :startDate");
        }
        if (endDate != null) {
            queryStr.append(" AND e.eventDate <= :endDate");
        }
        
        TypedQuery<Event> query = entityManager.createQuery(queryStr.toString(), Event.class);
        
        if (latitude != null && longitude != null && latitude != 0 && longitude != 0) { 
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
        }
        if (type != null && !type.isEmpty() && !type.equals("Any")) {
            query.setParameter("type", type.toLowerCase(Locale.of("pl")));
        }
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }

        List<Event> resultList = query.getResultList();
        
        Log.info("Found " + resultList.size() + " events");

        return resultList;
    }
}
