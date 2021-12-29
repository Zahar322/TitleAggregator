package com.title.aggregator.domain.service;

import com.title.aggregator.api.TitlesRequest;
import com.title.aggregator.clients.TitleClient;
import com.title.aggregator.domain.model.Titles;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "scheduler.enabled", havingValue = "true")
public class NotificationService {

    private final TitlesService titlesService;
    private final TitleClient titleClient;

    @Scheduled(fixedDelay = 600000)
    public void sendNotification() {
        List<Titles> titles = titlesService.getTitles();
        titleClient.sendTitles(new TitlesRequest(titles));
    }

}
