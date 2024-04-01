package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tag_id")
    private UUID id;

    @Expose
    private String tag;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonBackReference("event-tags")
    private Event event;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<Expense> expenses;

    public Tag() {
    }

    public Tag(String tag, Event event) {
        this();
        this.tag = tag;
        this.event = event;
    }

    public Tag(String tagName) {
        this.tag = tagName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Event getEvent() {
        return event;
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
