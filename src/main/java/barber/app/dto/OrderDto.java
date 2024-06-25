package barber.app.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderDto {
    private Integer id;
    private Integer customerId;
    private Integer barberId;
    private String status;
    private Timestamp time;
    private int grade;
    private Integer serviceId;
}
