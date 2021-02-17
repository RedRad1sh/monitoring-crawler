package ru.radish.crawler.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import ru.radish.crawler.database.model.ParsePlan;
import ru.radish.crawler.database.model.ParseResult;
import ru.radish.crawler.database.service.ParseResultService;
import ru.radish.crawler.parsers.ShopParser;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Service
public class ScheduleMaker {

    @Autowired
    private ParseResultService parseResultService;

    public TaskScheduler addNewSchedule(ParsePlan parsePlan, String cron, ShopParser parserWorker) throws InterruptedException {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.initialize();
        threadPoolTaskScheduler.schedule(parse(parsePlan, parserWorker), new CronTrigger(cron));
        System.out.println("Task added for " + parsePlan.getUrl());
        ParseResult foundedParseResult = parseResultService.findFirstByParsePlanOrderByIdDesc(parsePlan);
        System.out.println(foundedParseResult);
        return threadPoolTaskScheduler;
    }

    private Runnable parse(ParsePlan parsePlan, ShopParser parserWorker) {
        return () -> {
            try {
                URL parseUrl = new URL(parsePlan.getUrl());
                Map<String, String> result = parserWorker.makeMapGoodDesc(parseUrl);
                ParseResult foundedParseResult = parseResultService.findFirstByParsePlanOrderByIdDesc(parsePlan);
                boolean inStock = Boolean.valueOf(result.get("inStock"));
                BigDecimal lastPrice = BigDecimal.valueOf(0L);
                BigDecimal actualPrice = BigDecimal.valueOf(Long.parseLong(result.get("price"))).setScale(2);
                if (foundedParseResult != null) {
                    lastPrice = foundedParseResult.getPrice().setScale(2);
                }

                if (foundedParseResult == null || !lastPrice.equals(actualPrice)) {
                    System.out.println(new Date(System.currentTimeMillis()) + ":");
                    System.out.println("result: " + result.toString());
                    createParseResult(parsePlan, actualPrice, inStock);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        };
    }

    private void createParseResult(ParsePlan parsePlan, BigDecimal actualPrice, boolean inStock) {
        ParseResult parseResult = new ParseResult();
        parseResult.setParsePlan(parsePlan);
        parseResult.setPrice(actualPrice);
        parseResult.setParseTime(new Timestamp(System.currentTimeMillis()));
        parseResult.setInStock(inStock);
        System.out.println(parseResult);
        parseResultService.save(parseResult);
    }
}
