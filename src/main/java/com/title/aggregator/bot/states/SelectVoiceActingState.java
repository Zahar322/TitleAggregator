package com.title.aggregator.bot.states;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.title.aggregator.domain.model.CallbackData;
import com.title.aggregator.jpa.models.TelegramUser;
import com.title.aggregator.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.title.aggregator.utils.Constants.Messages.CHOOSE_VOICE_ACTING_MESSAGE;
import static com.title.aggregator.utils.Constants.VoiceActing.VOICE_ACTINGS;
import static com.title.aggregator.utils.TelegramUtils.getChatId;

@Component
@RequiredArgsConstructor
public class SelectVoiceActingState extends State {

    private final ObjectMapper objectMapper;

    @Override
    public List<BotApiMethod> process(Update update, TelegramUser telegramUser) {
        Long chatId = getChatId(update);
        return Arrays.asList(createMessage(chatId));
    }

    @Override
    protected String getState() {
        return States.SELECT_VOICE_ACTING.name();
    }

    public SendMessage createMessage(Long chatId) {
        SendMessage sendMessage = TelegramUtils.createMessage(chatId, CHOOSE_VOICE_ACTING_MESSAGE);
        sendMessage.setReplyMarkup(createReplyKeyboard(new CallbackData(States.SELECT_TITLE.ordinal())));
        return sendMessage;
    }

    protected List<List<InlineKeyboardButton>> createButtons(CallbackData message) {
        return VOICE_ACTINGS.stream()
                            .map(message::setDub)
                            .map(this::createButton)
                            .collect(Collectors.toList());
    }

    @SneakyThrows
    private List<InlineKeyboardButton> createButton(CallbackData voiceActing) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(voiceActing.getDub());
        inlineKeyboardButton.setCallbackData(objectMapper.writeValueAsString(voiceActing));
        return Collections.singletonList(inlineKeyboardButton);
    }
}
