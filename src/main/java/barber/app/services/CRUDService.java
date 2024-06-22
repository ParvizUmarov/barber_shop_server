package barber.app.services;
import java.util.Collection;

public interface CRUDService<T> {
    Collection<T> getAll();
    void create(T object);
    void update(T object);
    void delete(Integer id);
    T get(Integer id);
    String checkToken(String mail,String token);
}
