package com.title.aggregator.jpa.models;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "titles")
@Entity
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Getter
@Setter
public class Titles implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Type(type = "json")
    @Column(columnDefinition = "json", name = "titles")
    private List<com.title.aggregator.domain.model.Titles> titles;

    public com.title.aggregator.domain.model.Titles findTitlesByVoiceActing(String voiceActing) {
        return titles.stream()
                .filter(title -> voiceActing.equals(title.getVoiceActing()))
                .findFirst()
                .orElse(null);
    }
}
