package barber.app.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderInfoDto {
    private int id;
    private int barberId;
    private String barberName;
    private String barberPhone;
    private int customerId;
    private String customerName;
    private String customerPhone;
    private String status;
    private Timestamp time;
    private int grade;
    private int serviceId;
    private String serviceName;
    private int servicePrice;
}
