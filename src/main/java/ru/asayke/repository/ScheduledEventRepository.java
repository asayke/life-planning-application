package ru.asayke.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asayke.entity.ApplicationUser;
import ru.asayke.entity.ScheduledEvent;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduledEventRepository extends JpaRepository<ScheduledEvent, Long> {
    List<ScheduledEvent> findAllByDateBeforeAndHasPassed(Date date, boolean hasPassed);

    List<ScheduledEvent> findAllByApplicationUser(ApplicationUser applicationUser);
}
