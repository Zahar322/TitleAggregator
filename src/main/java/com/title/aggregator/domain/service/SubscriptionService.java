package com.title.aggregator.domain.service;

import com.title.aggregator.domain.model.Title;
import com.title.aggregator.jpa.models.Subscription;
import com.title.aggregator.jpa.models.TelegramUser;
import com.title.aggregator.jpa.models.User;
import com.title.aggregator.jpa.repository.SubscriptionRepository;
import com.title.aggregator.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public List<Subscription> getNotifications(String voiceActing, Title title) {
        return subscriptionRepository.findAllByVoiceActingAndTitleName(voiceActing, title.getName());
    }

    public void create(TelegramUser telegramUser, String voiceActing, Title title) {
        User user = userRepository.findByTelegramUserId(telegramUser.getId());
        Subscription subscription = new Subscription(title.getName(), voiceActing, user);
        subscriptionRepository.save(subscription);
    }

    @Transactional
    public void delete(TelegramUser telegramUser, String voiceActing, Title title) {
        User user = userRepository.findByTelegramUserId(telegramUser.getId());
        subscriptionRepository.deleteByUserIdAndVoiceActingAndTitleName(user.getId(), voiceActing, title.getName());
    }

    public List<Subscription> findSubscriptionByTelegramUser(TelegramUser telegramUser) {
        User user = userRepository.findByTelegramUserId(telegramUser.getId());
        return subscriptionRepository.findAllByUserId(user.getId());
    }

    public void disableSubscriptions(String voiceActing, String titleName) {
        List<Subscription> subscriptions = subscriptionRepository.findAllByVoiceActingAndTitleName(voiceActing, titleName);
        subscriptions.forEach(subscription -> subscription.setEnabled(false));
        subscriptionRepository.saveAll(subscriptions);
    }
}
