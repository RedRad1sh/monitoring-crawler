package ru.radish.crawler.database.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.radish.crawler.database.model.ParseResult;
import ru.radish.crawler.database.model.Parser;
import ru.radish.crawler.database.repository.ParserRepository;

import java.util.List;

@Slf4j
@Service
public class ParseServiceImplementation implements ParserService {

    @Autowired
    ParserRepository parserRepository;

    @Override
    public Parser findOne(Long id) {
        log.info("IN ParsePlanServiceImplementation METHOD getById {}", id);
        return parserRepository.findById(id).get();
    }

    @Override
    public void save(Parser good) {
        log.info("IN ParsePlanServiceImplementation METHOD save {}", good);
        parserRepository.save(good);
    }

    @Override
    public void delete(Long id) {
        log.info("IN ParsePlanServiceImplementation METHOD delete {}", id);
        parserRepository.deleteById(id);
    }

    @Override
    public List<Parser> getAll() {
        log.info("IN ParsePlanServiceImplementation METHOD getAll");
        return parserRepository.findAll();
    }

    @Override
    public Parser findByClassName(String className) {
        log.info("IN ParseServiceImplementation METHOD findByClassName");
        return parserRepository.findByClassName(className);
    }

    @Override
    public Parser findByShopName(String shopName) {
        log.info("IN ParseServiceImplementation METHOD findByShopName");
        return parserRepository.findByShopName(shopName);
    }

}
