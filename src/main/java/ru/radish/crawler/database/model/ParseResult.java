package ru.radish.crawler.database.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "\"ParseResult\"", schema = "public")
@Getter
@Setter
@ToString
public class ParseResult extends BaseEntity{

    @Column(name = "\"parseTime\"")
    private Timestamp parseTime;

    @Column(name = "\"inStock\"")
    private boolean inStock;

    @Column(name = "\"price\"")
    private BigDecimal price;

    /**
     * Ссылка на вышестоящую категорию (если not null)
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "\"parsePlanId\"", referencedColumnName = "id")
    private ParsePlan parsePlan;

}
