package com.oscar.vivero.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GestiondePersonasController {
	@GetMapping({ "GestiondePersonas" })
	public String GestiondePersonas() {
		return "/admin/GestiondePersonas";

	}

	@GetMapping("/GestionPersonasMenuAdmin")
	public String mostrarMenuAdmin() {
		return "/admin/MenuAdmin";
	}
}
