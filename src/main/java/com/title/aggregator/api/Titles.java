package com.title.aggregator.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Titles implements Serializable {

    private List<Title> titles;
    private String voiceActing;

    public List<Title> getTitles() {
        if (titles == null) {
            titles = new ArrayList<>();
        }
        return titles;
    }

    public void addTitle(Title title) {
        if (title != null) {
            getTitles().add(title);
        }
    }
}
