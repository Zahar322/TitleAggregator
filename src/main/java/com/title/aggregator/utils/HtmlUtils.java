package com.title.aggregator.utils;

import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static com.title.aggregator.utils.Constants.Tags.*;

@UtilityClass
public class HtmlUtils {

    public static Elements findElements(String url, String element) {
        Elements elements = null;
        try {
            Document document = Jsoup.connect(url).get();
            elements = document.getElementsByClass(element);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return elements;
    }

    public static Document getDocument(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    public static String getPicture(Element element, String attr) {
        return element.getElementsByTag(IMAGE).attr(attr);
    }

    public static String getUrl(Element element) {
        return getFirstAnchor(element).attr(HREF);
    }

    public static Element getFirstAnchor(Element element) {
        return element.getElementsByTag(ANCHOR).get(0);
    }

    public static String getHrefValue(Element element) {
        return element.attr(HREF);
    }

    public static String getSeriesNumber(Element element, String seriesNumberClassName) {
        Element seriesElement = element.getElementsByClass(seriesNumberClassName).get(0);
        String html = seriesElement.html();
        int startIndex = html.indexOf("-");
        return html.substring(startIndex + 1);
    }

    public static String getSeriesNumber(Element seriesElement) {
        String html = seriesElement.html();
        int startIndex = html.indexOf("-");
        return html.substring(startIndex + 1);
    }


}
