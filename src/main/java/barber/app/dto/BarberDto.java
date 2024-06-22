package barber.app.dto;

import lombok.Data;

@Data
public class BarberDto {
    private Integer id;
    private String name;
    private String surname;
    private String birthday;
    private String phone;
    private String mail;
    private String password;
    private Boolean authState;
    private Integer workExperience;
    private String token;
}
