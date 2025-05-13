package com.oscar.vivero.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	  @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	                .csrf(csrf -> csrf.disable())  
	                .authorizeHttpRequests(auth -> auth
	                                
	                                .requestMatchers("/CSS/**", "/imagenes/**").permitAll()
	                              
	                                .requestMatchers("/auth/**", "/cliente/Registro","/plantas/plantasInvitado").permitAll()
	                                .requestMatchers("/plantas/plantasPersonal").hasRole("PERSONAL")
	                                .requestMatchers("/clientes/**").hasRole("CLIENTE")
	                                .requestMatchers("/personas/**", "/plantas/**").hasRole("ADMIN")
	                                .requestMatchers("/mensajes/**").hasAnyRole("ADMIN", "PERSONAL")
	                                .requestMatchers("/ejemplares/**").hasAnyRole("ADMIN", "PERSONAL")
	                                
	                                // Cualquier otra ruta es privada Cambiar despues a authenticated().
	                                .anyRequest().permitAll() 
	                                
	                )
	                .formLogin(form -> form
	                                .loginPage("/inicio")
	                                .loginProcessingUrl("/auth/leerlogin")
	                                .defaultSuccessUrl("/auth/redireccionar")
	                                .failureUrl("/inicio?error=true")
	                                .permitAll()
	                )
	                .logout(logout -> logout
	                                .logoutUrl("/auth/logout")
	                                .logoutSuccessUrl("/inicio")
	                                .permitAll()
	                )
	                .exceptionHandling(exception -> exception
	                                .accessDeniedPage("/auth/prohibido")
	                );
	    
	        return http.build();
	    }

	    @Bean
	    PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
}
