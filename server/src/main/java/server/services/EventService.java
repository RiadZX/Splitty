package server.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import commons.Event;

import java.time.Instant;
import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository){
        this.eventRepository=eventRepository;
    }

    public void newEventLastActivity(UUID eventID) {
        Event event = eventRepository.findById(eventID).orElse(null);
        if (event != null) {
            event.setLastActivityTime(Instant.now());
            eventRepository.saveAndFlush(event);
        }
    }
}
