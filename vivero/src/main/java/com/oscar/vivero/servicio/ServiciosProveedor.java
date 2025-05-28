package com.oscar.vivero.servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Proveedor;
import com.oscar.vivero.repository.ProveedorRepository;

@Service
public class ServiciosProveedor {
	@Autowired
	ProveedorRepository proveedorrepo;

	public void insertarProveedor(Proveedor p) {
		proveedorrepo.saveAndFlush(p);
	}

	public boolean validarNombreProveedor(Proveedor p) {
		String nombre = p.getNombre();

		if (nombre == null || nombre.isEmpty()) {
			return false;
		}

		if (nombre.length() < 3 || nombre.length() > 30) {
			System.out.println("Longitud del nombre del Proveedor inválida");
			return false;
		}

		return true;
	}

	public boolean validarCIF(Proveedor p) {
		String cif = p.getCif();
		String cifRegex = "^[ABCDEFGHJKLMNPQRSUVW][0-9]{7}[0-9A-J]$";

		if (cif == null || !cif.toUpperCase().matches(cifRegex)) {
			System.out.println("Formato de CIF inválido");
			return false;
		}

		return true;
	}

	public boolean validarUsuario(Proveedor p) {
		String usuario = p.getCredencial().getUsuario();
		String usuarioRegx = "^[A-Za-z0-9_]+$";
		if (usuario == null || usuario.isEmpty() || usuario.contains(" ") || !usuario.matches(usuarioRegx)) {
			System.out.println("El usuario no puede ser vacío, contener espacios, ni caracteres inválidos");
			return false;
		}
		return true;
	}

	public boolean existeUsuario(Proveedor p) {
		if (!proveedorrepo.findByCredencial_Usuario(p.getCredencial().getUsuario()).isEmpty()) {
			System.out.println("El usuario ya existe...");
			return false;
		}
		return true;
	}

	public boolean validarContraseña(Proveedor p) {
		String password = p.getCredencial().getPassword();
		if (password == null || password.isEmpty() || password.contains(" ")) {
			System.out.println("La contraseña no puede ser vacía ni contener espacios");
			return false;
		}
		return true;
	}
	
	public List<String> validarProveedor(Proveedor p) {
	    List<String> errores = new ArrayList<>();

	    if (p.getNombre() == null || p.getNombre().length() < 3 || p.getNombre().length() > 30) {
	        errores.add("Nombre inválido (entre 3 y 30 caracteres).");
	    }

	    String cifRegex = "^[ABCDEFGHJKLMNPQRSUVW][0-9]{7}[0-9A-J]$";
	    if (p.getCif() == null || !p.getCif().toUpperCase().matches(cifRegex)) {
	        errores.add("CIF inválido.");
	    }

	    if (p.getCredencial().getUsuario() == null || p.getCredencial().getUsuario().isEmpty()
	            || p.getCredencial().getUsuario().contains(" ")) {
	        errores.add("Usuario inválido: no puede estar vacío ni contener espacios.");
	    }

	    if (p.getCredencial().getPassword() == null || p.getCredencial().getPassword().isEmpty()
	            || p.getCredencial().getPassword().contains(" ")) {
	        errores.add("Contraseña inválida: no puede estar vacía ni contener espacios.");
	    }

	    if (!proveedorrepo.findByCredencial_Usuario(p.getCredencial().getUsuario()).isEmpty()) {
	        errores.add("El usuario ya existe.");
	    }

	    return errores;
	}


	public Optional<Proveedor> buscarProveedorPorId(Long id) {
		return proveedorrepo.findById(id);

	}

	public Optional<Proveedor> buscarProveedorPorIdCredencial(Long id) {
		return proveedorrepo.findByCredencialId(id);

	}

}
