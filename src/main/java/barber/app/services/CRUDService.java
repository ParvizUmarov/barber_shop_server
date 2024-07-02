package barber.app.services;
import barber.app.session.SessionUser;

import java.util.Collection;

public interface CRUDService<T> {
    Collection<T> getAll();
    void create(T object, String token);
    void update(T object, String token);
    void delete(Integer id, String token);
    T get(Integer id, String token);
    SessionUser checkToken(String token);
}
