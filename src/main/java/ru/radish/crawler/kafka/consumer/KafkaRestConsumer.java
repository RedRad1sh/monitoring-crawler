package ru.radish.crawler.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.radish.crawler.database.model.ParsePlan;
import ru.radish.crawler.database.model.Parser;
import ru.radish.crawler.database.model.Scheduler;
import ru.radish.crawler.database.service.ParsePlanService;
import ru.radish.crawler.database.service.ParserService;
import ru.radish.crawler.database.service.SchedulerService;
import ru.radish.crawler.parsers.ShopParser;
import ru.radish.crawler.scheduler.CronMaker;
import ru.radish.crawler.scheduler.ScheduleMaker;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static ru.radish.crawler.kafka.consumer.SchedulerInitializer.getParserWorker;

@Service
@EnableScheduling
@Slf4j
public class KafkaRestConsumer {
    @Value("${kafka.server}")
    private String server = "";

    @Value("${kafka.topic}")
    private String topicName = "";

    @Value("${kafka.username}")
    private String username = "";

    @Value("${kafka.password}")
    private String password = "";

    private String group = "messageListener";

    private Map<String, TaskScheduler> taskSchedulers = new HashMap<>();

    private ScheduleMaker scheduleMaker;

    @Autowired
    ParsePlanService parsePlanService;

    @Autowired
    SchedulerService schedulerService;

    @Autowired
    ParserService parserService;

    public void setTaskSchedulers(Map<String, TaskScheduler> taskSchedulers) {
        this.taskSchedulers = taskSchedulers;
    }

    public void setScheduleMaker(ScheduleMaker scheduleMaker) {
        this.scheduleMaker = scheduleMaker;
    }

    public void setAuthorizationConfig(String server, String username, String password) {
        this.server = server;
        this.username = username;
        this.password = password;
    }

    private Consumer<String, String> getConsumer() {
        final Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                server);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_uncommitted");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put("request.timeout.ms", "30000");
        props.put("ssl.endpoint.identification.algorithm", "https");
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "PLAIN");
        props.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"" + username + "\" password=\"" + password + "\";");

        return new KafkaConsumer<>(props);
    }

    @Scheduled(initialDelay = 50000, fixedDelay = 50000)
    public void readMessages() throws JsonProcessingException, JSONException {
        Consumer<String, String> consumer = getConsumer();
        consumer.subscribe(Arrays.asList(topicName));
        ConsumerRecords<String, String> consumerRecords = consumer.poll(30000);
        if (!consumerRecords.isEmpty()) {
            Iterator<ConsumerRecord<String, String>> iterator = consumerRecords.iterator();
            while (iterator.hasNext()) {
                ConsumerRecord iteratorRecord = iterator.next();
                extractInfoFromJson(iteratorRecord);
            }
        }
        consumer.commitAsync();
        consumer.close();
    }

    private void extractInfoFromJson(ConsumerRecord<String, String> iteratorRecord) throws JsonProcessingException, JSONException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object jsonObjectFormatted = objectMapper.readValue(iteratorRecord.value().toString(), Object.class);
        JSONObject jsonObject = new JSONObject(iteratorRecord.value().toString());
        System.out.println("key: " + iteratorRecord.key());
        System.out.println("value: \n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObjectFormatted));
        try {
            URL url = new URL(jsonObject.get("url").toString());
            String shopName = jsonObject.getJSONObject("shop").get("name").toString();
            Long createDate = Long.parseLong(jsonObject.get("createDate").toString());
            String vendorNumber = iteratorRecord.key();
            createParsePlan(url, shopName, createDate, vendorNumber);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void createParsePlan(URL url, String shopName, Long createDate, String vendorNumber) throws InterruptedException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Parser> parsers = parserService.getAll();
        for (Parser parser : parsers) {
            try {
                ShopParser parserWorker = getParserWorker(parser);
                URL shopUrl = url;
                if (!parser.getShopName().equals(shopName)) {
                    shopUrl = new URL(parserWorker.searchGoodByVendorNumber(vendorNumber));
                }

                Date date = new Date(createDate);
                String cron = CronMaker.everyMinute(date);
                Scheduler scheduler = new Scheduler();
                scheduler.setTrigger(cron);

                ParsePlan parsePlan = createParsePlan(shopUrl, parser, scheduler);

                TaskScheduler taskScheduler = scheduleMaker.addNewSchedule(parsePlan, cron, parserWorker);
                taskSchedulers.put(parsePlan.getId().toString(), taskScheduler);
            } catch (MalformedURLException e) {
                log.error("Wrong shop url");
            }
        }
    }

    private ParsePlan createParsePlan(URL url, Parser parser, Scheduler scheduler) {
        ParsePlan parsePlan = new ParsePlan();
        parsePlan.setUrl(url.toString());
        parsePlan.setActive(true);
        parsePlan.setParser(parser);
        parsePlan.setScheduler(scheduler);
        schedulerService.save(scheduler);
        parsePlanService.save(parsePlan);
        return parsePlan;
    }

}
