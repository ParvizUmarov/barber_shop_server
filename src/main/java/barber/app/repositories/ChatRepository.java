package barber.app.repositories;

import barber.app.entity.Barber;
import barber.app.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    @Query("SELECT c FROM Chat c WHERE c.barber.id = ?1")
    Collection<Chat> findChatByBarberId(Integer barberId);

    @Query("SELECT c FROM Chat c WHERE c.customer.id = ?1")
    Collection<Chat> findChatByCustomerId(Integer customerId);
}
