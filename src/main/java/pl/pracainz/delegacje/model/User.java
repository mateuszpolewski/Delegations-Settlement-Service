package pl.pracainz.delegacje.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;
    @Column(name = "name")
    @Pattern(regexp = "^[A-Z]{1}[a-z]{2,15}( [A-Z]{1}[a-z]{3,15})?$", message = "*Proszę poprawnie wpisać imię")
    private String name;
    @Column(name = "last_name")
    @Pattern(regexp = "^[A-Z]{1}[a-z]{2,15}(-[A-Z]{1}[a-z]{3,15})?$", message = "*Proszę poprawnie wpisać nazwisko")
    private String lastName;
    @Column(name = "email")
    @Email(message = "*Proszę podać właściwy email")
    private String email;
    @Column(name = "phone_number")
    @Pattern(regexp = "^(\\+{2}[0-9])?[0-9]{9}$", message = "*Proszę poprawnie numer telefonu")
    private String phoneNumber;
    @Column(name = "position")
    private String position;
    @Column(name = "comment")
    private String comment;
    @Column(name = "active")
    private Boolean active;
    @Transient
    private String chosenRole;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(fetch = FetchType.EAGER,  cascade = CascadeType.MERGE, mappedBy = "user")
    private Set<Delegation> delegations;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "password_id")
    private Password password;
}
