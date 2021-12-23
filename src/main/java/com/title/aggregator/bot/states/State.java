package com.title.aggregator.bot.states;

import com.title.aggregator.domain.model.CallbackData;
import com.title.aggregator.domain.model.Title;
import com.title.aggregator.domain.model.Titles;
import com.title.aggregator.jpa.models.TelegramUser;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

import static com.title.aggregator.utils.Constants.Messages.*;
import static com.title.aggregator.utils.Constants.Messages.SOURCE_LINK;
import static com.title.aggregator.utils.TelegramUtils.createMessage;
import static java.util.stream.Collectors.toList;

public abstract class State {

    public abstract List<BotApiMethod> process(Update update, TelegramUser telegramUser);

    public boolean canProcess(TelegramUser telegramUser) {
        return telegramUser != null && getState().equalsIgnoreCase(telegramUser.getState());
    }

    protected abstract String getState();

    protected ReplyKeyboard createReplyKeyboard(CallbackData data) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(createButtons(data));
        return inlineKeyboardMarkup;
    }

    protected CallbackData createCallbackData(String voiceActing, String titleId) {
        return null;
    }

    protected List<List<InlineKeyboardButton>> createButtons(CallbackData data) {
        return Collections.emptyList();
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

    public SendMessage sendMessageWithEntities(Long chatId, String voiceActing, Title title) {
        String message = title.toString();
        SendMessage sendMessage = createMessage(chatId, message);
        MessageEntity headerEntity = messageEntity(0, title.getName().length(), "bold", null);
        MessageEntity imageEntity = messageEntity(message.length() -  SOURCE_LINK.length() - 1 - PICTURE.length(), PICTURE.length(), "text_link", title.getPicture());
        MessageEntity sourceEntity = messageEntity(message.length() - SOURCE_LINK.length(), SOURCE_LINK.length(), "text_link", title.getUrl());
        sendMessage.setEntities(Arrays.asList(headerEntity, sourceEntity, imageEntity));
        CallbackData data = createCallbackData(voiceActing, title.getId());
        sendMessage.setReplyMarkup(createReplyKeyboard(data));
        return sendMessage;
    }

    private MessageEntity messageEntity(int offset, int lenght, String type, String url) {
        MessageEntity entity = new MessageEntity();
        entity.setUrl(url);
        entity.setOffset(offset);
        entity.setLength(lenght);
        entity.setType(type);
        return entity;
    }

    public SendMessage sendMessage(Titles titles, Long chatId, String message) {
        SendMessage sendMessage = createMessage(chatId, message);
        sendMessage.setReplyMarkup(createKeyboard(titles));
        return sendMessage;
    }

    private ReplyKeyboard createKeyboard(Titles titles) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = createButtons(titles);
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }

    private List<List<InlineKeyboardButton>> createButtons(Titles titles) {
        return titles.getTitles().stream()
                                 .map(title -> createCallbackData(titles.getVoiceActing(), title.getId(), title.getName()))
                                 .map(this::createButtons)
                                 .flatMap(Collection::stream)
                                 .collect(toList());
    }

    protected CallbackData createCallbackData(String voiceActing, String titleId, String message) {
        return null;
    }

    public enum States {
        INITIAL,
        SELECT_VOICE_ACTING,
        SELECT_TITLE,
        SHOW_TITLE,
        SUBSCRIPTION,
        SHOW_SUBSCRIPTION,
        SELECT_SUBSCRIPTION,
        UNSUBSCRIPTION;
    }
}
