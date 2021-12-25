package com.title.aggregator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.title.aggregator.domain.service.BotService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.nio.charset.Charset;

import static com.title.aggregator.utils.Constants.MDCKeys.CHAT_ID;
import static com.title.aggregator.utils.TelegramUtils.getChatId;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/bot", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BotController {

    private final BotService botService;
    private final ObjectMapper mapper;

    @PostMapping
    @SneakyThrows
    public BotApiMethod receiveUpdate(@RequestBody Update update) {
        MDC.put(CHAT_ID, getChatId(update).toString());
        log.info(mapper.writeValueAsString(update));
        log.info(String.valueOf(Charset.defaultCharset()));
        return botService.updateReceived(update);
    }
}
