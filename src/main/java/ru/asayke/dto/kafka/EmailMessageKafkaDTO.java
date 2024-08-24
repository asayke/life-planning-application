package ru.asayke.dto.kafka;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailMessageKafkaDTO {
    String email;
    String message;

    public EmailMessageKafkaDTO(String email, String message) {
        this.email = email;
        this.message = message;
    }

    @Override
    public String toString() {
        return "NotificationKafkaDTO{" +
                "message='" + message + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
