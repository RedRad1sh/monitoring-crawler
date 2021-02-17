package ru.radish.crawler.parsers.sites;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import ru.radish.crawler.parsers.DefaultParser;
import ru.radish.crawler.parsers.ShopParser;

import java.io.IOException;

public class DNSParser implements ShopParser {

    /**
     * Поля поиска кода производителя
     */
    private static final String[] optionIds = {"Код производителя", "Модель"};

    public String getVendorNumber(String url) {
        String vendorNumber = "";
        WebDriver driver = DefaultParser.getWebDriver();
        try {
            vendorNumber = getVendorNumberFromSquareBrackets(DefaultParser.getPage(url));
            if (vendorNumber != null)
                return vendorNumber;
            driver.get(url);
            driver.findElement(By.linkText("Характеристики")).click();
            Document document = Jsoup.parse(driver.getPageSource());
            driver.quit();

            Element characteristic = null;
            for (String option :
                    optionIds) {
                characteristic = document.select(String.format("tr:contains(%s)", option)).first();
                if (characteristic != null)
                    break;
            }
            vendorNumber = characteristic.select("div").get(1).text().split("<!-- -->")[0];
            return vendorNumber;
        } finally {
            driver.quit();
            return vendorNumber;
        }
    }

    /**
     * Получает код производителя из названия товара, если он в нем есть
     *
     * @param goodPage страница товара
     * @return возвращает код производителя
     */
    private String getVendorNumberFromSquareBrackets(Document goodPage) {
        String goodName = goodPage.select(".page-title.product-card-top__title").text();
        int firstBracketIndex = goodName.lastIndexOf("[");
        int lastBracketIndex = goodName.lastIndexOf("]");
        if (firstBracketIndex != -1 && lastBracketIndex != -1)
            return goodName.substring(firstBracketIndex + 1, lastBracketIndex);
        else
            return null;
    }

    public String searchGoodByVendorNumber(String vendorNumber) {
        String url = "";
        try {
            Document good;
            do {
                String searchUrl = "https://www.dns-shop.ru/search/?q=" + vendorNumber;
                good = DefaultParser.getPage(searchUrl);
            } while (good.select("meta[name=application-name]").size() == 0);
            url = getVendorNumberFromGoodPage(good);
            if (url == null && good.select(".product-info-slider__char") != null) {
                String linkToGood = "https://www.dns-shop.ru" + good.select(".product-info__title-link").select("a").attr("href");
                return getVendorNumberFromGoodPage(DefaultParser.getPage(linkToGood));
            }
        } finally {
            return url;
        }
    }

    /**
     * Получает номер товара из страницы товара
     *
     * @param good страница товара
     * @return возвращает номер товара
     */
    private String getVendorNumberFromGoodPage(Document good) {
        return "https://www.dns-shop.ru" + good.select(".product-card-tabs").first().attr("data-product-url");
    }

    public String getGoodName(String url) {
        try {
            Document good = DefaultParser.getPage(url);
            String goodName = good.select("h1.page-title").text();
            return goodName;
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String getGoodPrice(String url) {
        try {
            Document good = DefaultParser.getPage(url);
            String rightString = ",\"bonus\"";
            String leftString = "\"price\":";

            int lastIndex = good.toString().lastIndexOf(rightString);
            int firstIndex = good.toString().substring(0, lastIndex).lastIndexOf(leftString) + leftString.length();

            String htmlGood = good.toString();
            return htmlGood.substring(firstIndex, lastIndex);
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String getStockState(String url) {
        try {
            Document good = DefaultParser.getPage(url);
            String inStock = good.select(".order-avail-wrap").text();
            if (inStock.indexOf("Товара нет в наличии") != -1)
                return "false";
            else
                return "true";
        } catch (IOException e) {
            return e.getMessage();
        }
    }

}
