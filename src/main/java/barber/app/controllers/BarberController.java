package barber.app.controllers;

import barber.app.dto.BarberDto;
import barber.app.dto.TokenDto;
import barber.app.entity.LoginUser;
import barber.app.services.BarberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void register(@RequestBody BarberDto barberDto){
        log.info("create barber ");
        barberService.create(barberDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        log.info("delete barber by id:", id);
        barberService.delete(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Integer id){
        log.info("get barber by id:" + id);
        try{
            BarberDto barberDto = barberService.get(id);
            return ResponseEntity.ok(barberDto);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }

    @GetMapping("/logout/{mail}")
    public ResponseEntity logout(@PathVariable String mail){
        try{
            barberService.logout(mail);
            return ResponseEntity.ok("Barber <"+mail+"> is logout");
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Barber not found");
        }
    }

    @GetMapping("/login")
    public ResponseEntity login(@RequestBody LoginUser user){
        log.info("login barber: " + user);
        try{
            BarberDto login = barberService.login(user);
            TokenDto tokenDto = new TokenDto();
            tokenDto.setMail(login.getMail());
            tokenDto.setToken(login.getToken());
            tokenDto.setUserId(login.getId());
            return ResponseEntity.ok(tokenDto);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }



    @GetMapping("/profile/{mail}")
    public ResponseEntity findByToken(@RequestHeader Map<String, String> headers, @PathVariable String mail){
        try{
            var token = headers.get(HEADER_KEY);
            log.info("find barber by token:" + token);
            BarberDto barberDto = barberService.getBarberInfo(mail, token);
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
            barberService.checkToken(barberDto.getMail(), headers.get(HEADER_KEY));
            log.info("update barber by id: " + barberDto.getId());
            barberDto.setId(barberDto.getId());
            barberService.update(barberDto);
            return ResponseEntity.ok("Barber <"+barberDto.getMail()+"> updated");
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }
}
