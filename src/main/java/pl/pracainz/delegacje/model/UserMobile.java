package pl.pracainz.delegacje.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMobile {
    private int id;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private boolean temporaryPassword;
    private String position;
    private String comment;
    private String message;
}
