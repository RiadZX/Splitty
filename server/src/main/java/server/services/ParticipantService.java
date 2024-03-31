package server.services;

import commons.Event;
import commons.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {
    private final ParticipantRepository repo;
    private final EventService eventService;
    @Autowired
    public ParticipantService(ParticipantRepository participantRepository, EventService eventService){
        this.repo =participantRepository;
        this.eventService = eventService;
    }
    public List<Participant> getAll(UUID eventID){
        return repo.getParticipantsFromEvent(eventID);
    }

    public Participant getById(UUID pID, UUID eID){
        Participant p = repo.findParticipantInEvent(eID, pID);
        return p;
    }

    public Participant add(Participant p, UUID eventID){
        //check if participant has empty fields
        if (p == null || isNullOrEmpty(p.getName()) || isNullOrEmpty(p.getIban())
                || isNullOrEmpty(p.getEmail())) {
            return null;
        }
        //set event part of based on id
        Event event = new Event();
        event.setId(eventID);
        p.setEventPartOf(event);
        Participant saved = repo.save(p);
        eventService.newEventLastActivity(eventID);
        return saved;
    }
    public Participant remove(UUID pID, UUID eID){
        Participant participant = repo.findParticipantInEvent(eID, pID);
        if (participant != null) {
            repo.deleteParticipantFromEvent(eID, pID);
            eventService.newEventLastActivity(eID);
            return participant;
        }
        return null;
    }

    public Participant update(UUID pID, UUID eID, Participant participant){
        if (participantExists(eID, pID)) {
            participant.setEventPartOf(new Event(eID));
            Participant saved = repo.save(participant);
            eventService.newEventLastActivity(eID);
            return saved;
        }
        return null;
    }


    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public boolean participantExists(UUID eventId, UUID id) {
        return repo.findParticipantInEvent(eventId, id) != null;
    }
}

