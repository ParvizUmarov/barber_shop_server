package barber.app.controllers;

import barber.app.dto.StoresDto;
import barber.app.services.StoresService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoresController {

    final StoresService storesService;
    private final String HEADER_KEY = "authorization";

    @GetMapping
    public ResponseEntity getAllStores(@RequestHeader Map<String, Object> headers){
        try{
            var token = headers.get(HEADER_KEY);
            Collection<StoresDto> allStores = storesService.getAll();
            log.info("get all stores");
            return ResponseEntity.ok(allStores);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

}
