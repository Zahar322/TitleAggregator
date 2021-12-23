package com.title.aggregator.utils;

import com.title.aggregator.bot.states.State;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.title.aggregator.bot.states.State.States.SELECT_VOICE_ACTING;
import static com.title.aggregator.bot.states.State.States.SHOW_SUBSCRIPTION;
import static com.title.aggregator.utils.Constants.Anidub.ANIDUB;
import static com.title.aggregator.utils.Constants.Anilibria.ANILIBRIA;
import static com.title.aggregator.utils.Constants.Animaunt.ANIMAUNT;
import static com.title.aggregator.utils.Constants.Anivost.ANIVOST;
import static com.title.aggregator.utils.Constants.Messages.BACK;

@UtilityClass
public class Constants {

    @UtilityClass
    public static final class Tags {

        public static final String ANCHOR = "a";
        public static final String HREF = "href";
        public static final String IMAGE = "img";
    }

    @UtilityClass
    public static final class Anidub {

        public static final String ANIDUB = "anidub";
        public static final String VOICE_ACTING_URL = "https://anime.anidub.life/anime/anime_ongoing/";
        public static final String ELEMENT_CLASS = "th-itemb";
        public static final String TITLE_CLASS_NAME = "th-in";
        public static final String SERIES_NUMBER_CLASS_NAME = "th-lastser";
        public static final String BASE_URL = "https://anime.anidub.life/";
    }

    @UtilityClass
    public static final class Anilibria {

        public static final String ANILIBRIA = "anilibria";
        public static final String VOICE_ACTING_URL = "https://www.anilibria.tv/pages/schedule.php";
        public static final String BASE_URL = "https://www.anilibria.tv";
        public static final String ELEMENT_CLASS = "goodcell";
        public static final String TITLE_CLASS_NAME = "schedule-runame";
        public static final String SERIES_NUMBER_CLASS_NAME = "schedule-series";

    }

    @UtilityClass
    public static final class Animaunt {

        public static final String ANIMAUNT = "animaunt";
        public static final String VOICE_ACTING_URL = "https://animaunt.org/ongoing/";
        public static final String ELEMENT_CLASS = "th-in";
        public static final String TITLE_CLASS_NAME = "th-title";
        public static final String SERIES_NUMBER_CLASS_NAME = "extra-series";
        public static final String BASE_URL = "https://animaunt.org/";
        public static final String PATTERN = "\\d+";

    }

    @UtilityClass
    public static final class Anivost {

        public static final String ANIVOST = "anivost";
        public static final String VOICE_ACTING_URL = "https://animevost.am/ongoing/";
        public static final String ELEMENT_CLASS = "shortstory";
        public static final String TITLE_CLASS_NAME = "th-in";
        public static final String SERIES_NUMBER_CLASS_NAME = "th-lastser";
        public static final String BASE_URL = "https://animevost.am/";

    }

    @UtilityClass
    public static final class VoiceActing {

        public static final List<String> VOICE_ACTINGS = Arrays.asList(ANIDUB, ANILIBRIA, ANIMAUNT, ANIVOST);
    }

    @UtilityClass
    public static final class States {

        public static final String INITIAL = "initial";
        public static final String SELECT_VOICE_ACTING = "select_voice_acting";
        public static final String SELECT_TITLE = "select_title";
        public static final String SUBSCRIPTION = "subscription";
    }

    @UtilityClass
    public static final class Messages {

        public static final String SERIES = "Cерия - ";
        public static final String SOURCE_LINK = "Смотреть";
        public static final String PICTURE = "Ссылка на картинку";
        public static final String SUBSCRIBE = "Подписаться";
        public static final String HELLO_MESSAGE = "Бот для подписки на новые выходящиеся тайтлы. Подписывайтесь на любымые тайтлы чтобы не пропускать выход серий";
        public static final String CHOOSE_VOICE_ACTING_MESSAGE = "Выбирете озвучку на которую хотите подписаться";
        public static final String SUBSCRIBE_SUCCESS = "Вы подписались на обновления тайтла ";
        public static final String BACK = "Вернуться назад";
        public static final String SHOW_SUBSCRIPTIONS = "Показать мои подписки";
        public static final String UNSUBSCRIBE = "Отписаться";
        public static final String UNSUBSCRIBE_SUCCESS = "Вы отписались на обновления тайтла ";
        public static final String EMPTY_SUBSCRIPTIONS = "У вас нет активных подписок";
        public static final String SHOW_TITLES = "Показаны тайтлы с озвучкой ";
        public static final String MY_SUBSCRIPTIONS = "Ваши подписки";
    }

    @UtilityClass
    public static final class Maps {

        public static final Map<String, State.States> MESSAGE_TO_STATE = Map.of(BACK, SELECT_VOICE_ACTING,
                Messages.SHOW_SUBSCRIPTIONS, SHOW_SUBSCRIPTION,
                Commands.SUBSCRIPTIONS, SHOW_SUBSCRIPTION
        );
    }

    @UtilityClass
    public static final class MDCKeys {

        public static final String CHAT_ID = "chatId";
        public static final String UPDATE_TITLES = "update-titles";
        public static final String DISABLE_SUBSCRIPTION = "disable-subscription";
    }

    @UtilityClass
    public static final class Commands {

        public static final String SUBSCRIPTIONS = "/subscriptions";
    }
}
