package ru.asayke.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.asayke.entity.enums.Priority;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduledEventDto {
    Long id;

    String title;

    String description;

    String date;

    Priority priority;

    Boolean hasPassed = false;
}
