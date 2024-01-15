package ru.kata.spring.boot_security.demo.utill;

public class UserNotCreatedExeption extends RuntimeException {
    public UserNotCreatedExeption(String msg) {
        super(msg);
    }
}
