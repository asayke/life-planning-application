package ru.asayke.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.asayke.entity.enums.Role;
import ru.asayke.entity.enums.Status;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationUserDto {
    Long id;

    String username;

    String firstName;

    String surname;

    String patronymic;

    String email;

    Role role;

    Status status = Status.ACTIVE;
}
