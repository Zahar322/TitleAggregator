package com.title.aggregator.bot.states;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.title.aggregator.domain.model.CallbackData;
import com.title.aggregator.domain.model.Title;
import com.title.aggregator.domain.model.Titles;
import com.title.aggregator.domain.service.SubscriptionService;
import com.title.aggregator.domain.service.TitlesService;
import com.title.aggregator.jpa.models.Subscription;
import com.title.aggregator.jpa.models.TelegramUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.title.aggregator.utils.Constants.Messages.SHOW_TITLES;
import static com.title.aggregator.utils.Constants.Messages.SUBSCRIBE;

@Slf4j
@Component
@RequiredArgsConstructor
public class SelectTitleState extends State {

    private final TitlesService titlesService;
    private final ObjectMapper objectMapper;
    private final SubscriptionService subscriptionService;

    @Override
    public List<BotApiMethod> process(Update update, TelegramUser telegramUser) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        return callbackQuery == null ? Collections.emptyList() : process(callbackQuery, telegramUser);
    }

    @Override
    protected String getState() {
        return States.SELECT_TITLE.name();
    }

    @SneakyThrows
    private List<BotApiMethod> process(CallbackQuery callbackQuery, TelegramUser telegramUser) {
        Long chatId = callbackQuery.getFrom().getId();
        String voiceActing = objectMapper.readValue(callbackQuery.getData(), CallbackData.class).getDub();
        Titles titles = titlesService.findTitlesByVoiceActing(voiceActing);
        log.info(objectMapper.writeValueAsString(titles));
        return titles != null ? process(titles, chatId, telegramUser) : Collections.emptyList();
    }

//    private List<BotApiMethod> process(Titles titles, Long chatId) {
//        return titles.getTitles().stream()
//                .map(title -> sendMessage(chatId, title))
//                .collect(toList());
//    }

    private List<BotApiMethod> process(Titles titles, Long chatId, TelegramUser telegramUser) {
        filterTitles(titles, telegramUser);
        return titlesService.divideTitlesOnHalf(titles).stream()
                .map(domainTitles -> sendMessage(domainTitles, chatId, SHOW_TITLES + titles.getVoiceActing()))
                .collect(Collectors.toList());
//        return Collections.singletonList(sendMessage(titles, chatId, SHOW_TITLES + titles.getVoiceActing()));
    }

    private void filterTitles(Titles titles, TelegramUser telegramUser) {
        List<Subscription> subscriptions = subscriptionService.findSubscriptionByTelegramUser(telegramUser);
        titles.getTitles().removeIf(title -> filterTitle(title, subscriptions));
    }

    private boolean filterTitle(Title title, List<Subscription> subscriptions) {
        return subscriptions.stream()
                            .anyMatch(subscription -> subscription.getTitleName().equals(title.getName()));
    }

    @Override
    @SneakyThrows
    protected List<List<InlineKeyboardButton>> createButtons(CallbackData data) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(data.getMessage());
        inlineKeyboardButton.setCallbackData(objectMapper.writeValueAsString(data));
        return Collections.singletonList(Collections.singletonList(inlineKeyboardButton));
    }

    @Override
    protected CallbackData createCallbackData(String voiceActing, String titleId) {
        return new CallbackData(States.SUBSCRIPTION.ordinal(), voiceActing, titleId, SUBSCRIBE);
    }

    @Override
    protected CallbackData createCallbackData(String voiceActing, String titleId, String message) {
        return new CallbackData(States.SHOW_TITLE.ordinal(), voiceActing, titleId, message);
    }
}
