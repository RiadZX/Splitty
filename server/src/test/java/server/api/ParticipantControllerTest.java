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

import server.api.ParticipantController;
import server.database.ParticipantRepository;
import commons.Participant;

public class ParticipantControllerTest {

    private ParticipantController participantController;
    private ParticipantRepository participantRepository;

    @BeforeEach
    public void setUp() {
        participantRepository = mock(ParticipantRepository.class);
        participantController = new ParticipantController(participantRepository);
    }

    @Test
    public void testGetAllParticipants() {
        List<Participant> participants = new ArrayList<>();
        // Add some sample participants
        participants.add(new Participant());
        participants.add(new Participant());

        // Mock the behavior of the repository
        when(participantRepository.getParticipantsFromEvent(any(UUID.class))).thenReturn(participants);
        UUID eventid=UUID.randomUUID();
        ResponseEntity<List<Participant>> response = participantController.getAll(eventid.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participants.size(), response.getBody().size());
    }

    @Test
    public void testGetParticipantById() {
        UUID participantId = UUID.randomUUID();
        Participant participant = new Participant();
        participant.setId(participantId);

        // Mock the behavior of the repository
        when(participantRepository.findParticipantInEvent(any(UUID.class), any(UUID.class))).thenReturn(participant);

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
        when(participantRepository.save(any(Participant.class))).thenReturn(participantToAdd);

        ResponseEntity<Participant> response = participantController.add(participantToAdd, eventId.toString());

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
        when(participantRepository.findParticipantInEvent(any(UUID.class), any(UUID.class))).thenReturn(participant);

        ResponseEntity<Participant> response = participantController.remove(participantId.toString(), eventId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participant, response.getBody());
        verify(participantRepository, times(1)).deleteParticipantFromEvent(any(UUID.class), any(UUID.class));
    }

    @Test
    public void testUpdateParticipant() {
        UUID participantId = UUID.randomUUID();
        Participant participantToUpdate = new Participant();
        participantToUpdate.setName("Updated Name");

        UUID eventId = UUID.randomUUID();

        // Mock the behavior of the repository
        when(participantRepository.findParticipantInEvent(any(UUID.class), any(UUID.class))).thenReturn(new Participant());
        when(participantRepository.save(any(Participant.class))).thenReturn(participantToUpdate);

        ResponseEntity<Participant> response = participantController.update(eventId.toString(), participantId.toString(), participantToUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(participantToUpdate.getName(), response.getBody().getName());
    }
}
