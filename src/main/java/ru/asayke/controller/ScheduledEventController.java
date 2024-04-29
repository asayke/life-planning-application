package ru.asayke.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.asayke.dto.ScheduledEventDto;
import ru.asayke.service.ScheduledEventService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/event/")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduledEventController {

    ScheduledEventService scheduledEventService;

    @GetMapping
    public ResponseEntity<List<ScheduledEventDto>> findAllByCurrentUser(Principal principal) {
        return ResponseEntity.ok(scheduledEventService.findAllByCurrentUser(principal.getName()));
    }
}
