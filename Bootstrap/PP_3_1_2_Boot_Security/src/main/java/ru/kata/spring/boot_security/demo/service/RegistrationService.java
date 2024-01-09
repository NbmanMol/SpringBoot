package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

@Service
public class RegistrationService {
    private final UserService userService;
    private final RoleDao roleDao;


    @Autowired
    public RegistrationService(UserService userService, RoleDao roleDao) {
        this.userService = userService;
        this.roleDao = roleDao;
    }

    @Transactional
    public void register(User user) {
        if (user.getRoleSet().isEmpty()) {
            user.setRole(roleDao.findByRolename("ROLE_USER"));
        }
        userService.createUser(user);
    }
}
