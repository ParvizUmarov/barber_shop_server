package barber.app.repositories;

import barber.app.restExceptionHandler.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Slf4j
@Repository
public class RedisRepository {
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template = new RedisTemplate();
    public static final String HASH_KEY = "token";


    public String getAllKeys() {
        return template.opsForHash().values(HASH_KEY).toString();
    }

    public String login(String mail){
        template.opsForHash().put(HASH_KEY, mail, UUID.randomUUID().toString());
        return (String) template.opsForHash().get(HASH_KEY, mail);
    }

    public boolean logout(String mail){
        template.opsForHash().delete(HASH_KEY, mail);
        return true;
    }

    public String checkUserToken(String mail, String token) {
        String t = (String) template.opsForHash().get(HASH_KEY, mail);
        log.info(t);
        log.info(mail);
        if(t == null || !t.equals(token)){
            throw new ResourceNotFoundException("You must authorized!");
        }
        return t;
    }
}
