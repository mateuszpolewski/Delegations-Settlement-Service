package pl.pracainz.delegacje.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private int id;
    @Column(name = "name")
    private String name;
    /*
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "currency")
    private Expense expense;
    */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "currency")
    private Set<Expense> expenses;
}

