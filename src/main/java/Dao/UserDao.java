package Dao;

import Model.User;

public interface UserDao extends CrudDao<User> {
    String getColorUser(String login);
    String getRoleUser(String login);
}

