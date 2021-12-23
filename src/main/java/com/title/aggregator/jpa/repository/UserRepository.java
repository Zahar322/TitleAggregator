package com.title.aggregator.jpa.repository;

import com.title.aggregator.jpa.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByTelegramUserId(Long telegramUserId);
}
