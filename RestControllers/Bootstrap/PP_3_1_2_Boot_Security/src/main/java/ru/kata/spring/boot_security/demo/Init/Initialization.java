package ru.kata.spring.boot_security.demo.Init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Initialization {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public Initialization(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void addUsers() {
        Role roleUser = new Role("ROLE_USER");
        Role roleAdmin = new Role("ROLE_ADMIN");
        roleService.save(roleUser);
        roleService.save(roleAdmin);

        Set<Role> adminRoleSet = Stream.of(roleAdmin).collect(Collectors.toSet());
        Set<Role> userRoleSet = Stream.of(roleUser).collect(Collectors.toSet());
        User user1 = new User("asd", "asd", "asd@asd", 4,
                "asd", adminRoleSet);
        User user2 = new User("фыв", "фыв", "фыв@фыв", 5,
                "user", userRoleSet);
        userService.createUser(user1);
        userService.createUser(user2);
    }
}