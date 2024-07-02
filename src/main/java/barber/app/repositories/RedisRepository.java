package barber.app.repositories;

import barber.app.restExceptionHandler.ResourceNotFoundException;
import barber.app.session.SessionUser;
import barber.app.session.TokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Repository
public class RedisRepository {
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate template = new RedisTemplate();
    public static String TOKEN = "";
    private TokenHolder tokenHolder = new TokenHolder();

    public String getAllKeys() {
        return template.opsForHash().values(TOKEN).toString();
    }

    public String login(String mail, int id){
        tokenHolder.create();
        template.opsForHash().put(tokenHolder.getToken(), mail, String.valueOf(id));
        return tokenHolder.getToken();
    }

    public boolean logout(String token){
        return template.delete(token);
    }

    public SessionUser checkUserToken(String token) {
        var user = template.opsForHash().entries(token);

        if(user.isEmpty()){
            throw new ResourceNotFoundException("{error: You must authorized}");
        }

        var mail = user.keySet().toArray()[0].toString();
        var id = Integer.parseInt(user.values().toArray()[0].toString());

        var sessionUser = new SessionUser(id, mail, token);

        return sessionUser;
    }
}
