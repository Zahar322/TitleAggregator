package com.title.aggregator.domain.converter;

import com.title.aggregator.domain.model.Titles;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TitlesToTitlesConverter implements Converter<Titles, com.title.aggregator.api.Titles> {

    private final TitleToTitleApiConverter converter;

    @Override
    public com.title.aggregator.api.Titles convert(Titles source) {
        com.title.aggregator.api.Titles titles = new com.title.aggregator.api.Titles();
        source.getTitles().forEach(title -> titles.addTitle(converter.convert(title)));
        titles.setVoiceActing(source.getVoiceActing());
        return titles;
    }
}
