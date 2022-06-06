package pl.pracainz.delegacje.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseMobile {
    private int id;
    private double price;
    private double valueInPln;
    private String title;
    private String description;

    private int delegation_id;
    private int category_id;
    private int currency_id;
}
