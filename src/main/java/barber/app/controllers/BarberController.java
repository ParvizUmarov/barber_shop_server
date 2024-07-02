package barber.app.controllers;

import barber.app.dto.BarberDto;
import barber.app.dto.TokenDto;
import barber.app.services.BarberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/barber")
@RequiredArgsConstructor
public class BarberController {

    private final BarberService barberService;
    private final String HEADER_KEY = "authorization";

    @GetMapping
    public Collection<BarberDto> getAll(){
        log.info("getAll barbers");
        return barberService.getAll();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody BarberDto barberDto){
    try{
        log.info("create barber ");
        var barber = barberService.register(barberDto);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setMail(barber.getMail());
        tokenDto.setToken(barber.getToken());
        tokenDto.setUid(barber.getId());
        return ResponseEntity.ok(tokenDto);
    }catch (Exception e){
        log.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", e.getMessage()));
     }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, @RequestHeader Map<String, String> headers){
        var token = headers.get(HEADER_KEY);
        log.info("delete barber by id:", id);
        barberService.delete(id, token);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Integer id, @RequestHeader Map<String, String> headers){
        log.info("get barber by id:" + id);
        try{
            var token = headers.get(HEADER_KEY);
            BarberDto barberDto = barberService.get(id, token);
            return ResponseEntity.ok(barberDto);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }

    @GetMapping("/logout")
    public ResponseEntity logout(@RequestHeader Map<String, String> headers){
        try{
            var token = headers.get(HEADER_KEY);
            barberService.logout(token);
            return ResponseEntity.ok("Barber <"+token+"> is logout");
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Barber not found");
        }
    }

    @GetMapping("/login/{mail}/{password}")
    public ResponseEntity login(@PathVariable String mail, @PathVariable String password){
        log.info("login barber: " + mail);
        try{
            BarberDto login = barberService.login(mail, password);
            TokenDto tokenDto = new TokenDto();
            tokenDto.setMail(login.getMail());
            tokenDto.setToken(login.getToken());
            tokenDto.setUid(login.getId());
            return ResponseEntity.ok(tokenDto);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/allInfo")
    public ResponseEntity getAllBarberInfo(@RequestHeader Map<String, String> headers){
        try{
            var token = headers.get(HEADER_KEY);
            var response = barberService.getAllBarberInfo(token);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }



    @GetMapping("/profile")
    public ResponseEntity getBarberInfo(@RequestHeader Map<String, String> headers){
        try{
            var token = headers.get(HEADER_KEY);
            log.info("find barber by token:" + token);
            BarberDto barberDto = barberService.getBarberInfo(token);
            return ResponseEntity.ok(barberDto);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestHeader Map<String, String> headers, @RequestBody BarberDto barberDto){
        try{
            var token = headers.get(HEADER_KEY);
            barberService.checkToken(token);
            log.info("update barber by id: " + barberDto.getId());
            barberDto.setId(barberDto.getId());
            barberService.update(barberDto, token);
            return ResponseEntity.ok("Barber <"+barberDto.getMail()+"> updated");
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }
}
