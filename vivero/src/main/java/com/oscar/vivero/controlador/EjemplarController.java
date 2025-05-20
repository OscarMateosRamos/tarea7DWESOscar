package com.oscar.vivero.controlador;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.oscar.vivero.repository.PersonaRepository;
import com.oscar.vivero.repository.PlantaRepository;
import com.oscar.vivero.servicio.Controlador;
import com.oscar.vivero.servicio.ServiciosCredenciales;
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
	PersonaRepository personarepo;

	@Autowired
	PlantaRepository plantarepo;

	@Autowired
	ServiciosPersona servPersona;
	@Autowired
	ServiciosMensaje servMensaje;
	@Autowired
	ServiciosEjemplar servEjemplar;
	@Autowired
	ServiciosPlanta servPlanta;
	@Autowired
	ServiciosCredenciales servCred;
	@Autowired
	Controlador controlador;

	@Transactional
	@PostMapping("/CamposEjemplar")
	public String InsertarEjemplar(@ModelAttribute Ejemplar CrearEjemplar, Model model, HttpSession session) {

		String codigoPlanta = CrearEjemplar.getPlanta().getCodigo();

		System.out.println("Codigo de Planta del ejemplar: " + codigoPlanta);
		if (servPlanta.existeCodigoPlanta(codigoPlanta)) {

			Ejemplar ej = new Ejemplar();

			List<Planta> plantas = servPlanta.encontrarPlantasPorCodigo(codigoPlanta);
			if (!plantas.isEmpty()) {
				ej.setPlanta(plantas.get(0));
			} else {
				model.addAttribute("error", "La planta con el nombre " + codigoPlanta + " no fue encontrada.");
				return "/personal/CrearEjemplar";
			}
			int numeroEjemplar = ejemplarrepo.findAll().size() + 1;

			String nuevoNombre = codigoPlanta.toUpperCase() + "_" + numeroEjemplar;
			ej.setNombre(nuevoNombre);
			System.out.println("nombre del ejemplar" + nuevoNombre);

			Date fecha = Date.valueOf(LocalDate.now());

			String usuario = (String) session.getAttribute("usuario");

			System.out.println("Usuario en sesion: " + usuario);

			String mensajeCadena = "Añadido nuevo ejemplar " + ej.getNombre() + " por " + usuario + " (" + fecha
					+ " ).";

			Mensaje mensaje = new Mensaje();
			mensaje.setMensaje(mensajeCadena);
			mensaje.setFechahora(fecha);

			for (Persona p : personarepo.findAll()) {
				if (p.getCredencial().getId().equals(servCred.buscarCredencialPorUsuario(usuario).get().getId())) {
					System.out.println("Econtrado el id persona y el id Credencial");
					mensaje.setPersona(p);
				}
			}

			// servMensaje.insertar(mensaje);

			if (ej.getMensajes() == null) {
				ej.setMensajes(new ArrayList<>());
			}

			ej.getMensajes().add(mensaje);

			servEjemplar.insertarEjemplar(ej);

			Planta pl = new Planta();
			pl = servPlanta.buscarPlantaPorCodigo(codigoPlanta);

			Long existencias = pl.getCantidadDisponible();

			pl.setCantidadDisponible(existencias + 1);

			servPlanta.modificarPlanta(pl);
			model.addAttribute("exito", "Creado el Ejemplar: " + nuevoNombre);
			return "/personal/CrearEjemplar";
		} else {
			model.addAttribute("error", "No existe el código de la planta.");
			return "/personal/CrearEjemplar";

		}
	}

