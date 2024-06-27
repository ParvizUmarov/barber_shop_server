package barber.app.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class StoresDto {
    private Integer id;
    private Integer barberId;
    private String image;
    private Timestamp time;
}
