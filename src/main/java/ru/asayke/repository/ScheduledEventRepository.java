package ru.asayke.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asayke.entity.ScheduledEvent;

@Repository
public interface ScheduledEventRepository extends JpaRepository<ScheduledEvent, Long> {
}
