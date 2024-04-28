package ru.asayke.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.asayke.entity.enums.Role;
import ru.asayke.entity.enums.Status;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "application_user")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "username", unique = true)
    String username;

    @Column(name = "fist_name")
    String firstName;

    @Column(name = "surname")
    String surname;

    @Column(name = "patronymic")
    String patronymic;

    @Column(name = "email", unique = true)
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    Role role;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    Status status = Status.ACTIVE;

    @OneToMany
    @JoinColumn(name = "application_user_id")
    List<ScheduledEvent> scheduledEvents;
}
