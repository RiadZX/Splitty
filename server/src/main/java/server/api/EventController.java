package server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import commons.Event;
import commons.EventLongPollingWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.services.EventService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service= service;
    }
    /**
     * Get all events
     * @return - list of all events
     */
    @GetMapping(path = { "", "/" })
    public ResponseEntity<List<Event>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Add event
     * @param event - event to add
     * @return - added event
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> add(@RequestBody Event event) {
        if (event==null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.add(event));
    }


    /**
     * Join an event
     * @param inviteCode - invite code for the event
     * @return - found event
     */
    @GetMapping("/join/{inviteCode}")
    public ResponseEntity<Event> join(@PathVariable("inviteCode") String inviteCode) {
        Event saved=service.joinEvent(inviteCode);
        if (saved==null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(saved);
    }
    /**
     * Get event by id
     * @param id - id of event
     * @return - event with id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") UUID id) {
        Event saved=service.get(id);
        if (saved==null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(saved);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable("id") UUID id) {
        if (this.service.delete(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    @MessageMapping("/events")
    @SendTo("/topic/events")
    public Event updateTitle(Event event) throws JsonProcessingException {

        System.out.println("It works! " + event.getId());
        return event;
    }
    /**
     * Update event
     * @param id - id of event
     * @param event - event to update
     * @return - updated event
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> update(@PathVariable("id") UUID id, @RequestBody Event event) {
        Event saved=service.update(id, event);
        if (saved!=null) {
            return ResponseEntity.ok(saved);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Subscribe to listen for new events
     * @return - new event.
     */
    @GetMapping("/subscribe")
    public DeferredResult<ResponseEntity<EventLongPollingWrapper>> subscribe() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        DeferredResult<ResponseEntity<EventLongPollingWrapper>> deferredResult = new DeferredResult<>(// deferred result is a result that is not completed yet.
                30000L, //If within 30 seconds the result is not set, the request will be timed out
                noContent//if not found then this code will be returned
        );
        service.subscribe(deferredResult);
        return deferredResult;
    }

}
