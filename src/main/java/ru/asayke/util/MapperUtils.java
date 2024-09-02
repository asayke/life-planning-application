package ru.asayke.util;

import ru.asayke.dto.ApplicationUserDto;
import ru.asayke.dto.auth.RegistrationRequest;
import ru.asayke.dto.ScheduledEventDto;
import ru.asayke.entity.ApplicationUser;
import ru.asayke.entity.ScheduledEvent;
import ru.asayke.entity.enums.Role;
import ru.asayke.entity.enums.Status;

public class MapperUtils {

    public static ApplicationUser enrichUserFromRegistrationDto(RegistrationRequest registrationRequest) {
        ApplicationUser applicationUser = new ApplicationUser();

        applicationUser.setUsername(registrationRequest.getUsername());
        applicationUser.setFirstName(registrationRequest.getFirstName());
        applicationUser.setSurname(registrationRequest.getSurname());
        applicationUser.setPatronymic(registrationRequest.getPatronymic());
        applicationUser.setPassword(registrationRequest.getPassword());
        applicationUser.setEmail(registrationRequest.getEmail());
        applicationUser.setStatus(Status.ACTIVE);
        applicationUser.setRole(Role.ROLE_USER);

        return applicationUser;
    }

    public static ScheduledEventDto convertScheduledEventToDto(ScheduledEvent scheduledEvent) {
        ScheduledEventDto scheduledEventDto = new ScheduledEventDto();

        scheduledEventDto.setId(scheduledEvent.getId());
        scheduledEventDto.setTitle(scheduledEvent.getTitle());
        scheduledEventDto.setDescription(scheduledEvent.getDescription());
        scheduledEventDto.setPriority(scheduledEvent.getPriority());
        scheduledEventDto.setHasPassed(scheduledEvent.getHasPassed());
        scheduledEventDto.setDate(scheduledEvent.getDate().toString());

        return scheduledEventDto;
    }

    public static ApplicationUserDto convertUserToDto(ApplicationUser applicationUser) {
        ApplicationUserDto userDto = new ApplicationUserDto();

        userDto.setId(applicationUser.getId());
        userDto.setEmail(applicationUser.getEmail());
        userDto.setUsername(applicationUser.getUsername());
        userDto.setFirstName(applicationUser.getFirstName());
        userDto.setSurname(applicationUser.getSurname());
        userDto.setPatronymic(applicationUser.getPatronymic());
        userDto.setRole(applicationUser.getRole());
        userDto.setStatus(applicationUser.getStatus());

        return userDto;
    }
}
