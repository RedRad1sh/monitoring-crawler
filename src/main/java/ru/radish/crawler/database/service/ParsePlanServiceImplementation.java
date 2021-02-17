package ru.radish.crawler.database.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.radish.crawler.database.model.ParsePlan;
import ru.radish.crawler.database.repository.ParsePlanRepository;

import java.util.List;

@Slf4j
@Service
public class ParsePlanServiceImplementation implements ParsePlanService {

    @Autowired
    ParsePlanRepository parsePlanRepository;

    @Override
    public ParsePlan findOne(Long id) {
        log.info("IN ParsePlanServiceImplementation METHOD getById {}", id);
        return parsePlanRepository.findById(id).get();
    }

    @Override
    public void save(ParsePlan good) {
        log.info("IN ParsePlanServiceImplementation METHOD save {}", good);
        parsePlanRepository.save(good);
    }

    @Override
    public void delete(Long id) {
        log.info("IN ParsePlanServiceImplementation METHOD delete {}", id);
        parsePlanRepository.deleteById(id);
    }

    @Override
    public List<ParsePlan> getAll() {
        log.info("IN ParsePlanServiceImplementation METHOD getAll");
        return parsePlanRepository.findAll();
    }

}
