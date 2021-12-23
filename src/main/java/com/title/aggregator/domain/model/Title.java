package com.title.aggregator.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

import static com.title.aggregator.utils.Constants.Messages.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Title implements Serializable {

    private String name;
    private String series;
    private String picture;
    private String url;
    private String id;

    public Title(String name, String series, String picture, String url) {
        this.name = name;
        this.series = series;
        this.picture = picture;
        this.url = url;
        this.id = randomAlphanumeric(5);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(name)
                                  .append("\n" + SERIES)
                                  .append(series)
                                  .append("\n" +  PICTURE)
                                  .append("\n" + SOURCE_LINK)
                                  .toString();
    }
}
