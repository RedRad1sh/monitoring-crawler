package ru.radish.crawler.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "\"Scheduler\"", schema = "public")
@Getter
@Setter
@ToString
public class Scheduler extends BaseEntity{

    @Column(name = "\"trigger\"")
    private String trigger;

    /**
     * Коллекция планов парсинга
     */
    @JsonIgnore
    @OneToMany(mappedBy = "scheduler", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Collection<ParsePlan> parsePlanCollection;

}
