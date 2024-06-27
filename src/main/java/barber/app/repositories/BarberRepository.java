package barber.app.repositories;

import barber.app.entity.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Map;

public interface BarberRepository extends JpaRepository<Barber, Integer> {
    @Query("SELECT b FROM Barber b WHERE b.mail = ?1")
    Barber findByMail(String mail);

    @Query("SELECT b FROM Barber b WHERE b.mail = ?1 and b.password = ?2")
    Barber login(String mail, String password);

    @Query("SELECT new map(b.id as barberId, b.name as barberName, b.surname as barberSurname, b.phone as barberPhone, b.mail as barberMail," +
            "b.workExperience as workExperience, sal.id as salonID, sal.address as salonAddress, sal.images as salonImages, " +
            "ser.id as serviceId, ser.name as serviceName, ser.price as servicePrice) " +
            "FROM Barber b " +
            "INNER JOIN b.service ser " +
            "INNER JOIN b.salon sal " +
            "ORDER BY b.id ASC LIMIT 10 ")
    Collection<Map<String, Object>> getAllBarbersInfo();



}
