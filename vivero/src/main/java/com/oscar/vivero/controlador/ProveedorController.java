package com.oscar.vivero.controlador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.modelo.Proveedor;
import com.oscar.vivero.repository.ProveedorRepository;
import com.oscar.vivero.servicio.ServiciosCredenciales;
import com.oscar.vivero.servicio.ServiciosLote;
import com.oscar.vivero.servicio.ServiciosProveedor;

@Controller
@RequestMapping("/proveedor")
public class ProveedorController {

	@Autowired
	ServiciosProveedor proveedorServ;

	@Autowired
	ServiciosCredenciales credencialesServ;

	@Autowired
	ServiciosLote lotesServ;

	@Autowired
	PasswordEncoder passwordencoder;

	@Autowired
	ProveedorRepository proveedorrepo;

	@GetMapping("/mostrarCrearProveedor")
	public String mostrarFormulario(@RequestParam(value = "exito", required = false) String exito, Model model) {
		model.addAttribute("proveedor", new Proveedor());
		if (exito != null) {
			model.addAttribute("exito", "Proveedor creado correctamente.");
		}
		return "/proveedor/CrearProveedor";
	}

	@PostMapping("/CamposProveedor") // Nuevo Proveedor
	public String crearProveedor(@ModelAttribute Proveedor CrearProveedor, Model model) {

		List<String> errores = new ArrayList<>();

		String usuario = CrearProveedor.getCredencial().getUsuario();
		String password = CrearProveedor.getCredencial().getPassword();
		String cif = CrearProveedor.getCif();
		String nombre = CrearProveedor.getNombre();

		boolean validoOK = true;

		if (!proveedorServ.validarNombreProveedor(CrearProveedor)) {
			errores.add("Nombre inválido.");
			validoOK = false;
		}

		if (!proveedorServ.validarCIF(CrearProveedor)) {
			errores.add("CIF inválido.");
			validoOK = false;
		}

		if (!proveedorServ.validarContraseña(CrearProveedor)) {
			errores.add("Contraseña inválida.");
			validoOK = false;
		}

		if (!proveedorrepo.findByCredencial_Usuario(CrearProveedor.getCredencial().getUsuario()).isEmpty()) {
			errores.add("Usuario inválido.");
			validoOK = false;
		}
//	}
//		if (!proveedorServ.validarUsuario(CrearProveedor)) {
//			errores.add("Usuario inválido.");
//			validoOK = false;
//		}

		if (!proveedorServ.existeUsuario(CrearProveedor)) {
			errores.add("El usuario ya existe.");
			validoOK = false;
		}

		if (!proveedorrepo.findByCif(cif).isEmpty()) {
			errores.add("El CIF ya está registrado.");
			validoOK = false;
		}

		if (!validoOK) {
			model.addAttribute("errores", errores);
			model.addAttribute("proveedor", CrearProveedor);
			return "/proveedor/CrearProveedor";
		}

		try {
			Credenciales cr = new Credenciales();
			cr.setUsuario(usuario);
			cr.setPassword(passwordencoder.encode(password));
			cr.setRol("PROVEEDOR");

			Proveedor p = new Proveedor();

			p.setCif(cif);
			p.setNombre(nombre);

			proveedorServ.insertarProveedor(p);

			p.setCredencial(cr);

			proveedorServ.insertarProveedor(p);

		} catch (Exception e) {
			errores.add("Error al guardar el proveedor o las credenciales: " + e.getMessage());
			e.printStackTrace();
			model.addAttribute("errores", errores);
			model.addAttribute("proveedor", CrearProveedor);
			return "/proveedor/CrearProveedor";
		}

		model.addAttribute("exito", "Proveedor creado correctamente.");
		model.addAttribute("proveedor", new Proveedor());

		return "/proveedor/CrearProveedor";
	}

}
