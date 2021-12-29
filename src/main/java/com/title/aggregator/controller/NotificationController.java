package com.title.aggregator.controller;

import com.title.aggregator.domain.service.SenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/notification", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NotificationController {

    private final SenderService senderService;

    @GetMapping
    public void sendNotification() {
        senderService.sendNotification();
    }
}
