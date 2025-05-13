package com.oscar.vivero.servicio;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.modelo.Persona;
import com.oscar.vivero.repository.PersonaRepository;

@Service
public class ServiciosPersona {

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	PersonaRepository personarepo;

	@Autowired
	ServiciosCredenciales servCred;

	public boolean validarPersona(String nombre, String email, String password, String usuario) {

		if (usuario.contains(" ")) {
			System.out.println("Nombre no puede contener espacios en blanco");
			return false;
		}

		if (password.contains(" ")) {
			System.out.println("La contraseña no puede contener espacios en blanco");
			return false;
		}

		if (nombre.length() > 255) {
			System.out.println("Nombre invalido");
			return false;
		}

		if (email.length() > 255) {
			System.out.println("Email invalido");
			return false;
		}

		String patron = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern pattern = Pattern.compile(patron);
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			System.out.println("Formato de email invalido..." + email);
			return false;
		}

		List<Persona> personas = personarepo.findAll();
		for (Persona p : personas) {
			if (p.getEmail().equals(email)) {
				System.out.println(p.getEmail());
				System.out.println("El email ya existe...." + email);
				return false;
			}
		}

		return true;
	}

	public List<Persona> vertodasPersonas() {
		List<Persona> plantas = personarepo.findAll();
		return plantas;
	}

	public void insertarPersona(Persona p) {
		personarepo.saveAndFlush(p);
	}

	public Optional<Persona> buscarPorId(Long id) {
		return personarepo.findById(id);
	}

	public Persona buscarPorNombre(String nombre) {
		Persona per = new Persona();

		List<Persona> personas = personarepo.findAll();

		for (Persona p : personas) {
			if (p.getCredencial().getUsuario().equals(nombre)) {

				per = p;
			}
		}

		return per;
	}

	public boolean existeidPersona(long idPersona) {
		List<Persona> personas = personarepo.findAll();

		for (Persona p : personas) {
			if (p.getId().equals(idPersona)) {

				return true;
			}
		}
		return false;
	}

	public boolean existeNombrePersona(String nombre) {
		List<Persona> personas = personarepo.findAll();

		for (Persona p : personas) {
			if (p.getNombre().equals(nombre)) {

				return true;
			}
		}
		return false;
	}

	public void guardarOActualizarPersona(Persona p) {
		sessionFactory.getCurrentSession().saveOrUpdate(p); // Asegúrate de que este método maneje tanto la inserción
															// como la actualización
	}

	public Persona buscarPersonaPorUsuarioCredencial(String usuario) {

		Persona per = new Persona();

		Optional<Credenciales> c = servCred.buscarCredencialPorUsuario(usuario);
		
		Long idCred = c.get().getId();
		List<Persona> personas = personarepo.findAll();

		for (Persona p : personas) {
			if (p.getCredencial().getId()==idCred) {
				System.out.println("Existe persona con el id");
				per = p;
			}
		}

		return per;
	}

}
