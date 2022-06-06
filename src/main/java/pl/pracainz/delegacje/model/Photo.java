package pl.pracainz.delegacje.model;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private int id;
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @Column(name = "type")
    private String type;
    //image bytes can have large lengths so we specify a value
    //which is more than the default length for picByte column
    @Lob
    @Column(name = "picByte", columnDefinition="LONGBLOB") //1000
    private byte[] picByte;


    public Photo(String name, String type, byte[] picByte) {
        this.name = name;
        this.type = type;
        this.picByte = picByte;
    }

}
