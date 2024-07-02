package barber.app.services;

import barber.app.dto.SalonDto;
import barber.app.entity.Salon;
import barber.app.repositories.RedisRepository;
import barber.app.repositories.SalonRepository;
import barber.app.restExceptionHandler.ResourceNotFoundException;
import barber.app.session.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalonService implements CRUDService<SalonDto>{

    private final SalonRepository repository;
    private final RedisRepository redisRepository;

    @Override
    public Collection<SalonDto> getAll() {
        return repository.findAll()
                .stream()
                .map(SalonService::mapToDto)
                .toList();
    }

    @Override
    public void create(SalonDto object, String token) {
        repository.save(mapToEntity(object));
    }

    @Override
    public void update(SalonDto object, String token) {

    }

    @Override
    public void delete(Integer id, String token) {
        repository.deleteById(id);
    }

    @Override
    public SalonDto get(Integer id, String token) {
        Salon salon =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Salon not found"));
        return mapToDto(salon);
    }

    @Override
    public SessionUser checkToken(String token) {
        return redisRepository.checkUserToken( token);
    }

    public static Salon mapToEntity(SalonDto salonDto){
        Salon salon = new Salon();
        salon.setId(salonDto.getId());
        salon.setAddress(salonDto.getAddress());
        salon.setImages(salonDto.getImages());
        return salon;
    }

    public static SalonDto mapToDto(Salon salon){
        SalonDto salonDto = new SalonDto();
        salonDto.setId(salon.getId());
        salonDto.setAddress(salon.getAddress());
        salonDto.setImages(salon.getImages());
        return salonDto;
    }

}
