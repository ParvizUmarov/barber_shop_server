package barber.app.services;

import barber.app.dto.TokenDto;
import barber.app.repositories.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService implements CRUDService<TokenDto>{

    private final RedisRepository redisRepository;

    @Override
    public Collection<TokenDto> getAll() {
        return List.of();
    }

    @Override
    public void create(TokenDto object) {

    }

    @Override
    public void update(TokenDto object) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public TokenDto get(Integer id) {
        return null;
    }

    @Override
    public String checkToken(String mail, String token) {
        return redisRepository.checkUserToken(mail, token);
    }

}
