package ru.radish.crawler.database.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.radish.crawler.database.model.Scheduler;
import ru.radish.crawler.database.repository.SchedulerRepository;

import java.util.List;

@Slf4j
@Service
public class SchedulerServiceImplementation implements SchedulerService {

    @Autowired
    SchedulerRepository schedulerRepository;

    @Override
    public Scheduler findOne(Long id) {
        log.info("IN ParsePlanServiceImplementation METHOD getById {}", id);
        return schedulerRepository.findById(id).get();
    }

    @Override
    public void save(Scheduler good) {
        log.info("IN ParsePlanServiceImplementation METHOD save {}", good);
        schedulerRepository.save(good);
    }

    @Override
    public void delete(Long id) {
        log.info("IN ParsePlanServiceImplementation METHOD delete {}", id);
        schedulerRepository.deleteById(id);
    }

    @Override
    public List<Scheduler> getAll() {
        log.info("IN ParsePlanServiceImplementation METHOD getAll");
        return schedulerRepository.findAll();
    }

}
