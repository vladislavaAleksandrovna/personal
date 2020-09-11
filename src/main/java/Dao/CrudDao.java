package Dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CrudDao<T> {

    Optional<Integer> findById(Integer id);

    void save(String login, String password, String email, String role);

    void delete(Integer id);

    void update(String password, String color);

    boolean isExists(String login, String password);

    boolean isExists(String login);

    List<T> findAll();
}
