package com.oscar.vivero.servicio;

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

	public boolean validarProveedor(Proveedor p) {

		System.out.println("Proveedor: " + p);
		if (p.getNombre() == null || p.getCif() == null) {

			return false;
		}

		if (p.getNombre().length() < 3 || p.getNombre().length() > 30) {
			System.out.println("Longitud del nombre del Proveedor inválida");
			return false;
		}

		String cifRegex = "^[ABCDEFGHJKLMNPQRSUVW][0-9]{7}[0-9A-J]$";
		if (!p.getCif().toUpperCase().matches(cifRegex)) {
			System.out.println("Formato de CIF inválido");
			return false;
		}

		if (p.getCredencial().getUsuario() == null || p.getCredencial().getUsuario().isEmpty()
				|| p.getCredencial().getUsuario().contains(" ")) {
			System.out.println("El usuario no puede ser vacío ni contener espacios");
			return false;
		}

		if (p.getCredencial().getPassword() == null || p.getCredencial().getPassword().isEmpty()
				|| p.getCredencial().getPassword().contains(" ")) {
			System.out.println("La contraseña no puede ser vacía ni contener espacios");
			return false;
		}

		if (!proveedorrepo.findByCredencial_Usuario(p.getCredencial().getUsuario()).isEmpty()) {
			System.out.println("El usuario ya existe...");
			return false;
		}

		return true;
	}

	public Optional<Proveedor> buscarProveedorPorId(Long id) {
		return proveedorrepo.findById(id);

	}
	
	public Optional<Proveedor> buscarProveedorPorIdCredencial(Long id) {
		return proveedorrepo.findByCredencialId(id);

	}

}
