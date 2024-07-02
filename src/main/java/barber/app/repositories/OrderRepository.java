package barber.app.repositories;

import barber.app.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE o.barber.id = ?1 ORDER BY o.time DESC")
    Collection<Order> getBarberOrders(Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.status = \"CANCELED\" WHERE o.id = ?1")
    void canceledOrder(Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.status = \"DONE\" WHERE o.id = ?1")
    void doneOrder( Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.status = \"CHANGED\" WHERE o.id = ?1")
    void changedOrder(Integer id);

    @Query("SELECT new map(o.id as id, c.id as customerId, c.name as customerName, c.phone as customerPhone, " +
            "o.status as status, o.time as time, o.grade as grade, b.id as barberId, b.name as barberName, b.phone as barberPhone," +
            " o.service.id as serviceId, o.service.name as serviceName, o.service.price as servicePrice) " +
            "FROM Order o " +
            "INNER JOIN o.customer c " +
            "INNER JOIN o.barber b " +
            "WHERE c.id = ?1 ORDER BY o.time DESC LIMIT 10")
    Collection<Map<String, Object>> getOrdersByCustomer(Integer customerId);

    @Query("SELECT new map(o.id as id, b.id as barberId, b.name as barberName, b.phone as barberPhone, " +
            "o.status as status, o.time as time, o.grade as grade, c.id as customerId, c.name as customerName, c.phone as customerPhone, " +
            "o.service.id as serviceId, o.service.name as serviceName, o.service.price as servicePrice) " +
            "FROM Order o " +
            "INNER JOIN o.barber b " +
            "INNER JOIN o.customer c " +
            "WHERE b.id = ?1 ORDER BY o.time DESC LIMIT 10")
    Collection<Map<String, Object>> getAllBarberOrder(Integer barberId);

    @Query("SELECT new map(o.id as id, b.id as barberId, b.name as barberName, b.phone as barberPhone, " +
            "o.status as status, o.time as time, o.grade as grade, c.id as customerId, c.name as customerName, c.phone as customerPhone, " +
            "o.service.id as serviceId, o.service.name as serviceName, o.service.price as servicePrice) " +
            "FROM Order o " +
            "INNER JOIN o.barber b " +
            "INNER JOIN o.customer c " +
            "WHERE b.id = ?1 AND o.status = 'RESERVED' ORDER BY o.time DESC LIMIT 10")
    Collection<Map<String, Object>> getBarberReservedOrder(Integer barberId);

    @Query("SELECT new map(o.id as id, c.id as customerId, c.name as customerName, c.phone as customerPhone, " +
            "o.status as status, o.time as time, o.grade as grade, b.id as barberId, b.name as barberName, b.phone as barberPhone," +
            " o.service.id as serviceId, o.service.name as serviceName, o.service.price as servicePrice) " +
            "FROM Order o " +
            "INNER JOIN o.customer c " +
            "INNER JOIN o.barber b " +
            "WHERE c.id = ?1 AND o.status = 'RESERVED' ORDER BY o.time DESC")
    Collection<Map<String, Object>> getCustomerReservedOrder(Integer customerId);

}
