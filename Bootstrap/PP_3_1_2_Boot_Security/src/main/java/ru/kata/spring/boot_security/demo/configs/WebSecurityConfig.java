package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserServiceImp userServiceImp;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserServiceImp userServiceImp) {
        this.successUserHandler = successUserHandler;
        this.userServiceImp = userServiceImp;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(getPasswordEncoder());
        authenticationProvider.setUserDetailsService(userServiceImp);
        return authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/auth/login", "/auth/registration", "/error").permitAll() //все могут сюда попасть
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and() //и
                .formLogin()  //форма логина
                .loginPage("/auth/login")  //страница логина базовая
                .loginProcessingUrl("/process_login") // откуда получит инфу(postMappping типо)
                .successHandler(successUserHandler)//  в случае успешного входа разбираться будет этот класс
                .permitAll()
                .failureUrl("/auth/login?error") // в случае неуспешной попадёт сюда(в таймлифе прописываем еррор)
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login");
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//        try {
//            auth.userDetailsService(userServiceImp).passwordEncoder(getPasswordEncoder());
//        } catch (Exception e) {
//            throw new RuntimeException("Не удалось настроить аутентификацию!");
//        }
//    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}