//	
//
//	List<Ejemplar> ejemplares = ejemplarrepo.findAll();
//
//	for (Ejemplar e : ejemplares) {
//		if (e.getNombre().equals(ej.getNombre())) {
//			String nuevoNombre = ej.getNombre() + "_" + e.getId();
//			ej.setNombre(nuevoNombre);
//			ejemplarrepo.saveAndFlush(ej);
//
//			Mensaje m = new Mensaje();
//
//			LocalDate fechahora = LocalDate.now();
//			Date date = Date.valueOf(fechahora);
//
//			
//
//			String mensaje = "Añadido nuevo ejemplar " + ej.getNombre() + " por " + p.get().getNombre() + " ("
//					+ fechahora + " ).";
//			m.setEjemplar(ej);
//
//			m.setFechahora(date);
//
//			m.setMensaje(mensaje);
//
//			Optional<Persona> personas = servPersona.buscarPorId(Long.valueOf(1));
//			m.setPersona(personas.get());
//			servMensaje.insertar(m);
//		}
//	}

	@GetMapping("/mostrarCrearEjemplar")
	public String mostrarCrearEjemplarFormulario(Model model) {

		List<Ejemplar> ejemplares = servEjemplar.vertodosEjemplares();
		model.addAttribute("ejemplares", ejemplares);

		Ejemplar ejemplar = new Ejemplar();
		model.addAttribute("ejemplar", ejemplar);

		return "/personal/CrearEjemplar";
	}

	@GetMapping("/ejemplaresTipoPlanta")
	public String listarEjemplaresTipoPlanta(@RequestParam(name = "codigo", required = false) List<String> codigos,
	                                         Model model) {
	    List<Planta> plantas = servPlanta.vertodasPlantas();
	    model.addAttribute("plantas", plantas);
	    model.addAttribute("codigoSeleccionado", codigos);

	    List<Ejemplar> ejemplaresFiltrados = new ArrayList<>();
	    Map<Long, Integer> mensajeCounts = new HashMap<>();
	    Map<Long, Date> ultimaFechaMensaje = new HashMap<>();

	    if (codigos != null && !codigos.isEmpty()) {
	        ejemplaresFiltrados = servEjemplar.findByPlantaCodigos(codigos);

	        for (Ejemplar ej : ejemplaresFiltrados) {
	            List<Mensaje> mensajes = servMensaje.verPorIdEjemplar(ej.getId());
	            mensajeCounts.put(ej.getId(), mensajes.size());

	            // Convertir Timestamp a Date y obtener la última fecha de mensaje
	            mensajes.stream()
	                    .map(Mensaje::getFechahora)  // Esto devuelve un Timestamp
	                    .map(timestamp -> new Date(timestamp.getTime()))  // Convertir a Date
	                    .max(Date::compareTo)  // Comparar las fechas
	                    .ifPresent(fecha -> ultimaFechaMensaje.put(ej.getId(), fecha));
	        }
	    }

	    model.addAttribute("ejemplares", ejemplaresFiltrados);
	    model.addAttribute("mensajeCounts", mensajeCounts);
	    model.addAttribute("ultimaFechaMensaje", ultimaFechaMensaje);

	    if (ejemplaresFiltrados.isEmpty() && codigos != null && !codigos.isEmpty()) {
	        model.addAttribute("mensajeFiltro", "No se encontraron ejemplares para los tipos de planta seleccionados.");
	    }

	    return "/personal/listadoEjemplaresTipoPlanta";
	}


/////CORREGIR
////	Se ha implementado el CU5B: Filtrar ejemplares por tipo de planta. Un usuario 
////	autenticado podrá filtrar los datos de los ejemplares de uno o de varios tipos de plantas elegidos. Se le 
////	mostrarán todos los ejemplares de esos tipos mostrados en una tabla con el nombre del ejemplar 
////	seguido del nº de mensajes asociados al mismo y la fecha/hora del último de éstos.
//
//	@GetMapping("/ejemplaresTipoPlanta")
//	public String listarEjemplaresTipoPlanta(@RequestParam(name = "codigo[]", required = false) String[] codigo,
//	                                          Model model) {
//	    // Obtener todas las plantas disponibles
//	    List<Planta> plantas = servPlanta.vertodasPlantas();
//	    model.addAttribute("plantas", plantas);
//
//	 //   String[] codigos = codigo.toString().split(",");
//	    for(String c: codigo) {
//	    	
//	    }
//	    
//	    
////	    // Si se seleccionaron dos códigos de planta, filtramos los ejemplares por los dos códigos
////	    if (codigo != null && codigo.size() == 2) {
////	        // Filtrar los ejemplares por los dos códigos seleccionados
////	        List<Ejemplar> ejemplares = servEjemplar.listaejemplaresPorTipoPlanta(codigo);
////
////	        // Agregar los ejemplares filtrados al modelo
////	        model.addAttribute("ejemplares", ejemplares);
////	    } else {
////	        // Si no se seleccionaron exactamente dos códigos de planta, mostramos todos los ejemplares
////	        List<Ejemplar> ejemplares = servEjemplar.vertodosEjemplares();
////	        model.addAttribute("ejemplares", ejemplares);
////	    }
//
//	    model.addAttribute("codigoPlanta", codigo);
//	    return "/personal/listadoEjemplaresTipoPlanta";  
//	}

	@GetMapping("/verMensajesEjemplar")
	@Transactional
	public String verMensajesDelEjemplar(@RequestParam(name = "ejemplarId", required = false) Long ejemplarId,
			Model model) {

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
