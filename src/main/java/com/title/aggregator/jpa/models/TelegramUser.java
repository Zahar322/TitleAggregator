package com.title.aggregator.jpa.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
public class TelegramUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long chatId;
    @Column
    private String state;

    public TelegramUser(Long chatId, String state) {
        this.chatId = chatId;
        this.state = state;
    }
}
