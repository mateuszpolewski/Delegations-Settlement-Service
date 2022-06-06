package pl.pracainz.delegacje.model;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "delegations")
public class Delegation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "delegation_id",unique = true, nullable = false)
    private int id;
    @Column(name = "delegation_title")
    private String title;
    @Column(name = "delegation_description")
    private String description;
    @Column(name = "delegation_start_date")
    private Date startDate;
    @Transient
    private String startDateTemp;
    @Column(name = "delegation_end_date")
    private Date endDate;
    @Transient
    private String endDateTemp;
    @Column(name = "destination_country")
    private String destinationCountry;
    @Column(name = "destination_city")
    private String destinationCity;
    @Column(name = "summary_cost")
    private double summaryCost;
    @Column(name = "advanced_payment")
    private double advancedPayment;
    @Column(name = "real_start_datetime")
    private Date realStartDateTime;
    @Column(name = "real_end_datetime")
    private Date realEndDateTime;
    @Column(name = "accountant_id")
    private int accountantId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date submissionDate;
    @Transient
    private String startHourTemp;
    @Transient
    private String endHourTemp;
    @Transient
    @Pattern(regexp = "^[0-9]+(\\.[0-9]{1,2})?$", message = "*Proszę poprawnie wprowadzić zaliczkę")
    String paymentTemp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delegation_status_id")
    private DelegationStatus delegationStatus;

    @OneToMany(mappedBy="delegation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Expense> expenses;
}
