package com.title.aggregator.domain.acting;

import com.title.aggregator.domain.model.Title;
import com.title.aggregator.domain.model.Titles;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.title.aggregator.utils.Constants.Anivost.*;
import static com.title.aggregator.utils.HtmlUtils.*;

@Component
public class Anivost extends VoiceActing {

    @Override
    public Titles createTitles() {
        Document document = getDocument(VOICE_ACTING_URL);
        List<Elements> allElements = getElements(document);
        Titles aniTitles = new Titles();
        aniTitles.setVoiceActing(getVoiceActing());
        allElements.forEach(elements -> elements.forEach(element -> aniTitles.addTitle(createAniTitle(element))));
        return aniTitles;
    }

    @Override
    public String getVoiceActing() {
        return ANIVOST;
    }

    private List<Elements> getElements(Document document) {
        List<Elements> elements = new ArrayList<>();
        elements.add(document.getElementsByClass(ELEMENT_CLASS));
        Elements navigation = document.getElementsByClass("block_4").get(0).getElementsByTag("a");
        navigation.forEach(element -> elements.add(findElements(getHrefValue(element), ELEMENT_CLASS)));
        return elements;
    }

    private Title createAniTitle(Element element) {
        return new Title(getTitle(element), getSeriesNumber(element), BASE_URL + getPicture(element, "src"), getUrl(element));
    }

    private String getTitle(Element element) {
        return getFirstAnchor(element).html();
    }

    private String getSeriesNumber(Element element) {
        Element seriesElement = getFirstAnchor(element);
        String html = seriesElement.html();
        String substring = html.split("\\[")[1];
        int beginIndex = substring.indexOf("-");
        int endIndex = substring.lastIndexOf("из");
        return substring.substring(beginIndex + 1, endIndex - 1);
    }
}
