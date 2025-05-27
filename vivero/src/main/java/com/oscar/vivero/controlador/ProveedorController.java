package com.oscar.vivero.controlador;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.modelo.Estado;
import com.oscar.vivero.modelo.Lote;
import com.oscar.vivero.modelo.Proveedor;
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

		if (!proveedorServ.validarProveedor(CrearProveedor)) {
			model.addAttribute("error", "El CIF o el usuario ya existen.");
			return "/proveedor/CrearProveedor";
		}

		String usuario = CrearProveedor.getCredencial().getUsuario();
		String password = CrearProveedor.getCredencial().getPassword();

		System.out.println("Usuario: " + usuario);
		System.out.println("Password: " + password);

		Credenciales cr = new Credenciales();

		cr.setUsuario(usuario);
		cr.setPassword(password);
		cr.setRol("PROVEEDOR");
		credencialesServ.insertarCredencial(cr);

		Proveedor p = new Proveedor();

		p.setCif(CrearProveedor.getCif());
		p.setNombre(CrearProveedor.getNombre());
		// p.setCredencial(cr);

		proveedorServ.insertarProveedor(p);

		p.setCredencial(cr);
		proveedorServ.insertarProveedor(p);

		System.out.println("Usuario: " + usuario);
		System.out.println("Password: " + password);
		System.out.println("Proveedor: " + p);
		System.out.println("Credencial: " + cr);

		model.addAttribute("exito", "Proveedor creado correctamente.");
		model.addAttribute("proveedor", new Proveedor());
		return "/proveedor/CrearProveedor";
	}

}
