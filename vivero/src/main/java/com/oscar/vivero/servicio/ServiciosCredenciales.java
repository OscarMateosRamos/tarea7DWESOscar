package com.oscar.vivero.servicio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.repository.CredencialRepository;

@Service
public class ServiciosCredenciales {

	@Autowired
	private CredencialRepository credencialrepo;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public void insertarCredencial(Credenciales c) {

		String passwordCifrada = passwordEncoder.encode(c.getPassword());

		c.setPassword(passwordCifrada);

		credencialrepo.saveAndFlush(c);
	}

	public boolean validarUsuarioPassword(Credenciales c) {
		if (c.getUsuario().isEmpty() || c.getPassword().isEmpty()) {
			return false;
		}
		String regpassword = "^([A-Za-z0-9_!?¿+-]){3,}$";
		return c.getPassword().matches(regpassword);
	}

	public Optional<Credenciales> buscarCredencialPorUsuario(String usuario) {
		return credencialrepo.findByUsuario(usuario);
	}

	public boolean verificaUsuario(String usuario, String password) {
		Optional<Credenciales> cred = credencialrepo.findByUsuario(usuario);
		return cred != null && passwordEncoder.matches(password, cred.get().getPassword());
	}

	public void actualizarRol(String usuario, String rol) {
		Optional<Credenciales> c = credencialrepo.findByUsuario(usuario);
		if (c != null) {
			c.get().setRol(rol);

			insertarCredencial(c.get());
		} else {
			System.out.println("Usuario no encontrado.");
		}
	}

	public boolean existeCredencial(String usuario) {
		return credencialrepo.existsByUsuario(usuario);
	}
}
