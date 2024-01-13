package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {
    private final UserService userService;
    private final RoleServiceImp roleServiceImp;


    @Autowired
    public RegistrationService(UserService userService, RoleServiceImp roleServiceImp1) {
        this.userService = userService;
        this.roleServiceImp = roleServiceImp1;
    }

    @Transactional
    public void register(User user) {
        if (user.getRoleSet().isEmpty()) {
            user.setRole(roleServiceImp.findByRolename("ROLE_USER"));
        } else {
            Set<Role> set = new HashSet<>();
            for (Role role : user.getRoleSet()){
                set.add(roleServiceImp.findByRolename(role.getRolename()));
            }
            user.setRoleSet(set);
        }

        userService.createUser(user);
    }
}
