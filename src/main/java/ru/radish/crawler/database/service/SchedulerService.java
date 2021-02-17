package ru.radish.crawler.database.service;

import org.springframework.stereotype.Service;
import ru.radish.crawler.database.model.Scheduler;

import java.util.List;

@Service
public interface SchedulerService {

    Scheduler findOne(Long id);

    void save(Scheduler scheduler);

    void delete(Long id);

    List<Scheduler> getAll();

}
