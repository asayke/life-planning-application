package ru.asayke.dto.kafka;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailMessageKafkaDTO {
    String email;
    String message;

    public EmailMessageKafkaDTO(String email, String message) {
        this.email = email;
        this.message = message;
    }
}
