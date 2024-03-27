package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantTest {
    private Participant p1, p2, p3, p4, p5, p6;

    @BeforeEach
    public void testSetup(){
        p1 = new Participant("a", new Event());
        p2 = new Participant("a", new Event());
        p3 = new Participant("b", new Event());
        p4= new Participant("c", new Event(), "NL32RABO4907152752", "example@com", "TMBQBHDGXXX");
        p5= new Participant();
        p6= new Participant("test");
    }

    @Test
    public void testConstructor(){
        assertEquals(p4.getName(), "c");
        assertEquals(p4.getEvent(), new Event());
        assertEquals(p4.getIban(), "NL32RABO4907152752");
        assertEquals(p4.getBic(), "TMBQBHDGXXX");
        assertEquals(p4.getEmail(), "example@com");
        assertNull(p4.getId());
        assertNull(p5.getName());
    }

    @Test
    public void testEqualsMethod(){
        assertEquals(p1, p1);
        assertNotEquals(p1, null);
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p1,p4);
        assertNotEquals(p4, null);
    }

    @Test
    public void testHashMethod(){
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
        assertNotEquals(p1.hashCode(), p4.hashCode());
    }

    @Test
    public void setEmail(){
        p1.setEmail("aerrq@yahoo.com");
        assertEquals(p1.getEmail(),"aerrq@yahoo.com");
    }

    @Test
    public void setBic(){
        p1.setBic("TMBQBHDGXYY");
        assertEquals(p1.getBic(),"TMBQBHDGXYY");
    }

    @Test
    public void setIban(){
        p1.setIban("NL32RABB5907152752");
        assertEquals(p1.getIban(),"NL32RABB5907152752");
    }

    @Test
    public void setName(){
        p6.setName("tessssst");
        assertEquals(p6.getName(),"tessssst");
    }

    @Test
    public void setID(){
        UUID tmp=UUID.randomUUID();
        p6.setId(tmp);
        assertEquals(p6.getId(),tmp);
    }

    @Test
    public void setEventPartOf(){
        Event tmp=new Event("test");
        assertNull(p6.getEvent());
        p6.setEventPartOf(tmp);
        assertEquals(p6.getEvent(),tmp);
    }

    @Test
    public void setDebts(){
        List<Debt> tmp=List.of(new Debt(),new Debt());
        assertNull(p6.getDebts());
        p6.setDebts(tmp);
        assertEquals(p6.getDebts(),tmp);
    }

    @Test void payFor(){
        List<Expense> temp=new ArrayList<>();
        assertEquals(p6.getPaidFor(),temp);
        p6.payFor(new Expense());
        temp.add(new Expense());
        assertEquals(p6.getPaidFor(), temp);
    }


    @Test
    public void testToString(){
        assertTrue(p1.toString().contains(p1.getName()));
    }
}
