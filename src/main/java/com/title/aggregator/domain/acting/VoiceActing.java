package com.title.aggregator.domain.acting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.title.aggregator.domain.model.Titles;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.StringWriter;

@Slf4j
public abstract class VoiceActing {

    @Resource
    private ObjectMapper objectMapper;

    @SneakyThrows
    public Titles getTitles() {
        Titles titles = createTitles();
        StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, titles);
        log.info("titles from {}, {}", getVoiceActing(), writer.toString());
        return titles;
    }

    public abstract Titles createTitles();

    public abstract String getVoiceActing();
}
