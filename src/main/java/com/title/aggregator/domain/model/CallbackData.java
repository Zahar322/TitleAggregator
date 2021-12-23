package com.title.aggregator.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallbackData {

    private int state;
    private String dub;
    private String title;
    @JsonIgnore
    private String message;

    public CallbackData(int state) {
        this.state = state;
    }

    public CallbackData(int state, String dub, String title) {
        this.state = state;
        this.dub = dub;
        this.title = title;
    }

    public CallbackData setDub(String dub) {
        this.dub = dub;
        return this;
    }
}
