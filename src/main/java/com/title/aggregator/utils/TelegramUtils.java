package com.title.aggregator.utils;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@UtilityClass
public class TelegramUtils {

    public static Long getChatId(Update update) {
        return getMessage(update).getChatId();
    }

    public static Message getMessage(Update update) {
        Message message = update.getMessage();
        return message != null ? message : update.getCallbackQuery().getMessage();
    }

    public static SendMessage createMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);
        return sendMessage;
    }
}
