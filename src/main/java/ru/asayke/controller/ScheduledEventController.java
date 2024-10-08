package ru.asayke.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.asayke.dto.ScheduledEventDto;
import ru.asayke.service.interfaces.ScheduledEventService;

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

    @GetMapping("/{sortOrder}/{sortBy}")
    public ResponseEntity<List<ScheduledEventDto>> findAllByCurrentUserWithSorting(Principal principal, @PathVariable(value = "sortOrder", required = false) String sortOrder, @PathVariable(value = "sortBy", required = false) String sortBy) {
        return ResponseEntity.ok(scheduledEventService.findAllByCurrentUserWithSorting(principal.getName(), sortOrder, sortBy));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createNewScheduledEvent(@RequestBody ScheduledEventDto scheduledEventDto, Principal principal) {
        scheduledEventService.createNewScheduledEvent(scheduledEventDto, principal.getName());
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateEvent(@RequestBody ScheduledEventDto scheduledEventDto, Principal principal) {
        scheduledEventService.updateEvent(scheduledEventDto, principal.getName());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
