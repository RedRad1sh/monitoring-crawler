package ru.radish.crawler.database.service;

import org.springframework.stereotype.Service;
import ru.radish.crawler.database.model.ParsePlan;
import ru.radish.crawler.database.model.ParseResult;

import java.util.List;

@Service
public interface ParseResultService {

    ParseResult findOne(Long id);

    void save(ParseResult parseResult);

    void delete(Long id);

    List<ParseResult> getAll();

    ParseResult findFirstByParsePlanOrderByIdDesc(ParsePlan parsePlan);
}
