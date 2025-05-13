package com.oscar.vivero.controlador;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GestiondePlantasController {

	@GetMapping({ "GestiondePlantas" })
	public String GestiondePlantas() {

		return "/admin/GestiondePlantas";

	}

	@GetMapping("/GestionPlantasMenuAdmin")  
	public String mostrarMenuAdmin() {
	    return "/admin/MenuAdmin";
	}
}