package barber.app.controllers;

import barber.app.dto.BarberDto;
import barber.app.dto.CustomerDto;
import barber.app.dto.TokenDto;
import barber.app.entity.LoginUser;
import barber.app.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final String HEADER_KEY = "authorization";

    @GetMapping
    public Collection<CustomerDto> getAll(){
        log.info("getAll customers");
        return customerService.getAll();
    }

    @PostMapping(name = "/register")
    public void register(@RequestBody CustomerDto customerDto){
        log.info("register customer ");
        customerService.create(customerDto);
    }

    @GetMapping("/login")
    public ResponseEntity login(@RequestBody LoginUser user){
        try{
            var customerDto =  customerService.login(user.getMail(), user.getPassword());
            TokenDto tokenDto = new TokenDto();
            tokenDto.setMail(customerDto.getMail());
            tokenDto.setToken(customerDto.getToken());
            tokenDto.setUserId(customerDto.getId());
            log.info("login customer: " + user);
            return ResponseEntity.ok(tokenDto);
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
            customerService.logout(mail);
            return ResponseEntity.ok("Customer <"+mail+"> is logout");
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        log.info("delete customer by id:", id);
        customerService.delete(id);
    }

    @GetMapping("/profile/{mail}")
    public ResponseEntity getCustomerInfo(@PathVariable String mail, @RequestHeader Map<String, String> headers){
        var token = headers.get(HEADER_KEY);
        log.info("customer profile: " + mail);
        CustomerDto customerDto = customerService.getCustomerInfo(mail, token);
        return ResponseEntity.ok(customerDto);
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable Integer id, @RequestBody CustomerDto customerDto){
        log.info("update customer by id:", id);
        customerDto.setId(id);
        customerService.update(customerDto);

    }



}
