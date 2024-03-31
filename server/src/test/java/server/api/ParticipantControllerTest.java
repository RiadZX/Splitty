package server.api;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import commons.Participant;
import server.services.ParticipantService;

public class ParticipantControllerTest {

    private ParticipantController participantController;
    private ParticipantService service;

    @BeforeEach
    public void setUp() {
        service = mock(ParticipantService.class);
        participantController = new ParticipantController(service);
    }

    @Test
    public void testGetAllParticipants() {
        List<Participant> participants = new ArrayList<>();
        // Add some sample participants
        participants.add(new Participant());
        participants.add(new Participant());

        // Mock the behavior of the repository
        when(service.getAll(any(UUID.class))).thenReturn(participants);
        UUID eventid=UUID.randomUUID();
        ResponseEntity<List<Participant>> response = participantController.getAll(eventid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participants.size(), response.getBody().size());
    }

    @Test
    public void testGetParticipantById() {
        UUID participantId = UUID.randomUUID();
        Participant participant = new Participant();
        participant.setId(participantId);

        // Mock the behavior of the repository
        when(service.participantExists(any(UUID.class), any(UUID.class))).thenReturn(true);
        when(service.getById(any(UUID.class), any(UUID.class))).thenReturn(participant);

        ResponseEntity<Participant> response = participantController.getById(participantId, UUID.randomUUID());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participantId, response.getBody().getId());
    }

    @Test
    public void testAddParticipant() {
        Participant participantToAdd = new Participant();
        participantToAdd.setName("John");
        participantToAdd.setIban("1234");
        participantToAdd.setEmail("john@example.com");

        UUID eventId = UUID.randomUUID();

        // Mock the behavior of the repository
        when(service.add(any(Participant.class), any(UUID.class))).thenReturn(participantToAdd);

        ResponseEntity<Participant> response = participantController.add(participantToAdd, eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participantToAdd, response.getBody());
    }

    @Test
    public void testRemoveParticipant() {
        UUID participantId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Participant participant = new Participant();
        participant.setId(participantId);

        // Mock the behavior of the repository
        when(service.remove(any(UUID.class), any(UUID.class))).thenReturn(participant);

        ResponseEntity<Participant> response = participantController.remove(participantId.toString(), eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participant, response.getBody());
        verify(service, times(1)).remove(any(UUID.class), any(UUID.class));
    }

    @Test
    public void testUpdateParticipant() {
        UUID participantId = UUID.randomUUID();
        Participant participantToUpdate = new Participant();
        participantToUpdate.setName("Updated Name");
        UUID eventId = UUID.randomUUID();

        // Mock the behavior of the repository
        when(service.update(any(UUID.class), any(UUID.class), any(Participant.class))).thenReturn(participantToUpdate);

        ResponseEntity<Participant> response = participantController.update(eventId, participantId, participantToUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participantToUpdate.getName(), response.getBody().getName());
    }
}
