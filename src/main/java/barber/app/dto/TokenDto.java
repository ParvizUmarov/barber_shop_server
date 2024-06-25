package barber.app.dto;

import lombok.Data;

@Data
public class TokenDto {
    private int uid;
    private String mail;
    private String token;
}
