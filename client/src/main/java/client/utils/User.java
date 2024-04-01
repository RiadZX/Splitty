package client.utils;


import commons.Participant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class User implements Serializable {

    enum Currency {
        USD,
        EUR,
        CHF,
        RON,
    }

    private Currency prefferedCurrency;

    private String name;

    private String email;

    private String iban;

    private  String bic;

    private LinkedHashMap<UUID, UUID> eventParticipant;

    public User(){
        eventParticipant=new LinkedHashMap<UUID, UUID>();
    }

    /**
     * Only use this for testing
     * @param name name of the user
     */
    public User(String name, String preferredCurrency){
        this();
        this.name=name;
        this.prefferedCurrency = Currency.valueOf(preferredCurrency);
        this.email="test@test.com";
        this.iban="GB12ABCD10203012345678";
        this.bic="AAAABBCCDD";
    }
    public User(String name, String email, String iban, String bic){
        this();
        this.name=name;
        this.email=email;
        this.iban=iban;
        this.bic=bic;
    }

    public Participant createParticipant(){
        return new Participant(name, null, iban, email, bic);
    }

    public String getName() {
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<UUID, UUID> getEventParticipant() {
        return eventParticipant;
    }

    public List<UUID> getEvents(){
        return this.eventParticipant.keySet().stream().toList();
    }

    public boolean eventExists(UUID eventId){
        return  this.eventParticipant.containsKey(eventId);
    }

    public void setEventParticipant(LinkedHashMap<UUID, UUID> eventParticipant) {
        this.eventParticipant = eventParticipant;
    }

    public void addEventParticipant(UUID event, UUID participant){
        this.eventParticipant.put(event, participant);
    }

    @Override
    public String toString() {
        StringBuilder s=new StringBuilder(this.name+"\n");
        this.eventParticipant.forEach((key, value) -> {
            s.append("Event:").append(key).append(" Participant:").append(value).append("\n");
        });
        return  s.toString();
    }
}
