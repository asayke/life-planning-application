package ru.asayke.entity;

import javax.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.asayke.entity.enums.Priority;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "scheduled_event")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduledEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "date")
    Date date;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    Priority priority;

    @Column(name = "has_passed")
    Boolean hasPassed = false;

    @ManyToOne
    @JoinColumn(name = "application_user_id")
    ApplicationUser applicationUser;
}
