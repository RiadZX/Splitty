package server.api;

import commons.Event;
import commons.Participant;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ParticipantRepository;
import server.services.EventService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events/{eventId}/participants")
public class ParticipantController {

    private final ParticipantRepository repo;

    private final EventService eventService;

    public ParticipantController(ParticipantRepository repo, EventService eventService) {
        this.repo = repo;
        this.eventService=eventService;
    }
    /**
     * Get all participants from an event
     * @param eventId - id of the event
     * @return - list of all participants
     */
    @GetMapping(path = { "", "/" })
    public ResponseEntity<List<Participant>> getAll(@PathVariable("eventId") String eventId) {
        return ResponseEntity.ok(repo.getParticipantsFromEvent(UUID.fromString(eventId)));
    }

    /**
     * Get participant by id
     * @param id - id of participant
     * @param eventId - id of the event
     * @return - participant with id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Participant> getById(@PathVariable("id") UUID id, @PathVariable("eventId") UUID eventId) {
        Participant participant = repo.findParticipantInEvent(eventId, id);
        return ResponseEntity.ok(participant);
    }

    /**
     * Create new participant
     * @param participant - participant to add
     * @return - added participant
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Participant> add(@RequestBody Participant participant, @PathVariable String eventId) {
        //check if participant has empty fields
        if (participant == null || isNullOrEmpty(participant.getName()) || isNullOrEmpty(participant.getIban())
                || isNullOrEmpty(participant.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        //set event part of based on id
        Event event=new Event();
        UUID eventUUID=UUID.fromString(eventId);
        event.setId(eventUUID);
        participant.setEventPartOf(event);
        Participant saved=repo.save(participant);
        eventService.newEventLastActivity(eventUUID);
        return ResponseEntity.ok(saved);
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Participant> remove(@PathVariable("id") String id, @PathVariable String eventId) {
        Participant participant= repo.findParticipantInEvent(UUID.fromString(eventId), UUID.fromString(id));
        if (participant != null) {
            ResponseEntity<Participant> removedParticipant = ResponseEntity.ok(participant);
            UUID eventUUID=UUID.fromString(eventId);
            repo.deleteParticipantFromEvent(eventUUID, UUID.fromString(id));
            eventService.newEventLastActivity(eventUUID);
            return removedParticipant;
        }
        return ResponseEntity.badRequest().build();
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Update participant by id
     * @param id - id of participant
     * @param participant - participant to update
     * @return - updated participant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Participant> update(@PathVariable("eventId") String eventId, @PathVariable("id") String id, @RequestBody Participant participant) {
        if (participantExists(eventId, id)) {
            participant.setEventPartOf(new Event(eventId));
            UUID eventUUID=UUID.fromString(eventId);
            participant.setId(eventUUID);
            var saved=repo.save(participant);
            eventService.newEventLastActivity(eventUUID);
            return ResponseEntity.ok(saved);
        }
        return ResponseEntity.badRequest().build();
    }


    private boolean participantExists(String eventId, String id){
        return repo.findParticipantInEvent(UUID.fromString(eventId), UUID.fromString(id)) != null;
    }
}
