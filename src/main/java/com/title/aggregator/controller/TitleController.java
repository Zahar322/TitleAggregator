package com.title.aggregator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.title.aggregator.api.TitleResponse;
import com.title.aggregator.api.TitlesRequest;
import com.title.aggregator.domain.model.Titles;
import com.title.aggregator.domain.service.SenderService;
import com.title.aggregator.domain.service.TitlesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.title.aggregator.utils.Constants.MDCKeys.CHAT_ID;
import static com.title.aggregator.utils.Constants.MDCKeys.UPDATE_TITLES;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/titles", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TitleController {

    private final ConversionService conversionService;
    private final TitlesService titlesService;
    private final SenderService senderService;
    private final ObjectMapper mapper;

    @GetMapping
    public TitleResponse getTitles() {
        List<Titles> titles = titlesService.findTitles();
        TitleResponse titleResponse = new TitleResponse();
        titles.forEach(modelTitles -> titleResponse.addTitles(conversionService.convert(modelTitles, com.title.aggregator.api.Titles.class)));
        return titleResponse;
    }

    @PostMapping
    public void sendTitles(@RequestBody TitlesRequest request) {
        MDC.put(CHAT_ID, UPDATE_TITLES);
        try {
            log.info(mapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            log.error("Write log failed ", e);
        }
        senderService.updateTitles(request.getTitles());
    }
}
