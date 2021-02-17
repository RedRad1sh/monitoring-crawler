package ru.radish.crawler.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.radish.crawler.database.model.Parser;

import java.util.Optional;

@Repository
public interface ParserRepository extends JpaRepository<Parser, Long> {

    Parser findByClassName(String className);

    Parser findByShopName(String shopName);

}
