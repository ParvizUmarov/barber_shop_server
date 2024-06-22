package barber.app.repositories;

import barber.app.dto.MessageDto;
import barber.app.entity.Chat;
import barber.app.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE m.chat.id = ?1")
    Collection<Message> getMessagesByChatId(Integer chatID);
}
