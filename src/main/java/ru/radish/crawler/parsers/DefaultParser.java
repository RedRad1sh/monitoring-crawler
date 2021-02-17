package ru.radish.crawler.parsers;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultParser {

    public static String phantomjsPath = "";

    public static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";

    private static final String[] referrers = {"http://www.bing.com", "http://www.google.com", "http://www.yandex.ru", "http://www.mail.ru", "http://www.vk.com"};

    private static WebDriver webDriver = null;

    public static Document getPage(String url) throws IOException {
        Document page;
        int counter = 0;
        do {
            try {
                Thread.sleep(2500 + new Random().nextInt(500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Connection connection = Jsoup.connect(url)
                    .ignoreHttpErrors(true)
                    .referrer(referrers[new Random().nextInt(referrers.length)])
                    .userAgent(userAgent);
            page = connection.get();
            counter++;
        } while (page.select("meta").size() < 10 && counter < 10);
        if (counter >= 10) {
            WebDriver webDriver = getWebDriver();
            webDriver.get(url);
            page = Jsoup.parse(webDriver.getPageSource());
            webDriver.quit();
        }
        return page;
    }

    public static WebDriver getWebDriver() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("general.useragent.override", DefaultParser.userAgent);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjsPath);
        String[] phantomArgs = new String[]{
                "--webdriver-loglevel=NONE"
        };
        Logger.getLogger(PhantomJSDriverService.class.getName()).setLevel(Level.OFF);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs);
        webDriver = new PhantomJSDriver(caps);
        return webDriver;
    }

}
