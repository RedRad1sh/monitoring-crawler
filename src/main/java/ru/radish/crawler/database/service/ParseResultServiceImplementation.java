package ru.radish.crawler.database.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.radish.crawler.database.model.ParsePlan;
import ru.radish.crawler.database.model.ParseResult;
import ru.radish.crawler.database.repository.ParseResultRepository;

import java.util.List;

@Slf4j
@Service
public class ParseResultServiceImplementation implements ParseResultService {

    @Autowired
    ParseResultRepository parseResultRepository;

    @Override
    public ParseResult findOne(Long id) {
        log.info("IN ParseResultServiceImplementation METHOD getById {}", id);
        return parseResultRepository.findById(id).get();
    }

    @Override
    public void save(ParseResult good) {
        log.info("IN ParseResultServiceImplementation METHOD save {}", good);
        parseResultRepository.save(good);
    }

    @Override
    public void delete(Long id) {
        log.info("IN ParseResultServiceImplementation METHOD delete {}", id);
        parseResultRepository.deleteById(id);
    }

    @Override
    public List<ParseResult> getAll() {
        log.info("IN ParseResultServiceImplementation METHOD getAll");
        return parseResultRepository.findAll();
    }

    @Override
    public ParseResult findFirstByParsePlanOrderByIdDesc(ParsePlan parsePlan) {
        log.info("IN ParsePlanServiceImplementation METHOD findFirstByParsePlan");
        return parseResultRepository.findFirstByParsePlanOrderByIdDesc(parsePlan);
    }


}
