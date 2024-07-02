package barber.app.services;
import barber.app.dto.SalonDto;
import barber.app.dto.ServiceDto;
import barber.app.entity.Salon;
import barber.app.entity.Services;
import barber.app.repositories.RedisRepository;
import barber.app.repositories.ServiceRepository;
import barber.app.restExceptionHandler.ResourceNotFoundException;
import barber.app.session.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceService implements CRUDService<ServiceDto>{

    private final ServiceRepository serviceRepository;
    private final RedisRepository redisRepository;

    @Override
    public Collection<ServiceDto> getAll() {
        return serviceRepository.findAll()
                .stream()
                .map(ServiceService::mapToDto)
                .toList();
    }

    @Override
    public void create(ServiceDto object, String token) {
       serviceRepository.save(mapToEntity(object));
    }

    @Override
    public void update(ServiceDto object, String token) {

    }

    @Override
    public void delete(Integer id, String token) {
        serviceRepository.deleteById(id);
    }

    @Override
    public ServiceDto get(Integer id, String token) {
        Services services = serviceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        return mapToDto(services);
    }

    @Override
    public SessionUser checkToken(String token) {
        return redisRepository.checkUserToken(token);
    }


    public static Services mapToEntity(ServiceDto serviceDto){
        Services services = new Services();
        services.setId(serviceDto.getId());
        services.setName(serviceDto.getName());
        services.setPrice(serviceDto.getPrice());
        return services;
    }

    public static ServiceDto mapToDto(Services services){
        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setId(services.getId());
        serviceDto.setName(services.getName());
        serviceDto.setPrice(services.getPrice());
        return serviceDto;
    }
}
