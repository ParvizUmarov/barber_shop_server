package barber.app.dto;

import barber.app.entity.Barber;
import barber.app.entity.Customer;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ChatDto {
    private Integer id;
    private Integer barberId;
    private Integer customerId;

}
