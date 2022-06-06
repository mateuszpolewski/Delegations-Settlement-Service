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
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int id;
    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
    private Set<Expense> expenses;
}
