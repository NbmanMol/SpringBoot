package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.utill.UserErrorResponse;
import ru.kata.spring.boot_security.demo.utill.UserNotCreatedExeption;
import ru.kata.spring.boot_security.demo.utill.UserValidator;

import javax.validation.Valid;
import java.util.List;


@org.springframework.web.bind.annotation.RestController //@Controller + @ResponseBody над каждым методом
@RequestMapping("/rest")
public class RestController {
    private final UserService userService;
    private final RegistrationService registrationService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    @Autowired
    public RestController(UserService userService, RegistrationService registrationService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping("/users")
    public List<User> getUserList() {
        return userService.getUserList();
    }


    @PostMapping("/new")
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
            }
            throw new UserNotCreatedExeption(errorMsg.toString());
        }
        registrationService.register(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleExeption(UserNotCreatedExeption e) {
        UserErrorResponse response = new UserErrorResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/update/{id}")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
            }
            throw new UserNotCreatedExeption(errorMsg.toString());
        }
        userService.updateUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Integer userId) {
        userService.deleteUser(userService.getUserForId(userId));
        return ResponseEntity.ok(HttpStatus.OK);
    }

}