package server.database;

import commons.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import  org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.UUID;


public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
    @Query("SELECT p FROM Participant p WHERE p.event.id = :eventID")
    List<Participant> getParticipantsFromEvent(UUID eventID);

    @Query("SELECT p FROM Participant p WHERE p.event.id = :eventID AND  p.id= :participantID")
    Participant findParticipantInEvent(UUID eventID, UUID participantID);

    @Modifying
    @Query("DELETE Participant p WHERE p.event.id = :eventID AND p.id= :participantID ")
    void deleteParticipantFromEvent(UUID eventID, UUID participantID);
}

