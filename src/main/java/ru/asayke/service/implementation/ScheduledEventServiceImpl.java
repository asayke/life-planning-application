package ru.asayke.service.implementation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.asayke.dto.ScheduledEventDto;
import ru.asayke.entity.ApplicationUser;
import ru.asayke.entity.ScheduledEvent;
import ru.asayke.exception.ApplicationUserNotFoundException;
import ru.asayke.repository.ApplicationUserRepository;
import ru.asayke.repository.ScheduledEventRepository;
import ru.asayke.service.ScheduledEventService;
import ru.asayke.util.MapperUtils;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduledEventServiceImpl implements ScheduledEventService {
    ScheduledEventRepository scheduledEventRepository;
    ApplicationUserRepository applicationUserRepository;

    @Override
    @Scheduled(cron = "0 * * * * *")
    public void checkScheduledEvents() {
        List<ScheduledEvent> scheduledEvents = scheduledEventRepository
                .findAllByDateBeforeAndHasPassed(Date.from(Instant.now()), false);

        for (ScheduledEvent scheduledEvent : scheduledEvents) {
            // TODO do some good things there
        }
    }

    @Override
    public List<ScheduledEventDto> findAllByCurrentUser(String username) {
        ApplicationUser applicationUser = applicationUserRepository
                .findByUsername(username)
                .orElseThrow(() -> new ApplicationUserNotFoundException(String.format("User with username %s is not found", username)));

        return scheduledEventRepository
                .findAllByApplicationUser(applicationUser)
                .stream()
                .map(MapperUtils::convertScheduledEventToDto)
                .toList();
    }
}
