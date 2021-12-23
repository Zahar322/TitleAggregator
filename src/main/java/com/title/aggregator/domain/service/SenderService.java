package com.title.aggregator.domain.service;

import com.title.aggregator.bot.TitleBot;
import com.title.aggregator.domain.model.Title;
import com.title.aggregator.domain.model.Titles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.title.aggregator.utils.Constants.MDCKeys.CHAT_ID;
import static com.title.aggregator.utils.Constants.MDCKeys.UPDATE_TITLES;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "scheduler.enabled", havingValue = "true")
public class SenderService {

    private final TitleBot titleBot;
    private final TitlesService titlesService;
    private final SubscriptionService subscriptionService;

    @Scheduled(fixedDelay = 40000)
    public void updateTitles() {
        MDC.put(CHAT_ID, UPDATE_TITLES);
        List<Titles> titles = titlesService.getTitles();
        com.title.aggregator.jpa.models.Titles jpaTitles = titlesService.findFirstTitles();
        jpaTitles.getTitles().forEach(fromJpaTitles -> sendNotification(titles, fromJpaTitles));
        titlesService.save(titles);
    }

    private void sendNotification(List<Titles> titles, Titles fromJpaTitles) {
        titles.stream()
              .filter(domainTitles -> domainTitles.getVoiceActing().equals(fromJpaTitles.getVoiceActing()))
              .findFirst()
              .ifPresent(domainTitles -> sendNotification(domainTitles, fromJpaTitles));
    }

    private void sendNotification(Titles domainTitles, Titles fromJpaTitles) {
        fromJpaTitles.getTitles().forEach(title -> sendNotification(domainTitles, title));
    }

    private void sendNotification(Titles domainTitles, Title fromJpaTitle) {
        domainTitles.getTitles().stream()
                                .filter(domainTitle -> domainTitle.getName().equals(fromJpaTitle.getName()))
                                .findFirst()
                                .ifPresentOrElse(
                                        domainTitle -> sendNotification(domainTitles.getVoiceActing(), domainTitle, fromJpaTitle),
                                        () -> subscriptionService.disableSubscriptions(domainTitles.getVoiceActing(), fromJpaTitle.getName()));
    }

    private void sendNotification(String voiceActing, Title domainTitle, Title fromJpaTitle) {
        if (hasUpdate(domainTitle, fromJpaTitle)) {
            sendMessage(voiceActing, domainTitle);
        }
    }

    private boolean hasUpdate(Title domainTitle, Title fromJpaTitle) {
        String currentSeries = domainTitle.getSeries();
        if (currentSeries == null) {
            return false;
        }
        return !currentSeries.equals(fromJpaTitle.getSeries());
    }

    private void sendMessage(String voiceActing, Title title) {
        titleBot.sendMessage(voiceActing, title);
    }

}
