package barber.app.controllers;

import barber.app.dto.CustomerDto;
import barber.app.dto.TokenDto;
import barber.app.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
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

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody CustomerDto customerDto){
        try{
            log.info("register customer " + customerDto.getMail());
            var customerInfo = customerService.register(customerDto);
            TokenDto tokenDto = new TokenDto();
            tokenDto.setMail(customerInfo.getMail());
            tokenDto.setToken(customerInfo.getToken());
            tokenDto.setUid(customerInfo.getId());
            return ResponseEntity.ok(tokenDto);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }

    }

    @GetMapping("/login/{mail}/{password}")
    public ResponseEntity login(@PathVariable String mail, @PathVariable String password){
        try{
            var customerDto =  customerService.login(mail, password);
            TokenDto tokenDto = new TokenDto();
            tokenDto.setMail(customerDto.getMail());
            tokenDto.setToken(customerDto.getToken());
            tokenDto.setUid(customerDto.getId());
            log.info("login customer: " + mail);
            return ResponseEntity.ok(tokenDto);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity logout(@RequestHeader Map<String, String> headers){
        try{
            var token = headers.get(HEADER_KEY);
            customerService.logout(token);
            return ResponseEntity.ok("Customer <"+token+"> is logout");
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, @RequestHeader Map<String, String> headers){
        var token = headers.get(HEADER_KEY);
        log.info("delete customer by id:", id);
        customerService.delete(id, token);
    }

    @GetMapping("/profile")
    public ResponseEntity getCustomerInfo(@RequestHeader Map<String, String> headers){
        var token = headers.get(HEADER_KEY);
        log.info("customer profile: " + token);
        CustomerDto customerDto = customerService.getCustomerInfo(token);
        return ResponseEntity.ok(customerDto);
    }

    @PatchMapping("/update customer")
    public void update(@RequestBody CustomerDto customerDto, @RequestHeader Map<String, String> headers){
        var token = headers.get(HEADER_KEY);
        log.info("update customer by id:"+ customerDto.getId());
        customerDto.setId(customerDto.getId());
        customerService.update(customerDto, token);

    }
}
