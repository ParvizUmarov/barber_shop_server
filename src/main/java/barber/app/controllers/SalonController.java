package barber.app.controllers;


import barber.app.dto.SalonDto;
import barber.app.dto.ServiceDto;
import barber.app.services.SalonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/salon")
@RequiredArgsConstructor
public class SalonController {

    private final SalonService salonService;

    @GetMapping()
    public Collection<SalonDto> getAll(){
        log.info("getAll salons");
        return salonService.getAllSalons();
    }



}
