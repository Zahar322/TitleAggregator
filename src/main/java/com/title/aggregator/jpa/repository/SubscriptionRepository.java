package com.title.aggregator.jpa.repository;

import com.title.aggregator.jpa.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByVoiceActingAndTitleName(String voiceActing, String titleName);

    void deleteByUserIdAndVoiceActingAndTitleName(Long userId, String voiceActing, String titleName);

    List<Subscription> findAllByUserId(Long userId);
}
