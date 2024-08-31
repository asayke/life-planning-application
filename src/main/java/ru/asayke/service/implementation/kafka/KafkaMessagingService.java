package ru.asayke.service.implementation.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.asayke.dto.kafka.EmailEvent;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaMessagingService {
    KafkaTemplate<String, EmailEvent> kafkaTemplate;
    NewTopic topic;

    public void sendMessage(EmailEvent kafkaCommonDTO) {
        Message<EmailEvent> message = MessageBuilder
                .withPayload(kafkaCommonDTO)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();

        kafkaTemplate.send(message);
        log.info(String.format("Sending %s in %s", kafkaCommonDTO, topic.name()));
    }
}
