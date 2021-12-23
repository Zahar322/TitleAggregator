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
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String titleName;
    @Column
    private String voiceActing;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private boolean enabled;

    public Subscription(String titleName, String voiceActing, User user) {
        this.titleName = titleName;
        this.voiceActing = voiceActing;
        this.user = user;
    }
}
