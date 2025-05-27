package com.oscar.vivero.controlador;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.modelo.Persona;
import com.oscar.vivero.servicio.Controlador;
import com.oscar.vivero.servicio.ServiciosEjemplar;
import com.oscar.vivero.servicio.ServiciosMensaje;
import com.oscar.vivero.servicio.ServiciosPersona;
import com.oscar.vivero.servicio.ServiciosPlanta;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mensajes")
public class MensajesController {

	@Autowired
	ServiciosMensaje servMensaje;

	@Autowired
	ServiciosPersona servPersona;

	@Autowired
	ServiciosEjemplar servEjemplar;

	@Autowired
	ServiciosPlanta servPlanta;

	@Autowired
	Controlador controlador;

	@GetMapping("/mostrarCrearMensajes")
	public String mostrarCrearMensajeFormulario(Model model) {

		model.addAttribute("mensaje", new Mensaje());

		List<Ejemplar> ejemplares = servEjemplar.vertodosEjemplares();

		model.addAttribute("ejemplares", ejemplares);

		return "/personal/CrearMensaje";

	}

	@PostMapping("/CamposMensaje")
	public String InsertarMensaje(@RequestParam("id") Long id, @RequestParam("mensaje") String mensaje, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {

		String usuario = (String) session.getAttribute("usuario");

		if (usuario == null) {
			model.addAttribute("error", "No estás autenticado. Por favor, inicia sesión.");
			return "/log/formularioLogIn";
		}

		if (id == null || id == 0) {
			model.addAttribute("error", "Debe seleccionar un ejemplar.");
			return "/personal/CrearMensaje";
		}

		if (!servEjemplar.existeIdEjemplar(id)) {
			model.addAttribute("error", "No existe el idEjemplar: " + id);
			return "/personal/CrearMensaje";
		}

		Persona p = servPersona.buscarPorNombre(usuario);
		Ejemplar ej = servEjemplar.buscarPorId(id);

		if (p == null) {
			model.addAttribute("error", "No se ha encontrado a la persona.");
			return "/personal/CrearMensaje";
		}

		if (ej == null) {
			model.addAttribute("error", "No se ha encontrado el ejemplar.");
			return "/personal/CrearMensaje";
		}

		LocalDateTime localDateTime = LocalDateTime.now();

		Mensaje m = new Mensaje();
		m.setFechahora(localDateTime);
		m.setMensaje(mensaje);
		m.setEjemplar(ej);
		m.setPersona(p);

		servMensaje.insertar(m);

		redirectAttributes.addFlashAttribute("exito", "Mensaje insertado exitosamente.");

		return "redirect:/mensajes/mostrarCrearMensajes";
	}

	@GetMapping("/mensajes")
	public String listarMensajes(Model model) {
		List<Mensaje> m = servMensaje.verTodosMensajes();
		model.addAttribute("mensajes", m);
		return "/personal/listadodeMensajes";
	}

	@GetMapping("/filtrarMensajesPorFecha")
	public String filtrarMensajesPorFecha(@RequestParam(name = "fechaInicio", required = false) String fechaInicio,
			@RequestParam(name = "fechaFin", required = false) String fechaFin, Model model) {

		if (fechaInicio == null || fechaFin == null || fechaInicio.isEmpty() || fechaFin.isEmpty()) {
			model.addAttribute("error", "Por favor, ingrese ambas fechas.");
			return "/personal/FiltrarMensajePorFechas";
		}

		try {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

			LocalDateTime fechaInicioParsed = LocalDateTime.parse(fechaInicio, formatter);
			LocalDateTime fechaFinParsed = LocalDateTime.parse(fechaFin, formatter);

			List<Mensaje> mensajesFiltrados = servMensaje.verMensajesRangoFechas(fechaInicioParsed, fechaFinParsed);

			if (mensajesFiltrados.isEmpty()) {
				model.addAttribute("error", "No se encontraron mensajes en el rango de fechas proporcionado.");
			} else {
				model.addAttribute("mensajes", mensajesFiltrados);
			}

		} catch (DateTimeParseException e) {
			model.addAttribute("error", "Las fechas deben tener el formato yyyy-MM-dd'T'HH:mm.");
		} catch (Exception e) {
			model.addAttribute("error", "Error al filtrar mensajes por fecha.");
		}

		return "/personal/FiltrarMensajePorFechas";
	}

	@GetMapping("/filtrarMensajesPersona")
	public String filtrarMensajesPorPersona(@RequestParam(name = "idPersona", required = false) Long idPersona,
			Model model) {

		List<Persona> personas = servPersona.vertodasPersonas();
		model.addAttribute("personas", personas);

		if (idPersona == null) {
			model.addAttribute("error", "Por favor, seleccione una persona.");
			return "/personal/filtrarMensajePersona";
		}

		List<Mensaje> mensajes = servMensaje.listamensajesPorIdPersona(idPersona);
		model.addAttribute("mensajes", mensajes);
		model.addAttribute("idPersonaSeleccionada", idPersona);

		return "/personal/filtrarMensajePersona";
	}

//	@GetMapping("/filtrarMensajesPorFecha")
//	public String filtrarMensajesPorFecha(@RequestParam(required = false) String fechaInicio,
//			@RequestParam(value = "fechaFin", required = false)  fechaFin, Model model) {
//			
//		System.out.println("Fecha Inicio:"+fechaInicio);
//		System.out.println("Fecha fin:"+fechaFin);
//		if (fechaInicio == null || fechaFin == null || fechaInicio.isEmpty() || fechaFin.isEmpty()) {
//			model.addAttribute("error", "Por favor, ingrese ambas fechas.");
//			return "/personal/FiltrarMensajePorFechas";
//		}
//
//		try {
//
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//			LocalDate fechaInicioParsed = LocalDate.parse(fechaInicio, formatter);
//			LocalDate fechaFinParsed = LocalDate.parse(fechaFin, formatter);
//
//			Date startDate = Date.valueOf(fechaInicioParsed);
//			Date endDate = Date.valueOf(fechaFinParsed);
//
//			List<Mensaje> mensajesFiltrados = servMensaje.verMensajesRangoFechas(startDate, endDate);
//
//			if (mensajesFiltrados.isEmpty()) {
//				model.addAttribute("error", "No se encontraron mensajes en el rango de fechas proporcionado.");
//			} else {
//				model.addAttribute("mensajes", mensajesFiltrados);
//			}
//
//			return "/personal/FiltrarMensajePorFechas";
//
//		} catch (DateTimeParseException e) {
//			model.addAttribute("error",
//					"Las fechas proporcionadas no tienen el formato correcto. Use el formato yyyy-MM-dd.");
//			return "/personal/FiltrarMensajePorFechas";
//		} catch (Exception e) {
//			model.addAttribute("error", "Ocurrió un error al procesar las fechas.");
//			return "/personal/FiltrarMensajePorFechas";
//		}
//	}

	@GetMapping("/filtrarMensajesCodigoPlanta")
	public String filtrarMensajesPorCodigoPlanta(
			@RequestParam(value = "tipoPlanta", required = false) String tipoPlanta, Model model) {
		try {

			System.out.println("Tipo Planta: " + tipoPlanta);
			List<String> codigosPlantas = servPlanta.listarCodigosDePlanta();
			model.addAttribute("tiposPlantas", codigosPlantas);

			if (tipoPlanta == null || tipoPlanta.isEmpty()) {
				model.addAttribute("error", "Por favor, seleccione un tipo de planta.");
				return "/personal/FiltrarMensajeTipoPlanta";
			}
			System.out.println("Tipo Planta: ****" + tipoPlanta);
			List<Mensaje> mensajesFiltrados = servMensaje.listamensajesPorCodigoPlanta(tipoPlanta);
			System.out.println("Tipo Planta: ************" + tipoPlanta);
			if (mensajesFiltrados.isEmpty()) {
				System.out.println("No hay mensajes");
				model.addAttribute("error", "No se encontraron mensajes para el tipo de planta seleccionado.");
			} else {
				System.out.println("Si  hay mensajes");
				model.addAttribute("mensajes", mensajesFiltrados);
			}

			return "/personal/FiltrarMensajeTipoPlanta";

		} catch (Exception e) {
			model.addAttribute("error", "Ocurrió un error al filtrar los mensajes.");
			return "/personal/FiltrarMensajeTipoPlanta";
		}
	}

	@GetMapping("GestiondeMensajes")
	public String GestiondePlantas() {
		return "/personal/GestiondeMensajes";

	}

	@GetMapping("/GestionMensajesMenuAdmin")
	public String mostrarMenuAdmin(HttpSession session) {
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

	@GetMapping("/MenuFiltradoMensajes")
	public String filtradodeMensajes() {
		return "/personal/MenuFiltradoMensajes";

	}
}