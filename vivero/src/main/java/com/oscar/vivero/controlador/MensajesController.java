package com.oscar.vivero.controlador;

import java.sql.Date;
import java.time.LocalDate;
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

	@PostMapping("/CamposMensaje")
	public String InsertarMensaje(@RequestParam Long id, @RequestParam String mensaje, @RequestParam String fechahora,
			Model model, HttpSession session) {

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

		Date fechaHoraDate;
		try {
			fechaHoraDate = Date.valueOf(fechahora);
		} catch (IllegalArgumentException e) {
			model.addAttribute("error", "Fecha no válida, debe ser en formato yyyy-MM-dd.");
			return "/personal/CrearMensaje";
		}

		Mensaje m = new Mensaje();
		m.setFechahora(fechaHoraDate);
		m.setMensaje(mensaje);
		m.setEjemplar(ej);
		m.setPersona(p);

		servMensaje.insertar(m);

		model.addAttribute("success", "Mensaje insertado exitosamente.");
		return "/personal/CrearMensaje";
	}

	@GetMapping("/mostrarCrearMensajes")
	public String mostrarCrearMensajeFormulario(Model model) {

		model.addAttribute("mensaje", new Mensaje());

		List<Ejemplar> ejemplares = servEjemplar.vertodosEjemplares();

		model.addAttribute("ejemplares", ejemplares);

		return "/personal/CrearMensaje";

	}

	@GetMapping("/mensajes")
	public String listarMensajes(Model model) {
		List<Mensaje> m = servMensaje.verTodosMensajes();
		model.addAttribute("mensajes", m);
		return "/personal/listadodeMensajes";
	}

	@GetMapping("/filtrarMensajesPorFecha")
	public String filtrarMensajesPorFecha(@RequestParam(value = "fechaInicio", required = false) String fechaInicio,
			@RequestParam(value = "fechaFin", required = false) String fechaFin, Model model) {

		if (fechaInicio == null || fechaFin == null || fechaInicio.isEmpty() || fechaFin.isEmpty()) {
			model.addAttribute("error", "Por favor, ingrese ambas fechas.");
			return "FiltrarMensajePorFechas";
		}

		try {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			LocalDate fechaInicioParsed = LocalDate.parse(fechaInicio, formatter);
			LocalDate fechaFinParsed = LocalDate.parse(fechaFin, formatter);

			Date startDate = Date.valueOf(fechaInicioParsed);
			Date endDate = Date.valueOf(fechaFinParsed);

			List<Mensaje> mensajesFiltrados = servMensaje.verMensajesRangoFechas(startDate, endDate);

			if (mensajesFiltrados.isEmpty()) {
				model.addAttribute("error", "No se encontraron mensajes en el rango de fechas proporcionado.");
			} else {
				model.addAttribute("mensajes", mensajesFiltrados);
			}

			return "FiltrarMensajePorFechas";

		} catch (DateTimeParseException e) {
			model.addAttribute("error",
					"Las fechas proporcionadas no tienen el formato correcto. Use el formato yyyy-MM-dd.");
			return "FiltrarMensajePorFechas";
		} catch (Exception e) {
			model.addAttribute("error", "Ocurrió un error al procesar las fechas.");
			return "FiltrarMensajePorFechas";
		}
	}

	@GetMapping("/filtrarMensajesCodigoPlanta")
	public String filtrarMensajesPorCodigoPlanta(
			@RequestParam(value = "tipoPlanta", required = false) String tipoPlanta, Model model) {
		try {

			List<String> tiposPlantas = servPlanta.listarTiposDePlanta();
			model.addAttribute("tiposPlantas", tiposPlantas);

			if (tipoPlanta == null || tipoPlanta.isEmpty()) {
				model.addAttribute("error", "Por favor, seleccione un tipo de planta.");
				return "FiltrarMensajeTipoPlanta";
			}

			List<Mensaje> mensajesFiltrados = servMensaje.listamensajesPorCodigoPlanta(tipoPlanta);

			if (mensajesFiltrados.isEmpty()) {
				model.addAttribute("error", "No se encontraron mensajes para el tipo de planta seleccionado.");
			} else {
				model.addAttribute("mensajes", mensajesFiltrados);
			}

			return "FiltrarMensajeTipoPlanta";

		} catch (Exception e) {

			model.addAttribute("error", "Ocurrió un error al filtrar los mensajes.");
			return "FiltrarMensajeTipoPlanta";
		}

	}
	
	@GetMapping( "GestiondeMensajes" )
	public String GestiondePlantas() {
		return "/personal/GestiondeMensajes";

	}

	@GetMapping("/GestionMensajesMenuAdmin")
	public String mostrarMenuAdmin(HttpSession session) {
		String rol = (String) session.getAttribute("rol");
		System.out.println("ROL: " + rol);

		if (rol.equalsIgnoreCase("admin")) {
			return "/admin/MenuAdmin";
		} else {
			if (rol.equalsIgnoreCase("personal")) {
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
