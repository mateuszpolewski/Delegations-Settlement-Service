package pl.pracainz.delegacje.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DelegationMobile {
    private int id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private String destinationCountry;
    private String destinationCity;
    private double summaryCost;
    private double advancedPayment;
    private int accountantId;
    private Date realStartDateTime;
    private Date realEndDateTime;
    private Date submissionDate;
    private int user_id;
    private int delegation_status_id;
}
