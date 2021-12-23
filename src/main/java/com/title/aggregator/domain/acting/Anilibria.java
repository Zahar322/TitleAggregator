package com.title.aggregator.domain.acting;

import com.title.aggregator.domain.model.Title;
import com.title.aggregator.domain.model.Titles;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import static com.title.aggregator.utils.Constants.Anilibria.*;
import static com.title.aggregator.utils.HtmlUtils.*;

@Component
public class Anilibria extends VoiceActing {

    @Override
    public Titles createTitles() {
        Elements elements = findElements(VOICE_ACTING_URL, ELEMENT_CLASS);
        Titles aniTitles = new Titles();
        aniTitles.setVoiceActing(getVoiceActing());
        elements.forEach(element -> aniTitles.addTitle(createAniTitle(element)));
        return aniTitles;
    }

    @Override
    public String getVoiceActing() {
        return ANILIBRIA;
    }

    private Title createAniTitle(Element element) {
        return new Title(getTitle(element), getSeriesNumber(element, SERIES_NUMBER_CLASS_NAME), getPicture(element, "src"), BASE_URL + getUrl(element));
    }

    private String getTitle(Element element) {
        return element.getElementsByClass(TITLE_CLASS_NAME).get(0).html();
    }
}
