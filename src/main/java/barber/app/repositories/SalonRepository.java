package barber.app.repositories;

import barber.app.dto.SalonDto;
import barber.app.entity.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface SalonRepository extends JpaRepository<Salon, Integer> {
    @Query("SELECT s FROM Salon s WHERE s.address != '' ORDER BY id ASC LIMIT 10")
    Collection<Salon> getAllSalons();

}
