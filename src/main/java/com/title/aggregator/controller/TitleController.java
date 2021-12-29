package com.title.aggregator.controller;

import com.title.aggregator.api.TitleResponse;
import com.title.aggregator.api.TitlesRequest;
import com.title.aggregator.domain.model.Titles;
import com.title.aggregator.domain.service.SenderService;
import com.title.aggregator.domain.service.TitlesService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/titles", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TitleController {

    private final ConversionService conversionService;
    private final TitlesService titlesService;
    private final SenderService senderService;

    @GetMapping
    public TitleResponse getTitles() {
        List<Titles> titles = titlesService.findTitles();
        TitleResponse titleResponse = new TitleResponse();
        titles.forEach(modelTitles -> titleResponse.addTitles(conversionService.convert(modelTitles, com.title.aggregator.api.Titles.class)));
        return titleResponse;
    }

    @PostMapping
    public void sendTitles(@RequestBody TitlesRequest request) {
        senderService.updateTitles(request.getTitles());
    }
}
