package barber.app.services;

import barber.app.dto.BarberDto;
import barber.app.entity.Barber;
import barber.app.entity.LoginUser;
import barber.app.repositories.BarberRepository;
import barber.app.repositories.RedisRepository;
import barber.app.restExceptionHandler.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarberService implements CRUDService<BarberDto>{

    private final BarberRepository repository;
    private final RedisRepository redisRepository;

    private static String TOKEN = null;

    @Override
    public Collection<BarberDto> getAll() {
        return repository.findAll()
                .stream()
                .map(BarberService::mapToDto)
                .toList();
    }

    @Override
    public void create(BarberDto barberDto) {
        repository.save(mapToEntity(barberDto));
    }

    public BarberDto login(LoginUser user){
        Barber barber = repository.login(user.getMail(), user.getPassword());
        if(barber == null){
            throw new ResourceNotFoundException("Barber not found. Try to register or try again!");
        }
        var token = redisRepository.login(barber.getMail());
        log.info("barber <"+barber.getMail()+"> successfully authorized");
        return mapToDtoForLogin(barber, token);
    }

    public void logout(String mail){
        redisRepository.logout(mail);
    }

    @Override
    public void update(BarberDto barberDto) {
        Barber barber = repository.findById(barberDto.getId()).orElseThrow(() -> new RuntimeException("Barber not found"));
        System.out.println("barber: " + barber);
        if(barberDto.getName() != null){barber.setName(barberDto.getName());}
        if(barberDto.getSurname() != null){barber.setSurname(barberDto.getSurname());}
        if(barberDto.getMail() != null){barber.setMail(barberDto.getMail());}
        if(barberDto.getBirthday() != null){barber.setBirthday(barberDto.getBirthday());}
        if(barberDto.getPhone() != null){barber.setPhone(barberDto.getPhone());}
        if(barberDto.getPassword() != null){barber.setPassword(barberDto.getPassword());}
        if(barberDto.getWorkExperience() != null){barber.setWorkExperience(barberDto.getWorkExperience());}
        if(barberDto.getAuthState() != null){barber.setAuthState(barberDto.getAuthState());}
        repository.save(barber);
    }

    @Override
    public void delete(Integer id) {
       repository.deleteById(id);
    }

    public BarberDto getBarberInfo(String mail, String token){
        String userTokenByMail = checkToken(mail, token);
        Barber barber = repository.findByMail(mail);
        if (barber == null) {
            throw new RuntimeException("Barber not found");
        }
        return mapToDtoForLogin(barber, userTokenByMail);
    }

    @Override
    public BarberDto get(Integer id) {
        Barber barber = repository.findById(id).orElseThrow(() -> new RuntimeException("Barber not found"));
        String userTokenByMail = redisRepository.checkUserToken(barber.getMail(), "");
        log.info(userTokenByMail);
        return mapToDtoForLogin(barber, userTokenByMail);
    }

    @Override
    public String checkToken(String mail, String token) {
        return redisRepository.checkUserToken(mail, token);
    }

    public static Barber mapToEntity(BarberDto barberDto){
        Barber barber = new Barber();
        barber.setId(barberDto.getId());
        barber.setName(barberDto.getName());
        barber.setSurname(barberDto.getSurname());
        barber.setBirthday(barberDto.getBirthday());
        barber.setPhone(barberDto.getPhone());
        barber.setMail(barberDto.getMail());
        barber.setWorkExperience(barberDto.getWorkExperience());
        barber.setPassword(barberDto.getPassword());
        barber.setAuthState(barberDto.getAuthState());
        return barber;
    }

    public static BarberDto mapToDto(Barber barber){
        BarberDto barberDto = new BarberDto();
        barberDto.setId(barber.getId());
        barberDto.setName(barber.getName());
        barberDto.setSurname(barber.getSurname());
        barberDto.setBirthday(barber.getBirthday());
        barberDto.setPhone(barber.getPhone());
        barberDto.setMail(barber.getMail());
        barberDto.setWorkExperience(barber.getWorkExperience());
        barberDto.setPassword(barber.getPassword());
        barberDto.setAuthState(barber.getAuthState());
        return barberDto;
    }

    public static BarberDto mapToDtoForLogin(Barber barber, String token){
        BarberDto barberDto = new BarberDto();
        barberDto.setId(barber.getId());
        barberDto.setName(barber.getName());
        barberDto.setSurname(barber.getSurname());
        barberDto.setBirthday(barber.getBirthday());
        barberDto.setPhone(barber.getPhone());
        barberDto.setMail(barber.getMail());
        barberDto.setWorkExperience(barber.getWorkExperience());
        barberDto.setPassword(barber.getPassword());
        barberDto.setAuthState(barber.getAuthState());
        barberDto.setToken(token);
        return barberDto;
    }



}
