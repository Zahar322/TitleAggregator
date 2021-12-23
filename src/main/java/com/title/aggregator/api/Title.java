package com.title.aggregator.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Title implements Serializable {

    private String name;
    private String series;
    private String picture;
    private String url;

}
