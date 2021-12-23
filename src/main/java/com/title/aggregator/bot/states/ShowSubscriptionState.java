package com.title.aggregator.bot.states;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.title.aggregator.domain.model.CallbackData;
import com.title.aggregator.domain.service.SubscriptionService;
import com.title.aggregator.domain.service.TitlesService;
import com.title.aggregator.jpa.models.Subscription;
import com.title.aggregator.jpa.models.TelegramUser;
import com.title.aggregator.jpa.models.Titles;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.title.aggregator.bot.states.State.States.SHOW_SUBSCRIPTION;
import static com.title.aggregator.bot.states.State.States.UNSUBSCRIPTION;
import static com.title.aggregator.utils.Constants.Messages.*;
import static com.title.aggregator.utils.TelegramUtils.createMessage;
import static com.title.aggregator.utils.TelegramUtils.getChatId;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Component
@RequiredArgsConstructor
public class ShowSubscriptionState extends State {

    private final SubscriptionService subscriptionService;
    private final TitlesService titlesService;
    private final ObjectMapper objectMapper;

    @Override
    public List<BotApiMethod> process(Update update, TelegramUser telegramUser) {
        Titles titles = titlesService.findFirstTitles();
        return process(update, titles, telegramUser);
    }

//    private List<BotApiMethod> process(Update update, Titles titles, TelegramUser telegramUser) {
//        Long chatId = getChatId(update);
//        List<BotApiMethod> methods = new ArrayList<>();
//        for (Subscription subscription : subscriptionService.findSubscriptionByTelegramUser(telegramUser)) {
//            String voiceActing = subscription.getVoiceActing();
//            com.title.aggregator.domain.model.Titles domainTitles = titles.findTitlesByVoiceActing(voiceActing);
//            methods.add(sendMessageWithEntities(chatId, voiceActing, domainTitles.findTitleByName(subscription.getTitleName())));
//        }
//        addDefaultMethod(methods, chatId);
//        return methods;
//    }

    private List<BotApiMethod> process(Update update, Titles titles, TelegramUser telegramUser) {
        Long chatId = getChatId(update);
        List<Subscription> subscriptions = subscriptionService.findSubscriptionByTelegramUser(telegramUser);
        return isNotEmpty(subscriptions) ? Collections.singletonList(createSendMessage(chatId, titles, subscriptions)) : Collections.singletonList(createMessage(chatId, EMPTY_SUBSCRIPTIONS));
    }

    private SendMessage createSendMessage(Long chatId, Titles titles, List<Subscription> subscriptions) {
        com.title.aggregator.domain.model.Titles domainTitles = titlesService.findTitlesFromSubscriptions(subscriptions, titles);
        return sendMessage(domainTitles, chatId, MY_SUBSCRIPTIONS);
    }

    private void addDefaultMethod(List<BotApiMethod> methods, Long chatId) {
        if (methods.isEmpty()) {
            methods.add(createMessage(chatId, EMPTY_SUBSCRIPTIONS));
        }
    }

    @Override
    protected String getState() {
        return SHOW_SUBSCRIPTION.name();
    }

    @Override
    protected CallbackData createCallbackData(String voiceActing, String titleId) {
        return new CallbackData(UNSUBSCRIPTION.ordinal(), voiceActing, titleId, UNSUBSCRIBE);
    }

    @Override
    @SneakyThrows
    protected List<List<InlineKeyboardButton>> createButtons(CallbackData data) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(data.getMessage());
        inlineKeyboardButton.setCallbackData(objectMapper.writeValueAsString(data));
        return Collections.singletonList(Collections.singletonList(inlineKeyboardButton));
    }

    @Override
    protected CallbackData createCallbackData(String voiceActing, String titleId, String message) {
        return new CallbackData(States.SELECT_SUBSCRIPTION.ordinal(), voiceActing, titleId, message);
    }
}
