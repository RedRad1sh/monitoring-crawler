package ru.radish.crawler.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.radish.crawler.database.model.Scheduler;

@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {
}
