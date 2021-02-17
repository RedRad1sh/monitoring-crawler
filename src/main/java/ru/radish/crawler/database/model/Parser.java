package ru.radish.crawler.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "\"Parser\"", schema = "public")
@Getter
@Setter
@ToString
public class Parser extends BaseEntity{

    @Column(name = "\"className\"")
    private String className;

    @Column(name = "\"shopName\"")
    private String shopName;

    /**
     * Коллекция планов парсинга
     */
    @JsonIgnore
    @OneToMany(mappedBy = "parser", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Collection<ParsePlan> parsePlanCollection;

}
