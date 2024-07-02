package barber.app.services;

import barber.app.dto.StoresDto;
import barber.app.entity.Barber;
import barber.app.entity.Stores;
import barber.app.repositories.RedisRepository;
import barber.app.repositories.StoresRepository;
import barber.app.session.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoresService implements CRUDService<StoresDto> {

    final RedisRepository redisRepository;
    final StoresRepository storesRepository;

    @Override
    public Collection<StoresDto> getAll() {
        return storesRepository.findAll()
                .stream()
                .map(StoresService::mapToDto)
                .toList();
    }

    @Override
    public void create(StoresDto object, String token) {

    }

    @Override
    public void update(StoresDto object, String token) {

    }

    @Override
    public void delete(Integer id, String token) {

    }

    @Override
    public StoresDto get(Integer id, String token) {
        return null;
    }

    @Override
    public SessionUser checkToken(String token) {
        return redisRepository.checkUserToken(token);
    }

    public static Stores mapToEntity(StoresDto storesDto){
        Stores stores = new Stores();
        Barber barber = new Barber();
        barber.setId(storesDto.getBarberId());
        stores.setId(storesDto.getId());
        stores.setBarber(barber);
        stores.setTime(storesDto.getTime());
        stores.setImage(storesDto.getImage());
        return stores;
    }
    
    public static StoresDto mapToDto(Stores stores){
        StoresDto storesDto = new StoresDto();
        storesDto.setId(stores.getId());
        storesDto.setBarberId(stores.getBarber().getId());
        storesDto.setImage(stores.getImage());
        storesDto.setTime(stores.getTime());
        return storesDto;
    }

}
