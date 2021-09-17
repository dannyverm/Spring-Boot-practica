package com.jpa.app.database;

import org.hibernate.type.TrueFalseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//esta anotacion habilita la seguridad desde los controladores
@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/","/css/**","/style/**","/js/**","/images/**","/listar").permitAll()
        //Cambiaremos todas estas autorizaciones para hacerlas desde el controlador mediante
        // @anotaciones
        // .antMatchers("/ver/**").hasAnyRole("ADMIN","USER")
        // .antMatchers("/uploads/**").hasAnyRole("USER","ADMIN")
        // .antMatchers("/form/**").hasAnyRole("ADMIN")
        .antMatchers("/factura/**").hasAnyRole("ADMIN").anyRequest().authenticated()
        .and()
        .formLogin().loginPage("/login")
        .permitAll()
        .and()
        .logout().permitAll()
        .and()
        .exceptionHandling().accessDeniedPage("/error_403");
        
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
        PasswordEncoder encoder = passwordEncoder();
        UserBuilder users = User.builder().passwordEncoder(encoder::encode);
        builder.inMemoryAuthentication()
        .withUser(users.username("admin").password("12345").roles("ADMIN","USER"))
        .withUser(users.username("cynthia").password("12345").roles("USER"));
    }
}