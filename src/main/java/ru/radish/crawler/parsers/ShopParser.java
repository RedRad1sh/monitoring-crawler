package ru.radish.crawler.parsers;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public interface ShopParser {

    /**
     * Находит ссылку на товар в магазине по коду производителя
     * @param vendorNumber код производителя
     * @return возвращает ссылку на товар
     */
    String searchGoodByVendorNumber(String vendorNumber);

    /**
     * Получает код производителя товара по ссылке
     * @param url ссылка на товар в магазине
     * @return возаращает код производителя
     */
    String getVendorNumber(String url);

    /**
     * Получает имя товара по ссылке
     * @param url ссылка на товар в магазине
     * @return возаращает имя
     */
    String getGoodName(String url);

    /**
     * Получает цену товара по ссылке
     * @param url ссылка на товар в магазине
     * @return возаращает цену
     */
    String getGoodPrice(String url);

    /**
     * Получает состояние товара (в наличии или нет)
     * @param url ссылка на товар в магазине
     * @return возаращает состояние товара
     */
    String getStockState(String url);

    /**
     * Собирает все данные о товаре в магазине по коду производителя
     * @param vendorNumber код производителя
     * @return возвращает коллекцию ключ=значение с характеристиками товара
     */
    default Map<String, String> makeMapGoodDesc(String vendorNumber){
        Map<String, String> description = new HashMap<>();
        String url = searchGoodByVendorNumber(vendorNumber);
        description.put("name", getGoodName(url));
        description.put("url", url);
        description.put("vendorNumber", vendorNumber);
        description.put("price", getGoodPrice(url));
        description.put("inStock", getStockState(url));
        return description;
    }

    /**
     * Собирает все данные о товаре в магазине по ссылке на товар в магазине
     * @param url ссылка на товар в магазине
     * @return возвращает коллекцию ключ=значение с характеристиками товара
     */
    default Map<String, String> makeMapGoodDesc(URL url){
        Map<String, String> description = new HashMap<>();
        description.put("name", getGoodName(url.toString()));
        description.put("url", url.toString());
        description.put("vendorNumber", getVendorNumber(url.toString()));
        description.put("price", getGoodPrice(url.toString()));
        description.put("inStock", getStockState(url.toString()));
        return description;
    }

}
