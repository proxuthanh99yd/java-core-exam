package ra.business.serviceInterface;

import java.util.List;
import java.util.stream.Stream;

public interface IGeneric <T,E>{
    List<T> findAll();
    T findById(E id);
    boolean save(T t);
    void changeStatusById(E id);
    E autoIncrementId(List<T> t);
    Stream<T> searchByName(String name);
    boolean existName(String name);
}
