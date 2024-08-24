package ru.asayke.service;

import ru.asayke.dto.ScheduledEventDto;
import ru.asayke.entity.ApplicationUser;

import java.util.List;

public interface ScheduledEventService {
    void checkScheduledEvents();

    List<ScheduledEventDto> findAllByCurrentUser(String username);

    void createNewScheduledEvent(ScheduledEventDto scheduledEventDto, String username);
}
