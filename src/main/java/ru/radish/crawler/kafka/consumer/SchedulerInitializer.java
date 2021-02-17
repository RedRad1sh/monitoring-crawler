package ru.radish.crawler.kafka.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import ru.radish.crawler.database.model.ParsePlan;
import ru.radish.crawler.database.model.Parser;
import ru.radish.crawler.database.service.ParsePlanService;
import ru.radish.crawler.parsers.ShopParser;
import ru.radish.crawler.scheduler.ScheduleMaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchedulerInitializer {

    @Autowired
    ParsePlanService parsePlanService;

    public Map<String, TaskScheduler> initializeTaskSchedulers(ScheduleMaker scheduleMaker) throws InterruptedException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<ParsePlan> parsePlans = parsePlanService.getAll();
        Map<String, TaskScheduler> taskSchedulers = new HashMap<>();
        for (ParsePlan parsePlan : parsePlans) {
            if (parsePlan.isActive()) {
                String cron = parsePlan.getScheduler().getTrigger();
                ShopParser parserWorker = getParserWorker(parsePlan.getParser());
                TaskScheduler taskScheduler = scheduleMaker.addNewSchedule(parsePlan, cron, parserWorker);
                taskSchedulers.put(parsePlan.getId().toString(), taskScheduler);
            }
        }
        return taskSchedulers;
    }

    public static ShopParser getParserWorker(Parser parser) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ShopParser parserWorker;
        Class mClassObject = Class.forName("ru.radish.crawler.parsers.sites." + parser.getClassName());
        Constructor constructor = mClassObject.getConstructor(new Class[]{});
        parserWorker = (ShopParser) constructor.newInstance();
        return parserWorker;
    }
}
