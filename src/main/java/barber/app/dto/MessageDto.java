package barber.app.dto;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class MessageDto {
    private Integer id;
    private Integer chatId;
    private String message;
    private Timestamp time;
}
