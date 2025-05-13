package com.oscar.vivero.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.modelo.Persona;
import com.oscar.vivero.servicio.ServiciosCredenciales;
import com.oscar.vivero.servicio.ServiciosPersona;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/admin")
public class PersonaController {

	@Autowired
	ServiciosPersona servPersona;

	@Autowired
	ServiciosCredenciales servCredenciales;

	@GetMapping("/personas")
	public String listarPersonas(Model model) {
		model.addAttribute("personas", servPersona.vertodasPersonas());
		return "/admin/listadodePersonas";
	}

	@PostMapping("/CamposPersona")
	@Transactional
	public String insertarPersona(@ModelAttribute Persona CrearPesonas, Model model) {

		String nombre = CrearPesonas.getNombre();
		String email = CrearPesonas.getEmail();
		String usuario = CrearPesonas.getCredencial().getUsuario();
		String password = CrearPesonas.getCredencial().getPassword();

		if (servCredenciales.existeCredencial(usuario)) {
			model.addAttribute("error", "Ya existe el usuario: " + usuario);
			return "/admin/CrearPersonas";
		} else {

			boolean personaValida = servPersona.validarPersona(nombre, email, password, password);
			if (!personaValida) {
				model.addAttribute("error", "Datos no válidos para la persona.");
				return "/admin/CrearPersonas";
			} else {

				Credenciales c = new Credenciales();
				c.setUsuario(usuario);
				c.setPassword(password);

				if (c.getRol() == null || c.getRol().isEmpty()) {
					c.setRol("PERSONAL");
				}

				servCredenciales.insertarCredencial(c);

				Persona p = new Persona();
				p.setNombre(nombre);
				p.setEmail(email);
				p.setCredencial(c);

				servPersona.insertarPersona(p);
				model.addAttribute("exito", "Persona insertada con exito");

				return "/admin/GestiondePersonas";
			}
		}
	}

	@GetMapping("/mostrarCrearPersonas")
	public String mostrarFormulario(Model model) {
		model.addAttribute("persona", new Persona());
		return "/admin/CrearPersonas";
	}
}
