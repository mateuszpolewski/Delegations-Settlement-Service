package pl.pracainz.delegacje.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "passwords")
public class Password {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "password_id")
    private int id;
    @Column(name = "temporary")
    private boolean temporary;
    @Column(name = "password_hash")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\\^$€*.+\\[\\]{}()?\\-\\\"!@#%&\\/\\,><':;|_~`])"
            + "[A-Za-z\\d\\^$€*.+\\[\\]{}()?\\-\\\"!@#%&\\/\\,><':;|_~`]{8,}$",
            message = "*Hasło powinno zawierać 8 znaków w tym przynajmniej jedną literę, liczbę i znak specjalny")
    private String passwordHash;
    @Transient
    private String retypePassword;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "password")
    private User user;

    public boolean isTemporary() {
        return temporary;
    }
}
