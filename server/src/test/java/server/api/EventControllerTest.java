package server.api;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import commons.Event;
import server.services.EventService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class EventControllerTest {

    private EventService service;
    private EventController eventController;

    @BeforeEach
    void setUp() {
        service = mock(EventService.class);
        eventController=new EventController(service);
    }

    @Test
    void testGetAll() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("Event 1"));
        events.get(0).setId(UUID.randomUUID());
        events.add(new Event("Event 2"));
        events.get(1).setId(UUID.randomUUID());
        when(service.getAll()).thenReturn(events);

        ResponseEntity<List<Event>> responseEntity = eventController.getAll();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(events, responseEntity.getBody());
    }

    @Test
    void testAdd() {
        Event event = new Event("New Event");
        event.setId(UUID.randomUUID());
        when(service.add(any(Event.class))).thenReturn(event);

        ResponseEntity<Event> responseEntity = eventController.add(event);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(event, responseEntity.getBody());
    }
    @Test
    void testJoinExistingEvent() {
        Event event = new Event("Event 1");
        event.setId(UUID.randomUUID());
        when(service.joinEvent("inviteCode")).thenReturn(event);

        ResponseEntity<Event> responseEntity = eventController.join("inviteCode");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(event, responseEntity.getBody());
    }

    @Test
    void testJoinNonExistingEvent() {
        when(service.joinEvent("invalidInviteCode")).thenReturn(null);

        ResponseEntity<Event> responseEntity = eventController.join("invalidInviteCode");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    @Test
    void testGetByIdExistingEvent() {
        UUID eventId = UUID.randomUUID();
        Event event = new Event("Event 1");
        event.setId(UUID.randomUUID());
        when(service.existsById(eventId)).thenReturn(true);
        when(service.get(eventId)).thenReturn(event);

        ResponseEntity<Event> responseEntity = eventController.getById(eventId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(event, responseEntity.getBody());
    }

    @Test
    void testGetByIdNonExistingEvent() {
        UUID eventId = UUID.randomUUID();
        when(service.existsById(eventId)).thenReturn(false);

        ResponseEntity<Event> responseEntity = eventController.getById(eventId);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    //TODO Adapt the test to reflect the new return value of the remove Event endpoint
//    @Test
//    void testRemoveExistingEvent() {
//        UUID eventId = UUID.randomUUID();
//        Event event = new Event("Event 1");
//        event.setId(UUID.randomUUID());
//        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
//
//        ResponseEntity<Event> responseEntity = eventController.remove(eventId);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(event, responseEntity.getBody());
//
//        verify(eventRepository, times(1)).deleteById(eventId);
//    }
//    @Test
//    void testRemoveNonExistingEvent() {
//        UUID eventId = UUID.randomUUID();
//        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
//
//        ResponseEntity<Event> responseEntity = eventController.remove(eventId);
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//    }

    @Test
    void testUpdateExistingEvent() {
        UUID eventId = UUID.randomUUID();
        Event event = new Event("Event 1");
        event.setId(eventId);
        when(service.existsById(eventId)).thenReturn(true);
        when(service.update(any(UUID.class), any(Event.class))).thenReturn(event);

        ResponseEntity<Event> responseEntity = eventController.update(eventId, event);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(event, responseEntity.getBody());
    }

    @Test
    void testUpdateNonExistingEvent() {
        UUID eventId = UUID.randomUUID();
        Event event = new Event("Event 1");
        event.setId(eventId);
        when(service.existsById(eventId)).thenReturn(false);

        ResponseEntity<Event> responseEntity = eventController.update(eventId, event);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
