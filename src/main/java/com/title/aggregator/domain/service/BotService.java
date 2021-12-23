package com.title.aggregator.domain.service;

import com.title.aggregator.bot.TitleBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
public class BotService {

    private final TitleBot titleBot;
    private final Executor botExecutor;

    public BotApiMethod<?> updateReceived(Update update) {
        botExecutor.execute(() -> {
            titleBot.onWebhookUpdateReceived(update);
        });
        return null;
    }
}
