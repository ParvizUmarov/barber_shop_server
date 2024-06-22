package barber.app.services;

import barber.app.dto.OrderDto;
import barber.app.dto.SalonDto;
import barber.app.entity.Barber;
import barber.app.entity.Customer;
import barber.app.entity.Order;
import barber.app.repositories.BarberRepository;
import barber.app.repositories.CustomerRepository;
import barber.app.repositories.OrderRepository;
import barber.app.repositories.RedisRepository;
import barber.app.restExceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService implements CRUDService<OrderDto>{

    private final OrderRepository repository;
    private final RedisRepository redisRepository;
    private final BarberRepository barberRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Collection<OrderDto> getAll() {
        log.info("get all orders");
        return repository.findAll()
                .stream()
                .map(OrderService::mapToDto)
                .toList();
    }

    @Override
    public void create(OrderDto object) {
        repository.save(mapToEntity(object));
    }

    @Override
    public void update(OrderDto object) {

    }

    @Override
    public void delete(Integer id) {
       repository.deleteById(id);
    }

    @Override
    public OrderDto get(Integer id) {
        Order order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return mapToDto(order);
    }

    @Override
    public String checkToken(String mail, String token) {
        return redisRepository.checkUserToken(mail, token);
    }

    public static Order mapToEntity(OrderDto orderDto){
        Order order = new Order();
        Barber barber = new Barber();
        Customer customer = new Customer();
        customer.setId(orderDto.getCustomerId());
        barber.setId(orderDto.getId());
        order.setId(orderDto.getId());
        order.setCustomer(customer);
        order.setBarber(barber);
        order.setStatus(orderDto.getStatus());
        order.setTime(orderDto.getTime());
        order.setGrade(orderDto.getGrade());
        return order;
    }

    public static OrderDto mapToDto(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setStatus(order.getStatus());
        orderDto.setBarberId(order.getBarber().getId());
        orderDto.setCustomerId(order.getCustomer().getId());
        orderDto.setGrade(order.getGrade());
        orderDto.setTime(order.getTime());
        return orderDto;
    }


}
