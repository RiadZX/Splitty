package server.services;
import commons.EventLongPollingWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.EventRepository;
import commons.Event;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
public class EventService {

    private final EventRepository eventRepository;

    /**
     * This is a hashmap that stores all the listeners for new events.
     * The key is a randomly created object to identify the listener.
     * The value is a consumer that accepts an event, it is the deferredresult that will be accepted with a new event.
     */
    private Map<Object, Consumer<EventLongPollingWrapper>> listeners = new ConcurrentHashMap<>();

    @Autowired
    public EventService(EventRepository eventRepository){
        this.eventRepository=eventRepository;
    }

    public void newEventLastActivity(UUID eventID) {
        Event event = eventRepository.findById(eventID).orElse(null);
        if (event != null) {
            event.setLastActivityTime(Instant.now());
            eventRepository.save(event);
            EventLongPollingWrapper wrapper=new EventLongPollingWrapper("PUT", event);
            System.out.println(wrapper);
            accept(wrapper);
        }
    }

    public void accept(EventLongPollingWrapper wrapper){
        System.out.println("accepting!!!");
        System.out.println(wrapper);
        System.out.println("!!!!!");
        listeners.values().forEach(listener -> listener.accept(wrapper)); // notify all listeners of a new event
        System.out.println("Accepted");
    }

    public void subscribe(DeferredResult<ResponseEntity<EventLongPollingWrapper>> deferredResult){
        Object key = new Object();
        listeners.put(key, wrapper -> {
            System.out.println(wrapper);
            System.out.println(wrapper.getAction());
            deferredResult.setResult(ResponseEntity.ok(wrapper));
        });
        deferredResult.onCompletion(() -> {
            listeners.remove(key);
        });
        deferredResult.onError((Throwable t) -> {
            System.out.println("Error deferredResult : " + t.getMessage());
            listeners.remove(key);
        });
        deferredResult.onTimeout(() -> {
            deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        });
    }
}
