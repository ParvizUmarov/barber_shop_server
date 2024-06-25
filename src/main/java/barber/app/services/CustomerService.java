package barber.app.services;

import barber.app.dto.CustomerDto;
import barber.app.entity.Customer;
import barber.app.repositories.CustomerRepository;
import barber.app.repositories.RedisRepository;
import barber.app.restExceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService implements CRUDService<CustomerDto> {

    private final CustomerRepository repository;
    private final RedisRepository redisRepository;

    @Override
    public Collection<CustomerDto> getAll() {
        return repository.findAll()
                .stream()
                .map(CustomerService::mapToDto)
                .toList();
    }

    @Override
    public void create(CustomerDto customerDto, String token) {
        var customer = mapToEntity(customerDto);
        repository.save(customer);
    }

    public CustomerDto register(CustomerDto customerDto) {
        log.info("user mail: " + customerDto.getMail());
        Customer responseFromDb = repository.findByMail(customerDto.getMail());

        if(responseFromDb != null){
            log.info("user exist");
            throw new ResourceNotFoundException("Customer with <"+customerDto.getMail()+"> mail already exist");
        }

        var token = redisRepository.login(customerDto.getMail());
        var customer = mapToEntity(customerDto);
        repository.save(customer);
        return mapToDtoFor2(customer, token);
    }

    @Override
    public void update(CustomerDto object, String token) {
        //repository.save(mapToEntity(object));
    }

    @Override
    public void delete(Integer id, String token) {
        repository.deleteById(id);
    }

    public CustomerDto login(String mail, String password){
        Customer customer = repository.login(mail, password);
        if(customer == null){
            throw new ResourceNotFoundException("Customer not found. Try to register or try again!");
        }
        var token = redisRepository.login(mail);
        log.info("customer <"+customer.getMail()+"> successfully authorized");
        return mapToDtoFor2(customer, token);
    }

    public void logout(String mail){
        System.out.println("redis repository: logout user<"+mail+">");
        redisRepository.logout(mail);
    }


    @Override
    public CustomerDto get(Integer id, String token) {
        Customer customer = repository.getById(id);
        return mapToDto(customer);
    }

    @Override
    public String checkToken(String mail, String token) {
        return redisRepository.checkUserToken(mail, token);
    }

    public CustomerDto getCustomerInfo(String mail, String token) {
        var userToken = checkToken(mail, token);
        Customer customer = repository.findByMail(mail);
        if (customer == null) {
            throw new RuntimeException("Customer not found");
        }
        return mapToDtoFor2(customer, userToken);
    }


    public static Customer mapToEntity(CustomerDto customerDto){
        Customer customer = new Customer();
        customer.setId(customerDto.getId());
        customer.setName(customerDto.getName());
        customer.setSurname(customerDto.getSurname());
        customer.setBirthday(customerDto.getBirthday());
        customer.setPhone(customerDto.getPhone());
        customer.setMail(customerDto.getMail());
        customer.setPassword(customerDto.getPassword());
        customer.setAuthState(customerDto.getAuthState());
        return customer;
    }

    public static CustomerDto mapToDto(Customer customer){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setName(customer.getName());
        customerDto.setSurname(customer.getSurname());
        customerDto.setBirthday(customer.getBirthday());
        customerDto.setPhone(customer.getPhone());
        customerDto.setMail(customer.getMail());
        customerDto.setPassword(customer.getPassword());
        customerDto.setAuthState(customer.getAuthState());
        return customerDto;
    }

    public static CustomerDto mapToDtoFor2(Customer customer, String token){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setName(customer.getName());
        customerDto.setSurname(customer.getSurname());
        customerDto.setBirthday(customer.getBirthday());
        customerDto.setPhone(customer.getPhone());
        customerDto.setMail(customer.getMail());
        customerDto.setPassword(customer.getPassword());
        customerDto.setAuthState(customer.getAuthState());
        customerDto.setToken(token);
        return customerDto;
    }
}
