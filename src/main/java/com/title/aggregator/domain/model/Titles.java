package com.title.aggregator.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Titles implements Serializable {

    private List<Title> titles;
    private String voiceActing;

    public Titles() {
    }

    public Titles(List<Title> titles, String voiceActing) {
        this.titles = titles;
        this.voiceActing = voiceActing;
    }

    public List<Title> getTitles() {
        if (titles == null) {
            titles = new ArrayList<>();
        }
        return titles;
    }

    public void addTitle(Title title) {
        getTitles().add(title);
    }

    public Title findTitleBydId(String titleId) {
        return getTitles().stream()
                          .filter(title -> titleId.equals(title.getId()))
                          .findFirst()
                          .orElse(null);
    }

    public Pair<String, Title> findTitleWithVoiceActingById(String titleId) {
        Title title = findTitleBydId(titleId);
        return title != null ? Pair.of(getVoiceActing(), title) : null;
    }

    public Title findTitleByName(String name) {
        return getTitles().stream()
                          .filter(title -> name.equals(title.getName()))
                          .findFirst()
                          .orElse(null);
    }
}
