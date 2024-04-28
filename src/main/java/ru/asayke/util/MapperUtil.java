package ru.asayke.util;

import ru.asayke.dto.RegistrationRequest;
import ru.asayke.entity.ApplicationUser;
import ru.asayke.entity.enums.Role;
import ru.asayke.entity.enums.Status;

public class MapperUtil {

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

    public static ApplicationUser convertApplicationUserDtoToApplicationUser() {
        return new ApplicationUser();
    }
}
