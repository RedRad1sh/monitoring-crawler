package ru.radish.crawler.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import ru.radish.crawler.kafka.consumer.KafkaRestConsumer;
import ru.radish.crawler.kafka.consumer.SchedulerInitializer;
import ru.radish.crawler.parsers.DefaultParser;
import ru.radish.crawler.scheduler.ScheduleMaker;

import java.util.Map;

@Configuration
@EntityScan("ru.radish.crawler.database.model")
@EnableJpaRepositories("ru.radish.crawler.database.repository")
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
        KafkaRestConsumer kafkaRestConsumer = applicationContext.getBean(KafkaRestConsumer.class);
        SchedulerInitializer schedulerInitializer = applicationContext.getBean(SchedulerInitializer.class);
        ScheduleMaker scheduleMaker = applicationContext.getBean(ScheduleMaker.class);
        Map<String, TaskScheduler> schedulers = schedulerInitializer.initializeTaskSchedulers(scheduleMaker);
        DefaultParser.phantomjsPath = applicationContext.getEnvironment().getProperty("phantomjs.path");
        DefaultParser.getWebDriver();
        kafkaRestConsumer.setTaskSchedulers(schedulers);
        kafkaRestConsumer.setScheduleMaker(scheduleMaker);
        kafkaRestConsumer.readMessages();
    }

}

@Configuration
@ComponentScan("ru.radish.crawler")
class ConfigurationClass {
}

