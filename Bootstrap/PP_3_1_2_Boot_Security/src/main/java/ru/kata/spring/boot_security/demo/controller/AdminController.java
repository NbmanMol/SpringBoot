package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.UserDetails;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.utill.UserValidator;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RegistrationService registrationService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RegistrationService registrationService, RoleService roleService) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public String printUserTable(Model model) {
        model.addAttribute("ListUser", userService.getUserList());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("thisUser", userDetails.getUser());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "admin/users";
    }

//    @GetMapping("/new")
//    public ModelAndView returnCreateUserPage() {
//        ModelAndView modelAndView = new ModelAndView("admin/new");
//        modelAndView.addObject("user", new User());
//        List<Role> roles = roleService.getAllRoles();
//        modelAndView.addObject("allRoles", roles);
//        return modelAndView;
//    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") User user) {
        registrationService.register(user);
        return "redirect:/admin/users";
    }

//    @GetMapping("/update")
//    public ModelAndView returnUpdateUserPage(@RequestParam Integer id) {
//        ModelAndView modelAndView = new ModelAndView("admin/update");
//        modelAndView.addObject("user", userService.getUserForId(id));
//        modelAndView.addObject("allRoles", roleService.getAllRoles());
//        return modelAndView;
//    }

    @PostMapping("/update/{id}")
    public String updateUser(@ModelAttribute("user") User updatedUser) {
        userService.updateUser(updatedUser);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete")
    public String removeUser(@RequestParam Integer id) {
        userService.deleteUser(userService.getUserForId(id));
        return "redirect:/admin/users";
    }

}