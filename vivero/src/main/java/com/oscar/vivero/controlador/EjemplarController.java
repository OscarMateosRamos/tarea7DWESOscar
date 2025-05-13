package com.oscar.vivero.controlador;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.modelo.Persona;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.repository.EjemplarRepository;
import com.oscar.vivero.servicio.Controlador;
import com.oscar.vivero.servicio.ServiciosEjemplar;
import com.oscar.vivero.servicio.ServiciosMensaje;
import com.oscar.vivero.servicio.ServiciosPersona;
import com.oscar.vivero.servicio.ServiciosPlanta;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/ejemplares")
public class EjemplarController {
	private final Logger log = (Logger) LoggerFactory.getLogger(MainController.class);

	@Autowired
	EjemplarRepository ejemplarrepo;

	@Autowired
	ServiciosPersona servPersona;
	@Autowired
	ServiciosMensaje servMensaje;
	@Autowired
	ServiciosEjemplar servEjemplar;
	@Autowired
	ServiciosPlanta servPlanta;
	@Autowired
	Controlador controlador;

	@PostMapping("/CamposEjemplar")
	public String InsertarEjemplar(@ModelAttribute Ejemplar CrearEjemplar, Model model, HttpSession session) {
		log.info("****Usuario " + session.getAttribute("usuario") + "*");
		
		
		
	    String codigoPlanta = CrearEjemplar.getPlanta().getCodigo();

	    if (!servPlanta.existeCodigoPlanta(codigoPlanta)) {
	        model.addAttribute("error", "No existe el código de la planta.");
	        return "/personal/CrearEjemplar";
	    }

	    String nombreOriginal = CrearEjemplar.getNombre();
	    List<Ejemplar> ejemplares = ejemplarrepo.findAll();
	    for (Ejemplar e : ejemplares) {
	        if (e.getNombre().equals(nombreOriginal)) {
	            nombreOriginal = nombreOriginal + "_" + e.getId();
	            break;
	        }
	    }

	    
	    List<Planta> plantas = servPlanta.encontrarPlantasPorCodigo(codigoPlanta);
	    if (plantas.isEmpty()) {
	        model.addAttribute("error", "La planta con el código " + codigoPlanta + " no fue encontrada.");
	        return "/personal/CrearEjemplar";
	    }

	    Planta planta = plantas.get(0);
	    CrearEjemplar.setPlanta(planta);
	    CrearEjemplar.setNombre(nombreOriginal);

	    
	    String usuario = (String) session.getAttribute("usuario");
	    if (usuario == null) {
	        model.addAttribute("error", "No se encontró al usuario actual.");
	        return "/personal/CrearEjemplar";
	    }

	    
	    String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
	    String contenidoMensaje = "Añadido nuevo ejemplar " + CrearEjemplar.getNombre() +
	                              " por " +  usuario + " (" + fechaHora + ").";

	    Mensaje mensaje = new Mensaje();
	    mensaje.setMensaje(contenidoMensaje);
	    mensaje.setPersona(servPersona.buscarPersonaPorUsuarioCredencial(usuario));
	    mensaje.setEjemplar(CrearEjemplar);

	  
	    if (CrearEjemplar.getMensajes() == null) {
	        CrearEjemplar.setMensajes(new ArrayList<>());
	    }
	    CrearEjemplar.getMensajes().add(mensaje);

	   
	    servEjemplar.insertarEjemplar(CrearEjemplar);

	    model.addAttribute("exito", "Ejemplar creado con éxito.");
	    return "/personal/CrearEjemplar";
	}


	@GetMapping("/mostrarCrearEjemplar")
	public String mostrarCrearEjemplarFormulario(Model model) {

		List<Ejemplar> ejemplares = servEjemplar.vertodosEjemplares();
		model.addAttribute("ejemplares", ejemplares);

		Ejemplar ejemplar = new Ejemplar();
		model.addAttribute("ejemplar", ejemplar);

		return "/personal/CrearEjemplar";
	}

	@GetMapping("/ejemplaresTipoPlanta")
	public String listarEjemplaresTipoPlanta(@RequestParam(name="codigo",required = false) String codigo, Model model) {
		List<Planta> plantas = servPlanta.vertodasPlantas();
		model.addAttribute("plantas", plantas);

		List<Ejemplar> ejemplares;
		if (codigo != null && !codigo.isEmpty()) {
			ejemplares = servEjemplar.listaejemplaresPorTipoPlanta(codigo);
		} else {
			ejemplares = servEjemplar.vertodosEjemplares();
		}

		model.addAttribute("ejemplares", ejemplares);
		model.addAttribute("codigoPlanta", codigo);

		return "/personal/listadoEjemplaresTipoPlanta";
	}

	@GetMapping("/verMensajesEjemplar")
	@Transactional
	public String verMensajesDelEjemplar(@RequestParam(name = "ejemplarId", required = false) Long ejemplarId, Model model) {

		List<Ejemplar> ejemplares = servEjemplar.vertodosEjemplares();
		model.addAttribute("ejemplares", ejemplares);

		if (ejemplarId != null) {
			Ejemplar ejemplar = servEjemplar.buscarPorId(ejemplarId);

			if (ejemplar == null) {
				model.addAttribute("error", "Ejemplar no encontrado");
			} else {
				List<Mensaje> mensajes = ejemplar.getMensajes();
				if (mensajes.isEmpty()) {
					model.addAttribute("mensajeError", "Este ejemplar no tiene mensajes.");
				} else {
					model.addAttribute("mensajes", mensajes);
				}
				model.addAttribute("ejemplar", ejemplar);
			}
		} else {

			List<Mensaje> todosLosMensajes = servMensaje.verTodosMensajes();
			model.addAttribute("mensajes", todosLosMensajes);
		}

		return "/personal/verMensajesEjemplar";
	}

	@GetMapping("/GestionStock")
	public String gestionStockEjemplares(Model model) {
		List<Planta> plantas = servPlanta.vertodasPlantas(); // Método que devuelve todas las plantas
		model.addAttribute("plantas", plantas);
		return "/personal/GestionStock";
	}
	
	@GetMapping({ "GestiondeEjemplares" })
	public String GestiondeEjemplares() {

		return "/personal/GestiondeEjemplares";

	}

	@GetMapping("/GestiondeEjemplaresMenuAdmin")
	public String mostrarMenuAdminEjemplares(HttpSession session) {
		String rol = (String) session.getAttribute("rol");
		System.out.println("ROL: " + rol);

		if (rol.equalsIgnoreCase("ADMIN")) {
			return "/admin/MenuAdmin";
		} else {
			if (rol.equalsIgnoreCase("PERSONAL")) {
				return "/personal/MenuPersonal";
			} else {
				return "/inicio";
			}
		}

	}
	
	

}
