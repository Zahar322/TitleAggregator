package com.title.aggregator.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.title.aggregator.bot.states.State;
import com.title.aggregator.beans.TelegramBotProperties;
import com.title.aggregator.bot.states.StateHelper;
import com.title.aggregator.domain.model.Title;
import com.title.aggregator.domain.service.SubscriptionService;
import com.title.aggregator.jpa.models.Subscription;
import com.title.aggregator.jpa.models.TelegramUser;
import com.title.aggregator.jpa.models.User;
import com.title.aggregator.jpa.repository.TelegramUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.title.aggregator.utils.Constants.Messages.*;
import static com.title.aggregator.utils.TelegramUtils.getChatId;

@Slf4j
@Component
@RequiredArgsConstructor
public class TitleBot extends TelegramWebhookBot {

    private final TelegramUserRepository telegramRepository;
    private final SubscriptionService subscriptionService;
    private final List<State> states;
    private final StateHelper stateHelper;
    private final ObjectMapper objectMapper;
    private final TelegramBotProperties properties;

    @Override
    public String getBotToken() {
        return properties.getToken();
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            Long chatId = getChatId(update);
            TelegramUser telegramUser = telegramRepository.findByChatId(chatId);
            stateHelper.setCurrentState(telegramUser, update);
            State currentState = states.stream().filter(state -> state.canProcess(telegramUser)).findFirst().orElse(null);
            currentState.process(update, telegramUser).forEach(this::executeWithoutException);
        } catch (Exception e) {
            log.error("Unable fullfil state", e);
        }
        return null;
    }

    @Override
    public String getBotPath() {
        return "/";
    }

    @Override
    public String getBotUsername() {
        return properties.getName();
    }

    @SneakyThrows
    private void executeWithoutException(BotApiMethod method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            log.error("Unable execute method {} ", objectMapper.writeValueAsString(method), e);
        }
    }

    public void sendMessage(String voiceActing, Title title) {
        subscriptionService.getNotifications(voiceActing, title)
                           .forEach(subscription -> sendMessage(subscription, title));
    }

    private void sendMessage(Subscription subscription, Title title) {
        try {
            log.info(objectMapper.writeValueAsString(subscription));
        } catch (JsonProcessingException e) {
           log.error("logging subscription was failed", e);
        }
        User user = subscription.getUser();
        TelegramUser telegramUser = user.getTelegramUser();
        sendMessage(telegramUser.getChatId(), title);
    }

    @SneakyThrows
    private void sendMessage(Long chatId, Title title) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        String message = createMessage(title);
        sendMessage.setText(message);
        MessageEntity imageEntity = messageEntity(message.length() -  SOURCE_LINK.length() - 1 - PICTURE.length(), PICTURE.length(), "text_link", title.getPicture());
        MessageEntity sourceEntity = messageEntity(message.length() - SOURCE_LINK.length(), SOURCE_LINK.length(), "text_link", title.getUrl());
        sendMessage.setEntities(Arrays.asList(sourceEntity, imageEntity));
        sendMessage.setReplyMarkup(createDefaultReplyKeyboard());
        execute(sendMessage);
    }

    private MessageEntity messageEntity(int offset, int lenght, String type, String url) {
        MessageEntity entity = new MessageEntity();
        entity.setUrl(url);
        entity.setOffset(offset);
        entity.setLength(lenght);
        entity.setType(type);
        return entity;
    }

    private String createMessage(Title title) {
        return new StringBuilder().append("Вышла " + title.getSeries() + " серия тайтла ")
                                  .append(title.getName())
                                  .append("\n" +  PICTURE)
                                  .append("\n" + SOURCE_LINK)
                                  .toString();
    }

    public ReplyKeyboard createDefaultReplyKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(createRow());
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    private List<KeyboardRow> createRow() {
        KeyboardRow keyboardButtons = new KeyboardRow();
        keyboardButtons.add(createKeyboardButton(BACK));
        keyboardButtons.add(createKeyboardButton(SHOW_SUBSCRIPTIONS));
        return Collections.singletonList(keyboardButtons);
    }

    private KeyboardButton createKeyboardButton(String text) {
        return new KeyboardButton(text);
    }
}
