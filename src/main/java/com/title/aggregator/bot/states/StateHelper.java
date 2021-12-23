package com.title.aggregator.bot.states;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.title.aggregator.domain.model.CallbackData;
import com.title.aggregator.jpa.models.TelegramUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.title.aggregator.utils.Constants.Maps.MESSAGE_TO_STATE;

@Component
@RequiredArgsConstructor
public class StateHelper {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void setCurrentState(TelegramUser user, Update update) {
        if (user != null) {
            String state = getCurrentState(user, update);
            user.setState(state);
        }
    }

    @SneakyThrows
    private String getCurrentState(TelegramUser user, Update update) {
        if (update.hasCallbackQuery()) {
            CallbackData callbackData = objectMapper.readValue(update.getCallbackQuery().getData(), CallbackData.class);
            return State.States.values()[callbackData.getState()].name();
        }
        if (update.hasMessage()) {
            State.States state = MESSAGE_TO_STATE.get(update.getMessage().getText());
            return state != null ? state.name() : user.getState();
        }
        return user.getState();
    }
}
