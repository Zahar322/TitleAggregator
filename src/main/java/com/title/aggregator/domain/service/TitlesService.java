package com.title.aggregator.domain.service;

import com.title.aggregator.domain.acting.VoiceActing;
import com.title.aggregator.domain.model.Title;
import com.title.aggregator.domain.model.Titles;
import com.title.aggregator.jpa.models.Subscription;
import com.title.aggregator.jpa.repository.TitlesRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import static com.title.aggregator.utils.Constants.Animaunt.ANIMAUNT;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class TitlesService {

    private final TitlesRepository repository;
    private final List<VoiceActing> actings;
    private final Executor executor;

    public List<Titles> findTitles() {
        com.title.aggregator.jpa.models.Titles titles = findFirstTitles();
        return titles != null ? titles.getTitles() : initTitles();
    }

    public List<Titles> initTitles() {
        List<Titles> titles = getTitles();
        save(titles, null);
        return titles;
    }

    public List<Titles> getTitles() {
        return actings.stream()
                      .map(VoiceActing::getTitles)
                      .collect(toList());
    }

    public List<Titles> getTitlesWithoutAnimaunt() {
        return actings.stream()
                      .filter(acting -> !acting.getVoiceActing().equals(ANIMAUNT))
                      .map(VoiceActing::getTitles)
                      .collect(toList());
    }

    public Titles getAnimauntTitles() {
        return actings.stream()
                      .filter(acting -> acting.getVoiceActing().equals(ANIMAUNT))
                      .findFirst()
                      .map(VoiceActing::getTitles)
                      .orElse(null);
    }

    public void save(List<Titles> titles, Long id) {
        com.title.aggregator.jpa.models.Titles jpaTitles = new com.title.aggregator.jpa.models.Titles();
        jpaTitles.setId(id);
        jpaTitles.setTitles(titles);
        repository.save(jpaTitles);
    }

    public com.title.aggregator.jpa.models.Titles findFirstTitles() {
        return repository.findFirstByIdNotNull();
    }

    public Titles findTitlesByVoiceActing(String voiceActing) {
        return findFirstTitles().findTitlesByVoiceActing(voiceActing);
    }

    public Title findTitleByVoiceActingAndId(String voiceActing, String titleId) {
        return findTitlesByVoiceActing(voiceActing).findTitleBydId(titleId);
    }

    public Title findTitleById(String titleId) {
        return findFirstTitles().getTitles()
                                .stream()
                                .map(titles -> titles.findTitleBydId(titleId))
                                .filter(Objects::nonNull)
                                .findFirst()
                                .orElse(null);
    }

    public Pair<String, Title> findTitleWithVoiceActionById(String titleId) {
        return findFirstTitles().getTitles()
                                .stream()
                                .map(titles -> titles.findTitleWithVoiceActingById(titleId))
                                .filter(Objects::nonNull)
                                .findFirst()
                                .orElse(null);
    }

    public Titles findTitlesFromSubscriptions(List<Subscription> subscriptions, com.title.aggregator.jpa.models.Titles titles) {
        Titles domainTitles = new Titles();
        List<Title> title = subscriptions.stream().map(subscription -> findTitleFromSubscription(subscription, titles)).collect(toList());
        domainTitles.setTitles(title);
        return domainTitles;
    }

    public Title findTitleFromSubscription(Subscription subscription, com.title.aggregator.jpa.models.Titles titles) {
        return titles.findTitlesByVoiceActing(subscription.getVoiceActing())
                     .getTitles()
                     .stream()
                     .filter(title -> title.getName().equals(subscription.getTitleName()))
                     .findFirst()
                     .orElse(null);
    }

    public List<Titles> divideTitlesOnHalf(Titles titles) {
        List<Title> sourceTitles = titles.getTitles();
        return ListUtils.partition(sourceTitles, sourceTitles.size() / 2  + 1).stream()
                .map(domainTitles -> new Titles(domainTitles, titles.getVoiceActing()))
                .collect(toList());
    }

//    public List<Titles> getTitles() {
//        CompletableFuture<Titles>[] futures = actings.stream()
//                                                     .map(this::getTitlesFuture)
//                                                     .toArray(CompletableFuture[]::new);
//
//        return allOf(futures)
//                .thenApply(
//                        v -> stream(futures)
//                                .map(CompletableFuture::join)
//                                .collect(toList())
//                          )
//                .join();
//    }
//
//    public CompletableFuture<Titles> getTitlesFuture(VoiceActing acting) {
//        return CompletableFuture.supplyAsync(acting::getTitles, executor);
//    }

}
