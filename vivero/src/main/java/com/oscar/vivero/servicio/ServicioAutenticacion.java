package com.oscar.vivero.servicio;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.repository.CredencialRepository;

@Service
public class ServicioAutenticacion implements UserDetailsService {

    @Autowired
    private CredencialRepository credencialRepositorio;

    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
        
        Credenciales credencial = credencialRepositorio.findByUsuario(usuario)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + usuario));

        
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + credencial.getRol())
        );

       
        return new User(
            credencial.getUsuario(),
            credencial.getPassword(),
            authorities
        );
    }
}

