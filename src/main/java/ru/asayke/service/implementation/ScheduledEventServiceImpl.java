package ru.asayke.service.implementation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asayke.dto.ScheduledEventDto;
import ru.asayke.dto.kafka.EmailEvent;
import ru.asayke.entity.ApplicationUser;
import ru.asayke.entity.ScheduledEvent;
import ru.asayke.exception.ApplicationUserNotFoundException;
import ru.asayke.repository.ApplicationUserRepository;
import ru.asayke.repository.ScheduledEventRepository;
import ru.asayke.service.implementation.kafka.KafkaMessagingService;
import ru.asayke.service.interfaces.ScheduledEventService;
import ru.asayke.util.MapperUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduledEventServiceImpl implements ScheduledEventService {
    ScheduledEventRepository scheduledEventRepository;
    ApplicationUserRepository applicationUserRepository;
    KafkaMessagingService kafkaMessagingService;

    @Override
    @Scheduled(cron = "0 * * * * *")
    public void checkScheduledEvents() {
        List<ScheduledEvent> scheduledEvents = scheduledEventRepository
                .findAllByDateBeforeAndHasPassed(Date.from(Instant.now()), false);

        for (ScheduledEvent scheduledEvent : scheduledEvents) {
            ApplicationUser applicationUser = scheduledEvent.getApplicationUser();
            scheduledEvent.setHasPassed(true);
            scheduledEventRepository.save(scheduledEvent);

            EmailEvent kafkaDTO = new EmailEvent(
                    applicationUser.getEmail(),
                    "You're wrong!",
                    String.format("Your event with title %s has already expired!", scheduledEvent.getTitle())
            );

            kafkaMessagingService.sendMessage(kafkaDTO);
        }
    }

    @Override
    public List<ScheduledEventDto> findAllByCurrentUser(String username) {
        ApplicationUser applicationUser = findUserOrThrowException(username);

        return scheduledEventRepository
                .findAllByApplicationUser(applicationUser)
                .stream()
                .map(MapperUtils::convertScheduledEventToDto)
                .toList();
    }

    @Override
    @Transactional
    public void createNewScheduledEvent(ScheduledEventDto scheduledEventDto, String username) {
        ApplicationUser applicationUser = findUserOrThrowException(username);

        ScheduledEvent scheduledEvent = new ScheduledEvent();
        scheduledEvent.setTitle(scheduledEventDto.getTitle());
        scheduledEvent.setDescription(scheduledEventDto.getDescription());
        scheduledEvent.setPriority(scheduledEventDto.getPriority());
        scheduledEvent.setHasPassed(scheduledEventDto.getHasPassed());
        scheduledEvent.setApplicationUser(applicationUser);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

        try {
            Date date = dateFormat.parse(scheduledEventDto.getDate());
            scheduledEvent.setDate(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        scheduledEventRepository.save(scheduledEvent);
    }

    private ApplicationUser findUserOrThrowException(String username) {
        return applicationUserRepository
                .findByUsername(username)
                .orElseThrow(() -> new ApplicationUserNotFoundException(String.format("User with username %s is not found", username)));
    }
}
