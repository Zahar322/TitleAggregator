package com.title.aggregator.api;

import com.title.aggregator.domain.model.Titles;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TitlesRequest {

    private List<Titles> titles;
}
