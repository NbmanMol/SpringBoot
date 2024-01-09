package ru.kata.spring.boot_security.demo.dao;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findByUsername(String username);

    void createUser(User user);

    List<User> getUsersList();

    void deleteUser(User user);

    User getUserForId(int id);

    void updateUser(User user);

    User findByUserName(String name);
}