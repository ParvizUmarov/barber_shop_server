package barber.app.controllers;

import barber.app.dto.OrderDto;
import barber.app.dto.ServiceDto;
import barber.app.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public Collection<OrderDto> getAll(){
        log.info("getAll orders");
        return orderService.getAll();
    }




}
