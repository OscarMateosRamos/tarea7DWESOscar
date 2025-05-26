package com.oscar.vivero.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.modelo.Proveedor;
import com.oscar.vivero.servicio.ServiciosCredenciales;
import com.oscar.vivero.servicio.ServiciosProveedor;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/proveedor")
public class ProveedorController {

	@Autowired
	ServiciosProveedor proveedorServ;

	@Autowired
	ServiciosCredenciales credencialesServ;

	@GetMapping("/mostrarCrearProveedor")
	public String mostrarFormulario(@RequestParam(value = "exito", required = false) String exito, Model model) {
		model.addAttribute("proveedor", new Proveedor());
		if (exito != null) {
			model.addAttribute("exito", "Proveedor creado correctamente.");
		}
		return "/proveedor/CrearProveedor";
	}

	@PostMapping("/CamposProveedor")
	public String crearProveedor(@Valid @ModelAttribute Proveedor proveedor, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "/proveedor/CrearProveedor";
		}

		if (!proveedorServ.validarProveedor(proveedor)) {
			model.addAttribute("error", "El CIF o el usuario ya existen.");
			return "/proveedor/CrearProveedor";
		}

		
		proveedor.getCredencial().setRol("PROVEEDOR");

		
		proveedorServ.insertarProveedor(proveedor);

		model.addAttribute("exito", "Proveedor creado correctamente.");
		model.addAttribute("proveedor", new Proveedor());
		return "/proveedor/CrearProveedor";
	}

}
