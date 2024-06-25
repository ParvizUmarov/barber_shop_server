package barber.app.controllers;

import barber.app.dto.OrderDto;
import barber.app.dto.OrderInfoDto;
import barber.app.dto.ServiceDto;
import barber.app.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final String HEADER_KEY = "authorization";

    @GetMapping
    public Collection<OrderDto> getAll(){
        log.info("getAll orders");
        return orderService.getAll();
    }

    @GetMapping("/barber/{id}")
    public ResponseEntity getBarberOrders(@PathVariable Integer id, @RequestHeader Map<String, String> headers){
        try{
            var token = headers.get(HEADER_KEY);
            log.info("get all barber orders by token: " + token);
            return ResponseEntity.ok(orderService.getBarbersOrder(id, token));
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }

    @GetMapping("/customer/{id}")
    public Collection<OrderInfoDto> getCustomerOrders(@PathVariable Integer id){
        log.info("get all customer orders by id: " + id);
        return orderService.getCustomersOrder(id);
    }

    @PostMapping("/customer/createOrder")
    public ResponseEntity createOrder(@RequestBody OrderDto orderDto, @RequestHeader Map<String, String> headers){
        try{
            var token = headers.get(HEADER_KEY);
           orderService.saveOrder(orderDto, token, "CUSTOMER");
           return ResponseEntity.ok("{response: order is create successfully}");
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }

    @PatchMapping("/customer/cancel/{id}")
    public ResponseEntity cancelOrder(@PathVariable Integer id, @RequestHeader Map<String, String> headers){
        try{
            var token = headers.get(HEADER_KEY);
            orderService.cancelOrder(id, token, "CUSTOMER");
            return ResponseEntity.ok("{response: order <"+id+"> is cancel}");
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }

    @PatchMapping("/barber/done/{id}")
    public ResponseEntity doneOrder(@PathVariable Integer id, @RequestHeader Map<String, String> headers){
        try{
            var token = headers.get(HEADER_KEY);
            orderService.doneOrder(id, token);
            return ResponseEntity.ok("{response: order <"+id+"> is done}");
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }



}
