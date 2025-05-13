package com.oscar.vivero.servicio;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Cliente;
import com.oscar.vivero.repository.ClienteRepository;
import com.oscar.vivero.repository.CredencialRepository;

@Service
public class ServiciosCliente {

	@Autowired
	ClienteRepository clienterepo;

	@Autowired
	CredencialRepository credencialrepo;

	public boolean validarCliente(String nombre, String email, String nif, String telefono, String direccion,
			String usuario, String password) {

		if (nombre == null || nombre.isEmpty() || nombre.length() > 255) {
			System.out.println("Nombre inválido");
			return false;
		}

		if (email == null || email.isEmpty() || email.length() > 255) {
			System.out.println("Email inválido");
			return false;
		}

		if (direccion == null || direccion.isEmpty() || direccion.length() > 255) {
			System.out.println("Dirección inválida");
			return false;
		}

		String patronEmail = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern patternEmail = Pattern.compile(patronEmail);
		Matcher matcherEmail = patternEmail.matcher(email);
		if (!matcherEmail.matches()) {
			System.out.println("Formato de email inválido: " + email);
			return false;
		}

		if (nif == null || nif.isEmpty() || nif.length() != 9) {
			System.out.println("NIF inválido: " + nif);
			return false;
		}

		String patronNIF = "^\\d{8}[A-Z]$";
		Pattern patternNIF = Pattern.compile(patronNIF);
		Matcher matcherNIF = patternNIF.matcher(nif);
		if (!matcherNIF.matches()) {
			System.out.println("Formato de NIF inválido: " + nif);
			return false;
		}

		if (telefono == null || telefono.isEmpty() || telefono.length() != 9) {
			System.out.println("Teléfono inválido: " + telefono);
			return false;
		}

		String patronTelefono = "^[6789]\\d{8}$";
		Pattern patternTelefono = Pattern.compile(patronTelefono);
		Matcher matcherTelefono = patternTelefono.matcher(telefono);
		if (!matcherTelefono.matches()) {
			System.out.println("Formato de teléfono inválido: " + telefono);
			return false;
		}

		if (usuario == null || usuario.isEmpty() || usuario.contains(" ")) {
			System.out.println("El usuario no puede ser vacío ni contener espacios");
			return false;
		}

		if (password == null || password.isEmpty() || password.contains(" ")) {
			System.out.println("La contraseña no puede ser vacía ni contener espacios");
			return false;
		}

		if (clienterepo.existsByEmail(email)) {
			System.out.println("El email ya existe: " + email);
			return false;
		}

		if (credencialrepo.existsByUsuario(usuario)) {
			System.out.println("El usuario ya existe: " + usuario);
			return false;
		}

		return true;
	}

	public void insertarCliente(Cliente c) {
		clienterepo.saveAndFlush(c);
	}

	public Cliente buscarClientePorId(Long id) {
		return clienterepo.findById(id).orElse(null);
	}

	public Cliente buscarClientePorIdCredencial(Long idCredencial) {
		for (Cliente c : clienterepo.findAll()) {
			if (c.getCredencial().getId().equals(idCredencial)) {
				return c;
			}
		}
		return null;
	}

}


