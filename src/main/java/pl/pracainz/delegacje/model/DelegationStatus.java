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
@Table(name = "delegation_status")
public class DelegationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "delegation_status_id")
    private int id;
    @Column(name = "status")
    private String status;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "delegationStatus")
    private Set<Delegation> delegations;

    public String getPolishStatus() {
        if(this.status.equals("WAITING")) return "OCZEKUJĄCA";
        if(this.status.equals("APPROVED")) return "ZATWIERDZONO";
        if(this.status.equals("REJECTED")) return "ODRZUCONO";
        if(this.status.equals("FINISHED")) return "ZAKOŃCZONO";
        if(this.status.equals("SETTLED")) return "ROZLICZONO";
        if(this.status.equals("CANCELLED")) return "ANULOWANO";
        return "BLAD";
    }
}
