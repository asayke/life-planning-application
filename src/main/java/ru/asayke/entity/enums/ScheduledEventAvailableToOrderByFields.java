package ru.asayke.entity.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ScheduledEventAvailableToOrderByFields {
    TITLE("title"),
    DATE("date"),
    PRIORITY("priority"),
    HAS_PASSED("hasPassed");

    String name;
}
