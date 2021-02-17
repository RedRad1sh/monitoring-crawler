package ru.radish.crawler.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "\"ParsePlan\"", schema = "public")
@Getter
@Setter
@ToString
public class ParsePlan extends BaseEntity{

    @Column(name = "\"active\"")
    private boolean active;

    @Column(name = "\"url\"")
    private String url;

    /**
     * Ссылка на привязанный парсер
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "\"parserId\"", referencedColumnName = "id")
    private Parser parser;

    /**
     * Ссылка на привязанный шедулер
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "\"schedulerId\"", referencedColumnName = "id")
    private Scheduler scheduler;

    /**
     * Коллекция результатов парсинга
     */
    @JsonIgnore
    @OneToMany(mappedBy = "parsePlan", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Collection<ParseResult> parseResultCollection;

}
