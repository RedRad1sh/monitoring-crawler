package ru.radish.crawler.database.service;

import org.springframework.stereotype.Service;
import ru.radish.crawler.database.model.Parser;

import java.util.List;

@Service
public interface ParserService {

    Parser findOne(Long id);

    void save(Parser parser);

    void delete(Long id);

    List<Parser> getAll();

    Parser findByClassName(String className);

    Parser findByShopName(String shopName);

}
