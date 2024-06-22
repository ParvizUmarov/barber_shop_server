package barber.app.repositories;

import barber.app.entity.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BarberRepository extends JpaRepository<Barber, Integer> {
    @Query("SELECT b FROM Barber b WHERE b.mail = ?1")
    Barber findByMail(String mail);

    @Query("SELECT b FROM Barber b WHERE b.mail = ?1 and b.password = ?2")
    Barber login(String mail, String password);

}
