package com.title.aggregator.jpa.repository;

import com.title.aggregator.jpa.models.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {

    TelegramUser findByChatId(Long chatId);
}
