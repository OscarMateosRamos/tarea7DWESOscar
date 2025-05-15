package com.oscar.vivero.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
               
                .requestMatchers("/CSS/**", "/imagenes/**").permitAll()
                .requestMatchers("/auth/**", "/cliente/Registro", "/plantas/plantasInvitado").permitAll()
                .requestMatchers("/inicio", "/auth/leerlogin").permitAll() 
                .requestMatchers("/plantas/plantasPersonal").hasRole("PERSONAL")
                .requestMatchers("/clientes/**").hasRole("CLIENTE")
                .requestMatchers("/personas/**", "/plantas/**").hasRole("ADMIN")
                .requestMatchers("/mensajes/**", "/ejemplares/**").hasAnyRole("ADMIN", "PERSONAL")
                .anyRequest().authenticated()  
            )
            .formLogin(form -> form
                .loginPage("/login")  
                .loginProcessingUrl("/auth/leerlogin")  
                .defaultSuccessUrl("/auth/redireccionar", true)  
                .permitAll()  
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/inicio")
                .permitAll()
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/auth/prohibido")
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) 
                .maximumSessions(1).expiredUrl("/auth/login")  
            );
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  
    }
}
