package com.title.aggregator.domain.service;

import com.title.aggregator.domain.model.Title;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.title.aggregator.utils.Constants.Animaunt.PATTERN;
import static com.title.aggregator.utils.HtmlUtils.getDocument;

@Slf4j
@Service
public class TitleService {

    public String findAnimauntSeriesNumber(String url) {
        Document document = getDocument(url);
        if (document != null) {
            Elements elements = document.getElementsByClass("vis vis-clear");
            Pattern pattern = Pattern.compile(PATTERN);
            Matcher matcher = pattern.matcher(findSeriesNumber(elements));
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return null;
    }

    private String findSeriesNumber(Elements elements) {
        return elements.stream().filter(element -> element.html().contains("Эпизоды"))
                                .findAny()
                                .map(Element::html)
                                .orElse("");
    }
}
