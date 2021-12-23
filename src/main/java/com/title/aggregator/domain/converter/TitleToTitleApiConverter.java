package com.title.aggregator.domain.converter;

import com.title.aggregator.domain.model.Title;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TitleToTitleApiConverter implements Converter<Title, com.title.aggregator.api.Title> {

    @Override
    public com.title.aggregator.api.Title convert(Title source) {
        com.title.aggregator.api.Title title = new com.title.aggregator.api.Title();
        title.setName(source.getName());
        title.setPicture(source.getPicture());
        title.setSeries(source.getSeries());
        title.setUrl(source.getUrl());
        return title;
    }
}
