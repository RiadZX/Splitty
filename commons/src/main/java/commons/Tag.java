package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue
    @Expose
    private long id;

    @Expose
    private String tag;

    @ManyToOne
    @JsonBackReference("event-tags")
    private Event event;

    public Tag(String tag, Event event) {
        this.tag = tag;
        this.event = event;
    }

    public Tag() {
    }

    public Tag(String tagName) {
        this.tag = tagName;
    }

    public String getTag() {
        return tag;
    }

    public Event getEvent() {
        return event;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Tag tag1 = (Tag) o;
        return Objects.equals(tag, tag1.getTag()) && Objects.equals(event, tag1.getEvent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, event);
    }
}
