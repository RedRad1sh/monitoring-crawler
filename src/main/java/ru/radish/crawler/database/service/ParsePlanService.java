package ru.radish.crawler.database.service;

import org.springframework.stereotype.Service;
import ru.radish.crawler.database.model.ParsePlan;

import java.util.List;

@Service
public interface ParsePlanService {

    ParsePlan findOne(Long id);

    void save(ParsePlan parsePlan);

    void delete(Long id);

    List<ParsePlan> getAll();

}
