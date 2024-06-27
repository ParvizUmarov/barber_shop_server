package barber.app.services;

import barber.app.dto.OrderDto;
import barber.app.dto.OrderInfoDto;
import barber.app.entity.Barber;
import barber.app.entity.Customer;
import barber.app.entity.Order;
import barber.app.entity.Services;
import barber.app.repositories.BarberRepository;
import barber.app.repositories.CustomerRepository;
import barber.app.repositories.OrderRepository;
import barber.app.repositories.RedisRepository;
import barber.app.restExceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService implements CRUDService<OrderDto>{

    private final OrderRepository ordersRepository;
    private final RedisRepository redisRepository;
    private final BarberRepository barberRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Collection<OrderDto> getAll() {
        log.info("get all orders");
        return ordersRepository.findAll()
                .stream()
                .map(OrderService::mapToDto)
                .toList();
    }

    @Override
    public void create(OrderDto object, String token) {
        ordersRepository.save(mapToEntity(object));
    }

    public void saveOrder(OrderDto object, String token, String userType){
        if(userType.equals("CUSTOMER")){
           var customer = customerRepository.findById(object.getCustomerId());
           checkToken(customer.get().getMail(), token);
        }else{
            var customer = barberRepository.findById(object.getBarberId());
            checkToken(customer.get().getMail(), token);
        }
        ordersRepository.save(mapToEntity(object));
    }

    @Override
    public void update(OrderDto object, String token) {

    }

    public Collection<OrderInfoDto> getCustomerReservedOrder(Integer id, String token) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        checkToken(customer.getMail(), token);
        return orderInfoToDto(ordersRepository.getCustomerReservedOrder(id));
    }

    public void cancelOrder(Integer id, String token, String userType){
        checkToken("", token);
        ordersRepository.canceledOrder(id);
        log.info("canceled order by id: " + id);
    }

    public void doneOrder(Integer id, String token){
        ordersRepository.doneOrder(id);
        log.info("order <"+id+"> DONE");

    }

    @Override
    public void delete(Integer id, String token) {
       ordersRepository.deleteById(id);
    }

    @Override
    public OrderDto get(Integer id, String token) {
        Order order = ordersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return mapToDto(order);
    }

    public Collection<OrderInfoDto> getBarbersOrder(Integer id, String token) {
        Barber barber = barberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Barber not found"));
        checkToken(barber.getMail(), token);
        return orderInfoToDto(ordersRepository.getOrdersByBarber(id));
    }

    public Collection<OrderInfoDto> getCustomersOrder(Integer id, String token) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        checkToken(customer.getMail(), token);
        return orderInfoToDto(ordersRepository.getOrdersByCustomer(id));
    }

    @Override
    public String checkToken(String mail, String token) {
        return redisRepository.checkUserToken(mail, token);
    }

    public static Order mapToEntity(OrderDto orderDto){
        Order order = new Order();
        Barber barber = new Barber();
        Customer customer = new Customer();
        Services services = new Services();
        services.setId(orderDto.getServiceId());
        customer.setId(orderDto.getCustomerId());
        barber.setId(orderDto.getBarberId());
        order.setId(orderDto.getId());
        order.setCustomer(customer);
        order.setBarber(barber);
        order.setStatus(orderDto.getStatus());
        order.setTime(orderDto.getTime());
        order.setGrade(orderDto.getGrade());
        order.setService(services);
        return order;
    }

    public static OrderDto mapToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setStatus(order.getStatus());
        orderDto.setBarberId(order.getBarber().getId());
        orderDto.setCustomerId(order.getCustomer().getId());
        orderDto.setGrade(order.getGrade());
        orderDto.setTime(order.getTime());
        orderDto.setServiceId(order.getService().getId());
        return orderDto;
    }

    public static Collection<OrderInfoDto> orderInfoToDto(Collection<Map<String, Object>> orders) {
        return orders.stream()
                .map(order -> {
                    OrderInfoDto orderInfo = new OrderInfoDto();
                    orderInfo.setId(Integer.parseInt(order.get("id").toString()));
                    orderInfo.setBarberId(Integer.parseInt(order.get("barberId").toString()));
                    orderInfo.setBarberName(order.get("barberName").toString());
                    orderInfo.setBarberPhone(order.get("barberPhone").toString());
                    orderInfo.setCustomerId(Integer.parseInt(order.get("customerId").toString()));
                    orderInfo.setCustomerName(order.get("customerName").toString());
                    orderInfo.setCustomerPhone(order.get("customerPhone").toString());
                    orderInfo.setTime((Timestamp) order.get("time"));
                    orderInfo.setGrade(Integer.parseInt(order.get("grade").toString()));
                    orderInfo.setStatus(order.get("status").toString());
                    orderInfo.setServiceId(Integer.parseInt(order.get("serviceId").toString()));
                    orderInfo.setServiceName(order.get("serviceName").toString());
                    orderInfo.setServicePrice(Integer.parseInt(order.get("servicePrice").toString()));
                    return orderInfo;
                })
                .collect(Collectors.toList());
    }


}
