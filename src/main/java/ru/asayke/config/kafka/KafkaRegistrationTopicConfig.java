//package ru.asayke.config.kafka;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//
//@Configuration
//public class KafkaRegistrationTopicConfig {
//
//    @Value("${kafka.topic.registration-topic}")
//    private String topicName;
//
//    @Bean
//    public NewTopic registrationTopic() {
//        return TopicBuilder
//                .name(topicName)
//                .build();
//    }
//}
