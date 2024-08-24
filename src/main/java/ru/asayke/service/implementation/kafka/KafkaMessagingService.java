package ru.asayke.service.implementation.kafka;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.asayke.dto.kafka.EmailMessageKafkaDTO;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaMessagingService {
    KafkaTemplate<String, EmailMessageKafkaDTO> kafkaTemplate;

    public void sendMessage(String topicName, EmailMessageKafkaDTO kafkaCommonDTO) {
        log.info(String.format("Sending %s in %s", kafkaCommonDTO.toString(), topicName));
        kafkaTemplate.send(topicName, kafkaCommonDTO);
    }
}
