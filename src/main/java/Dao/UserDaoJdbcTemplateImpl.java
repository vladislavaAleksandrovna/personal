package Dao;

import Model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserDaoJdbcTemplateImpl implements UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDaoJdbcTemplateImpl(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final String SQL_SELECT_ALL = "SELECT * FROM user_db";
    private final String SQL_INSERT_USER = "INSERT INTO user_db (login,password,email,role) VALUES (?,?,?,?)";
    private final String SQL_UPDATE_COLOR = "UPDATE user_db set color = ? where login = ?";
    private final String SQL_SELECT_COLOR = "SELECT color, role from user_db where login = ?";
    private final String SQL_DELETE_USER = "DELETE FROM user_db where id = ?";


    Map<Integer, User> userMap = new HashMap<>();

    @Override
    public Optional<Integer> findById(Integer id) {
        Integer idUser;
        List<User> user = (List<User>) jdbcTemplate.query(SQL_SELECT_ALL,userRowMapper);
        for (User u:user){
            idUser = u.getId();
            if(idUser.equals(id)){
                return Optional.of(id);
            }
        }
        return Optional.empty();

    }

    @Override
    public String getRoleUser(String login) {
        String role = null;
        for (User u : jdbcTemplate.query(SQL_SELECT_COLOR, loginUserRowMapper, login)) {
            role = u.getRole();
        }
        return role;
    }

    private RowMapper<User> userRowMapper = (resultSet, i) -> {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("login"),
                resultSet.getString("password"),
                resultSet.getString("email"));
    };

    private RowMapper<User> loginUserRowMapper = (resultSet, i) -> {
        return new User(
                resultSet.getString("color"),
                resultSet.getString("role"));
    };

    @Override
    public boolean isExists(String login) {
        for (User u : jdbcTemplate.query(SQL_SELECT_ALL, userRowMapper)) {
            if (u.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void save(String login, String password, String email, String role) {
        jdbcTemplate.update(SQL_INSERT_USER, login, password, email, role);
    }


    @Override
    public void update(String login, String color) {

        jdbcTemplate.update(SQL_UPDATE_COLOR, color, login);
    }


    @Override
    public boolean isExists(String login, String password) {
        for (User user : jdbcTemplate.query(SQL_SELECT_ALL, userRowMapper)) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getColorUser(String login) {
        String color = null;
        for (User u : jdbcTemplate.query(SQL_SELECT_COLOR, loginUserRowMapper, login)) {
            color = u.getColor();
        }
        return color;
    }

    @Override
    public List<User> findAll() {

        return jdbcTemplate.query(SQL_SELECT_ALL, userRowMapper);
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update(SQL_DELETE_USER,id);
    }
}