package ru.asayke.service.interfaces;

import ru.asayke.dto.ScheduledEventDto;
import ru.asayke.entity.ApplicationUser;

import java.util.List;

public interface ScheduledEventService {
    void checkScheduledEvents();

    List<ScheduledEventDto> findAllByCurrentUser(String username);

    void createNewScheduledEvent(ScheduledEventDto scheduledEventDto, String username);

    List<ScheduledEventDto> findAllByCurrentUserWithSorting(String name, String sortOrder, String sortBy);

    ScheduledEventDto updateEvent(ScheduledEventDto scheduledEventDto, String username);
}
