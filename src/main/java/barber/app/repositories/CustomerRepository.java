package barber.app.repositories;

import barber.app.entity.Barber;
import barber.app.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("SELECT c FROM Customer c WHERE c.mail = ?1 and c.password = ?2")
    Customer login(String mail, String password);

    @Query("SELECT c FROM Customer c WHERE c.mail = ?1")
    Customer findByMail(String mail);
}
