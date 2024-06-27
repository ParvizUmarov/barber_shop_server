package barber.app.repositories;

import barber.app.entity.Stores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Map;

public interface StoresRepository extends JpaRepository<Stores, Integer> {

}
