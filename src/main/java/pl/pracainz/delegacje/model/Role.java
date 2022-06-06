package pl.pracainz.delegacje.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private int id;
    @Column(name = "role_name")
    private String role;

    public String getPolishRole() {
        if(this.role.equals("USER")) return "PRACOWNIK";
        if(this.role.equals("ADMIN")) return "ADMIN";
        if(this.role.equals("ACCOUNTANT")) return "KSIĘGOWOŚĆ";
        if(this.role.equals("OWNER")) return "WŁAŚCICIEL";
        return "BLAD";
    }
}