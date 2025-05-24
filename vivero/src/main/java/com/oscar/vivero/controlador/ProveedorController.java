package com.oscar.vivero.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.Proveedor;
import com.oscar.vivero.servicio.ServiciosProveedor;

@Controller
@RequestMapping("/proveedor")
public class ProveedorController {

	@Autowired
	ServiciosProveedor proveedorServ;

	@GetMapping("/mostrarCrearProveedor")
	public String mostrarFormulario(Model model) {
		model.addAttribute("proveedor", new Proveedor());
		return "/proveedor/CrearProveedor";
	}

	@PostMapping("/CamposProveedor")
	public String crearProveedor(@ModelAttribute Proveedor proveedor, Model model) {
		if (proveedorServ.validarProveedor(proveedor)) {
			proveedorServ.insertarProveedor(proveedor);
			model.addAttribute("exito", "Proveedor creado correctamente.");
		} else {
			model.addAttribute("error", "Error al crear el proveedor. Verifica los datos.");
		}
		return "/proveedor/CrearProveedor";
	}

}
