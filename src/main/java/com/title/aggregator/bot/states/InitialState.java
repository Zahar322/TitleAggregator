package com.title.aggregator.bot.states;

import com.title.aggregator.jpa.models.TelegramUser;
import com.title.aggregator.jpa.models.User;
import com.title.aggregator.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

import static com.title.aggregator.utils.Constants.Messages.HELLO_MESSAGE;
import static com.title.aggregator.utils.Constants.States.SELECT_VOICE_ACTING;
import static com.title.aggregator.utils.TelegramUtils.createMessage;

@Component
@RequiredArgsConstructor
public class InitialState extends State {

    private final SelectVoiceActingState selectState;
    private final UserRepository userRepository;

    @Override
    public List<BotApiMethod> process(Update update, TelegramUser user) {
        Long chatId = update.getMessage().getChatId();
        TelegramUser telegramUser = new TelegramUser(chatId, SELECT_VOICE_ACTING, update.getMessage().getFrom());
        userRepository.save(new User(telegramUser));
        return Arrays.asList(createInitMessage(chatId), selectState.createMessage(chatId));
    }

    @Override
    public boolean canProcess(TelegramUser telegramUser) {
        return telegramUser == null;
    }

    @Override
    protected String getState() {
        return States.INITIAL.name();
    }

    private SendMessage createInitMessage(Long chatId) {
        SendMessage sendMessage = createMessage(chatId, HELLO_MESSAGE);
        sendMessage.setReplyMarkup(createDefaultReplyKeyboard());
        return sendMessage;
    }
}
