package pl.pracainz.delegacje.model;


import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import pl.pracainz.delegacje.repository.PhotoRepository;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "expense_id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "expense_description")
    private String description;
    @Column(name = "price")
    private double price;
    @Column(name = "value_in_pln")
    private double valueInPln; //bylo value_in_pln
    @Transient
    @Pattern(regexp = "^[0-9]+(\\.[0-9]{1,2})?$", message = "*Proszę poprawnie wprowadzić koszt")
    String paymentTemp;
    @Transient
    private String currencyNameTemp;
    @Transient
    private String categoryNameTemp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delegation_id")
    private Delegation delegation;
    /*
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "currency_id")
    private Currency currency;
    */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "expense", cascade=CascadeType.ALL)
    private Set<Photo> photos;

    /*
    PhotoRepository photoRepository; //potrzebne do usuwania
    @PreRemove
    private void removePhotosFromExpense() {
        if (photos.isEmpty()) {
            for (Photo photo : photos) {
                photo.getExpense().remove(this);
            }
        }
    }

     */
}
