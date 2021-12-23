package com.title.aggregator.bot.states;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.title.aggregator.domain.model.CallbackData;
import com.title.aggregator.domain.model.Title;
import com.title.aggregator.domain.service.TitleService;
import com.title.aggregator.domain.service.TitlesService;
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

import static com.title.aggregator.bot.states.State.States.SHOW_TITLE;
import static com.title.aggregator.utils.Constants.Messages.SUBSCRIBE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShowTitleState extends State {

    private final TitlesService titlesService;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public List<BotApiMethod> process(Update update, TelegramUser telegramUser) {
        CallbackQuery query = update.getCallbackQuery();
        Long chatId = query.getFrom().getId();
        CallbackData data = objectMapper.readValue(query.getData(), CallbackData.class);
        Title title = titlesService.findTitleByVoiceActingAndId(data.getDub(), data.getTitle());
        return Collections.singletonList(sendMessageWithEntities(chatId, data.getDub(), title));
    }

    @Override
    protected String getState() {
        return SHOW_TITLE.name();
    }

    @Override
    protected CallbackData createCallbackData(String voiceActing, String titleId) {
        return new CallbackData(States.SUBSCRIPTION.ordinal(), voiceActing, titleId, SUBSCRIBE);
    }

    @Override
    @SneakyThrows
    protected List<List<InlineKeyboardButton>> createButtons(CallbackData data) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(data.getMessage());
        inlineKeyboardButton.setCallbackData(objectMapper.writeValueAsString(data));
        return Collections.singletonList(Collections.singletonList(inlineKeyboardButton));
    }
}
