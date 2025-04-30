package ru.asayke.service.implementation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asayke.dto.ScheduledEventDto;
import ru.asayke.dto.kafka.EmailEvent;
import ru.asayke.entity.ApplicationUser;
import ru.asayke.entity.ScheduledEvent;
import ru.asayke.entity.enums.ScheduledEventAvailableToOrderByFields;
import ru.asayke.exception.ApplicationUserNotFoundException;
import ru.asayke.exception.ScheduledEventFieldDoesNotExists;
import ru.asayke.repository.ApplicationUserRepository;
import ru.asayke.repository.ScheduledEventRepository;
import ru.asayke.service.implementation.kafka.KafkaMessagingService;
import ru.asayke.service.interfaces.ScheduledEventService;
import ru.asayke.util.MapperUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduledEventServiceImpl implements ScheduledEventService {
    ScheduledEventRepository scheduledEventRepository;
    ApplicationUserRepository applicationUserRepository;
    KafkaMessagingService kafkaMessagingService;

    @Override
    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void checkScheduledEvents() {
        log.info("---------------Scheduled events checking started---------------");
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
    @Transactional(readOnly = true)
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

    @Override
    @Transactional(readOnly = true)
    public List<ScheduledEventDto> findAllByCurrentUserWithSorting(String username, String sortOrder, String sortBy) {
        boolean fieldIsAvailable = Stream.of(ScheduledEventAvailableToOrderByFields.values())
                .map(Enum::name).anyMatch(field -> field.equals(sortBy));

        if (fieldIsAvailable) {
            throw new ScheduledEventFieldDoesNotExists(String.format("Field with name %s does not exists", sortBy));
        }

        ApplicationUser applicationUser = findUserOrThrowException(username);

        Sort sort = Sort.by(Sort.Direction.valueOf(sortOrder.toUpperCase()), sortBy);

        return scheduledEventRepository
                .findAll(sort)
                .stream()
                .filter(event -> event.getApplicationUser().equals(applicationUser))
                .map(MapperUtils::convertScheduledEventToDto)
                .toList();
    }

    @Override
    @Transactional
    public ScheduledEventDto updateEvent(ScheduledEventDto scheduledEventDto, String username) {
        ApplicationUser applicationUser = findUserOrThrowException(username);

        ScheduledEvent eventById = scheduledEventRepository.findById(scheduledEventDto.getId())
                .orElseThrow(() -> new RuntimeException("Event with such id does not exists"));

        if (!Objects.equals(eventById.getApplicationUser().getId(), applicationUser.getId())) {
            throw new RuntimeException("U cant edit this event");
        }

        eventById.setTitle(scheduledEventDto.getTitle());
        eventById.setDescription(scheduledEventDto.getDescription());
        eventById.setPriority(scheduledEventDto.getPriority());
        eventById.setHasPassed(scheduledEventDto.getHasPassed());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");

        try {
            Date date = dateFormat.parse(scheduledEventDto.getDate());
            eventById.setDate(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ScheduledEvent saved = scheduledEventRepository.save(eventById);

        return MapperUtils.convertScheduledEventToDto(saved);
    }

    private ApplicationUser findUserOrThrowException(String username) {
        return applicationUserRepository
                .findByUsername(username)
                .orElseThrow(() -> new ApplicationUserNotFoundException(String.format("User with username %s is not found", username)));
    }
}
