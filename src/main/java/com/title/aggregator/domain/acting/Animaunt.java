package com.title.aggregator.domain.acting;

import com.title.aggregator.domain.model.Title;
import com.title.aggregator.domain.model.Titles;
import com.title.aggregator.domain.service.TitleService;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.title.aggregator.utils.Constants.Animaunt.*;
import static com.title.aggregator.utils.HtmlUtils.*;
import static java.util.Arrays.stream;
import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class Animaunt extends VoiceActing {

    private final TitleService titleService;
    private final Executor executor;

    @Override
    public Titles createTitles() {
        Document document = getDocument(VOICE_ACTING_URL);
        List<Elements> elements = getElements(document);
        Titles titles = new Titles();
        titles.setVoiceActing(getVoiceActing());
        titles.setTitles(getTitles(elements));
        return titles;
    }

    @Override
    public String getVoiceActing() {
        return ANIMAUNT;
    }

    private List<Elements> getElements(Document document) {
        List<Elements> elements = new ArrayList<>();
        elements.add(document.getElementsByClass(ELEMENT_CLASS));
        Elements navigation = document.getElementsByClass("navigation").get(0).getElementsByTag("a");
        navigation.forEach(element -> elements.add(findElements(getHrefValue(element), ELEMENT_CLASS)));
        return elements;
    }

    private List<Title> getTitles(List<Elements> elements) {
        CompletableFuture<Title>[] futures = elements.stream()
                                                     .flatMap(Collection::parallelStream)
                                                     .map(this::getTitleFuture)
                                                     .toArray(CompletableFuture[]::new);

        return allOf(futures).thenApply(v -> stream(futures).map(CompletableFuture::join)
                                                            .collect(toList())).join();
    }

    public CompletableFuture<Title> getTitleFuture(Element element) {
        return CompletableFuture.supplyAsync(() -> createAniTitle(element), executor);
    }

    private Title createAniTitle(Element element) {
        Title title = new Title(getTitle(element), null, BASE_URL + getPicture(element, "src"), getUrl(element));
        title.setSeries(titleService.findAnimauntSeriesNumber(title.getUrl()));
        return title;
    }

    private String getTitle(Element element) {
        return element.getElementsByClass(TITLE_CLASS_NAME).get(0).html();
    }

}
