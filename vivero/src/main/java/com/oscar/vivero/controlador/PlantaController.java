package com.oscar.vivero.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.servicio.ServiciosPlanta;

@Controller
@RequestMapping("/plantas")
public class PlantaController {
	@Autowired
	ServiciosPlanta servPlanta;

	@GetMapping("/plantasAdmin")
	public String listarPlantasAd(Model model) {
		List<Planta> p = servPlanta.vertodasPlantas();
		model.addAttribute("plantas", p);
		return "/admin/listadodePlantasAdmin";
	}
	
	@GetMapping("/plantasInvitado")
	public String listarPlantasInv(Model model) {
		List<Planta> p = servPlanta.vertodasPlantas();
		model.addAttribute("plantas", p);
		return "/listadodePlantas";
	}
	
	
	@GetMapping("/plantasPersonal")
	public String listarPlantasPer(Model model) {
		List<Planta> p = servPlanta.vertodasPlantas();
		model.addAttribute("plantas", p);
		return "/personal/listadodePlantasPersonal";
	}

	@PostMapping("/CamposPlanta")
	public String InsertarPlanta(@ModelAttribute Planta CrearPlanta, Model model) {

		Planta p = new Planta();

		String codigo = CrearPlanta.getCodigo();
		String nombreComun = CrearPlanta.getNombrecomun();
		String nombreCientifico = CrearPlanta.getNombrecientifico();

		if (CrearPlanta.getCodigo() == null || CrearPlanta.getCodigo().isEmpty()) {
			model.addAttribute("error", "El código de la planta es obligatorio.");
			return "/admin/CrearPlantas";
		}

		p.setCodigo(codigo);
		p.setNombrecomun(nombreComun);
		p.setNombrecientifico(nombreCientifico);
		boolean credValidas = servPlanta.validarPlanta(p);

		if (!credValidas) {
			model.addAttribute("error", " campos de la Planta Invalidos.");
			return "/admin/CrearPlantas";
		}

		servPlanta.insertarPlanta(p);

		return "/admin/CrearPlantas";
	}

	@PostMapping("/CamposModificarPlanta/{id}")
	public String ModificarPlanta(@PathVariable Long id, @ModelAttribute Planta modificarPlanta, RedirectAttributes redirectAttributes) {
	    Planta existePlanta = servPlanta.buscarPlantaPorId(id);

	    if (existePlanta == null) {
	        redirectAttributes.addFlashAttribute("error", "Planta no encontrada.");
	        return "/admin/ModificarPlantas";
	    }

	    
	    existePlanta.setNombrecomun(modificarPlanta.getNombrecomun());
	    
	    existePlanta.setNombrecientifico(modificarPlanta.getNombrecientifico());

	    boolean credValidas = servPlanta.validarPlantaSinCodigo(existePlanta);
	    if (!credValidas) {
	        redirectAttributes.addFlashAttribute("error", "Campos de la Planta inválidos.");
	        return "/admin/ModificarPlantas";
	    }

	    servPlanta.modificarPlanta(existePlanta);
	    redirectAttributes.addFlashAttribute("exito", "Planta modificada correctamente.");

	    return "/admin/GestiondePlantas";
	}

	@GetMapping("/CrearPlantas")
	public String mostrarCrearPlantaFormulario(Model model) {
		model.addAttribute("planta", new Planta());
		return "/admin/CrearPlantas";
	}

	@GetMapping("/ModificarPlantas")
	public String mostrarCrearModificarPlantasFormulario(Model model) {
		List<Planta> p = servPlanta.vertodasPlantas();
		model.addAttribute("plantas", p);
		return "/admin/ModificarPlanta";
	}

	@GetMapping("/formularioModificarPlanta/{id}")
	public String mostrarFormularioModificarPlanta(@PathVariable Long id, Model model) {

		Planta p = servPlanta.buscarPlantaPorId(id);

		if (p == null) {
			model.addAttribute("error", "Planta no encontrada");
			return "ModificarPlanta";
		}

		model.addAttribute("planta", p);
		return "/admin/formularioModificarPlanta";
	}

}
