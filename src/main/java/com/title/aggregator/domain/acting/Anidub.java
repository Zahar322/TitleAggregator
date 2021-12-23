package com.title.aggregator.domain.acting;

import com.title.aggregator.domain.model.Title;
import com.title.aggregator.domain.model.Titles;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.title.aggregator.utils.Constants.Anidub.*;
import static com.title.aggregator.utils.HtmlUtils.*;

@Component
public class Anidub extends VoiceActing {

    @Override
    public Titles createTitles() {
        Document document = getDocument(VOICE_ACTING_URL);
        List<Elements> allElements = getElements(document);
        Titles titles = new Titles();
        titles.setVoiceActing(getVoiceActing());
        allElements.forEach(elements -> elements.forEach(element -> titles.addTitle(createAniTitle(element))));
        return titles;
    }

    @Override
    public String getVoiceActing() {
        return ANIDUB;
    }

    private List<Elements> getElements(Document document) {
        List<Elements> elements = new ArrayList<>();
        elements.add(document.getElementsByClass(ELEMENT_CLASS));
        Elements navigation = document.getElementsByClass("navigation").get(0).getElementsByTag("a");
        navigation.forEach(element -> elements.add(findElements(getHrefValue(element), ELEMENT_CLASS)));
        return elements;
    }

    private Title createAniTitle(Element element) {
        return new Title(getTitle(element), getSeriesNumber(element), BASE_URL + getPicture(element, "data-src"), getUrl(element));
    }

    private String getTitle(Element element) {
        return element.getElementsByTag("img").attr("alt").replaceFirst("Постер аниме ", "");
    }

    private String getSeriesNumber(Element element) {
        Element seriesElement = element.getElementsByClass(SERIES_NUMBER_CLASS_NAME).get(0);
        return seriesElement.getElementsByTag("span").html();
    }
}
