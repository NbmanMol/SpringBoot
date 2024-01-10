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

@Component
public class Initialization {
    private final UserService userService;

    @Autowired
    public Initialization(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void addUsers() {
        Role roleUser = new Role("ROLE_USER");
        Role roleAdmin = new Role("ROLE_ADMIN");

        Set<Role> roleSet = new HashSet<>();
        Set<Role> roleSet1 = new HashSet<>();

        roleSet.add(roleAdmin);
        User admin = new User("asd", "фыв", "На@Новый.год", 5, "123", roleSet);
        userService.createUser(admin);

        roleSet1.add(roleUser);
        User user = new User("фыв", "неХочет", "снова@На.завод", 3, "123", roleSet1);
        userService.createUser(user);
    }
}