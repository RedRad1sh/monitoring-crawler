package ru.radish.crawler.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.radish.crawler.database.model.ParsePlan;

@Repository
public interface ParsePlanRepository extends JpaRepository<ParsePlan, Long> {
}
