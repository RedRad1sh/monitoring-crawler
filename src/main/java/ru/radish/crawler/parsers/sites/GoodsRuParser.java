package ru.radish.crawler.parsers.sites;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.radish.crawler.parsers.DefaultParser;
import ru.radish.crawler.parsers.ShopParser;

import java.io.IOException;

public class GoodsRuParser implements ShopParser {

    /**
     * Поля поиска кода производителя
     */
    private static final String[] optionIds = {"Артикул производителя", "Модель"};

    public String getVendorNumber(String url) {
        Document document = null;
        try {
            document = DefaultParser.getPage(url + "/spec/");
            Element characteristic = null;
            for (String option :
                    optionIds) {
                characteristic = document.select(String.format("tr:contains(%s)", option)).first();
                if (characteristic != null)
                    break;
            }
            String vendorNumber = characteristic.select("td").get(1).text().split("показать больше")[0];
            return vendorNumber;
        } catch (IOException e) {
            return e.toString();
        }
    }

    public String getGoodName(String url) {
        Document good = null;
        try {
            good = DefaultParser.getPage(url);
            return good.select("header.title-page").select("h1").text();
        } catch (IOException e) {
            return e.toString();
        }
    }

    public String getGoodPrice(String url) {
        try {
            Document good = DefaultParser.getPage(url);
            String firstPrice = good.select("div.price__final").text();
            String secondPrice = good.select("span.last-price").text();
            if (!firstPrice.equals(""))
                return firstPrice.split("₽")[0].replace(" ", "");
            else if (secondPrice != null)
                return secondPrice.split("руб")[0].replace(" ", "");
            else
                return "";
        } catch (IOException e) {
            return e.toString();
        }
    }

    public String getStockState(String url) {
        try {
            Document good = DefaultParser.getPage(url);
            String inStock = good.select(".prod--disabled-caption").text();
            if (inStock.indexOf("Нет в наличии") != -1)
                return "false";
            else
                return "true";
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public String searchGoodByVendorNumber(String searchQuery) {
        String returnUrl = "";
        try {
            String searchUrl = "https://goods.ru/catalog/?q=" + searchQuery;
            Document foundedGoods;
            foundedGoods = DefaultParser.getPage(searchUrl);
            Elements foundedGood = foundedGoods.select("a.ddl_product_link");
            if (foundedGood.size() == 1) {
                String goodUrl = foundedGood.get(0).attr("href");
                returnUrl = "https://goods.ru" + goodUrl;
            } else {
                for (Element good : foundedGood) {
                    String name = good.select("img").attr("alt");
                    int queryIndex = name.indexOf(searchQuery);
                    if (queryIndex != -1) {
                        returnUrl = "https://goods.ru" + good.attr("href");
                        break;
                    }
                }
            }
        } finally {
            return returnUrl;
        }
    }

}
