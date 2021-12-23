package com.title.aggregator.api;

import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
public class TitleResponse implements Serializable {

    private List<Titles> titles;

    public List<Titles> getTitles() {
        if (titles == null) {
            titles = new ArrayList<>();
        }
        return titles;
    }

    public void addTitles(Titles titles) {
        getTitles().add(titles);
    }
}
