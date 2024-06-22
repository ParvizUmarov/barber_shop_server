package barber.app.controllers;

import barber.app.dto.BarberDto;
import barber.app.dto.ServiceDto;
import barber.app.services.ServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public Collection<ServiceDto> getAll(){
        log.info("getAll services");
        return serviceService.getAll();
    }



}
