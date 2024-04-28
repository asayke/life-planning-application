package ru.asayke.service.implementation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.asayke.repository.ScheduledEventRepository;
import ru.asayke.service.ScheduledEventService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduledEventServiceImpl implements ScheduledEventService {
    ScheduledEventRepository scheduledEventRepository;
}
