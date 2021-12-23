package com.title.aggregator.jpa.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "telegram_user_id")
    private TelegramUser telegramUser;

    public User() {
    }

    public User(TelegramUser telegramUser) {
        this.telegramUser = telegramUser;
    }
}
