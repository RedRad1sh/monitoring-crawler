package ru.radish.crawler.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.radish.crawler.database.model.ParsePlan;
import ru.radish.crawler.database.model.ParseResult;

@Repository
public interface ParseResultRepository extends JpaRepository<ParseResult, Long> {

    ParseResult findFirstByParsePlanOrderByIdDesc(ParsePlan parsePlan);

}
