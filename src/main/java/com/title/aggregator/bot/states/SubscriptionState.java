package com.title.aggregator.bot.states;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.title.aggregator.domain.model.CallbackData;
import com.title.aggregator.domain.model.Title;
import com.title.aggregator.domain.service.SubscriptionService;
import com.title.aggregator.domain.service.TitlesService;
import com.title.aggregator.jpa.models.TelegramUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.title.aggregator.utils.Constants.Messages.SUBSCRIBE_SUCCESS;
import static com.title.aggregator.utils.TelegramUtils.createMessage;
import static com.title.aggregator.utils.TelegramUtils.getChatId;

@Component
@RequiredArgsConstructor
public class SubscriptionState extends State {

    private final ObjectMapper objectMapper;
    private final SubscriptionService subscriptionService;
    private final SelectVoiceActingState selectState;
    private final TitlesService titlesService;

    @Override
    @SneakyThrows
    public List<BotApiMethod> process(Update update, TelegramUser telegramUser) {
        CallbackQuery query = update.getCallbackQuery();
        CallbackData data = objectMapper.readValue(query.getData(), CallbackData.class);
        Title title = titlesService.findTitleByVoiceActingAndId(data.getDub(), data.getTitle());
        subscriptionService.create(telegramUser, data.getDub(), title);
        Long chatId = getChatId(update);
        return Arrays.asList(createMessage(chatId, createText(title)), selectState.createMessage(chatId));
    }

    private String createText(Title title) {
        return SUBSCRIBE_SUCCESS + title.getName();
    }

    @Override
    protected String getState() {
        return States.SUBSCRIPTION.name();
    }
}
