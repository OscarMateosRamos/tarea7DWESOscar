package com.oscar.vivero.controlador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.Estado;
import com.oscar.vivero.modelo.LineaLote;
import com.oscar.vivero.modelo.Lote;
import com.oscar.vivero.modelo.Proveedor;
import com.oscar.vivero.repository.PlantaRepository;
import com.oscar.vivero.repository.ProveedorRepository;
import com.oscar.vivero.servicio.ServiciosLineasLote;
import com.oscar.vivero.servicio.ServiciosLote;
import com.oscar.vivero.servicio.ServiciosPersona;
import com.oscar.vivero.servicio.ServiciosProveedor;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/lote")
public class LoteController {

	@Autowired
	ProveedorRepository proveedorRepo;

	@Autowired
	PlantaRepository plantaRepo;

	@Autowired
	ServiciosLote loteServ;

	@Autowired
	ServiciosPersona personaServ;

	@Autowired
	ServiciosProveedor proveedorServ;

	@Autowired
	ServiciosLineasLote lineasLoteServ;

	@GetMapping("/GestiondeLotes")
	public String gestiondeLotes() {
		return "/personal/GestiondeLotes";
	}

	// Mostrar formulario inicial: proveedor + urgente + listado de plantas
	@GetMapping("/mostrarCrearLote")
	public String mostrarFormularioLote(HttpSession session, Model model) {

		ArrayList<LineaLote> lotesSession = (ArrayList<LineaLote>) session.getAttribute("lotesSesion");
		if (lotesSession == null) {
			lotesSession = new ArrayList<>();
		}

		Long codigoProveedor = (Long) session.getAttribute("codigoProveedor");
		// System.out.println("++++++ codigoProveedor"+codigoProveedor);

		model.addAttribute("plantas", plantaRepo.findAll());

		if (codigoProveedor == null) {

			model.addAttribute("proveedores", proveedorRepo.findAll());
			return "/personal/CrearLote";
		} else {

			model.addAttribute("codigoProveedor", codigoProveedor);

			return "/personal/LineasLote";
		}

	}

	// Guardar proveedor y urgente en sesión
	@PostMapping("/guardarDatosLote")
	public String guardarDatosLote(@RequestParam("codigoProveedor") long codigoProveedor,
			@RequestParam(value = "urgente", required = false) boolean urgente, HttpSession session, Model model) {

		Optional<Proveedor> p = proveedorServ.buscarProveedorPorId(codigoProveedor);

		session.setAttribute("codigoProveedor", codigoProveedor);
		session.setAttribute("proveedor", p);
		session.setAttribute("urgente", urgente);

		model.addAttribute("codigoProveedor", codigoProveedor);
		model.addAttribute("nombreProveedor", p.get().getNombre());
		// model.addAttribute("urgente", urgente);)
		model.addAttribute("plantas", plantaRepo.findAll());
		return "/personal/LineasLote";
	}

	// Añadir planta (línea lote) a la lista en sesión
	@PostMapping("/añadirALote")
	public String añadirALote(@RequestParam("codigo") String codigo, @RequestParam("cantidad") int cantidad,

			HttpSession session, Model model) {

		List<LineaLote> lineasLote = (List<LineaLote>) session.getAttribute("lotesSession");
		if (lineasLote == null) {
			System.out.println("+++++++++++");
			lineasLote = new ArrayList<>();
		}

		boolean existe = false;
		Long id = (Long) session.getAttribute("codigoProveedor");

		System.out.println("session.getAttribute( codigoProveedor )" + session.getAttribute("codigoProveedor"));

		for (LineaLote item : lineasLote) {
			if (item.getCodigoPlanta().equals(codigo) && item.getCodigoProveedor() == id) {
				item.setCantidad(item.getCantidad() + cantidad);
				existe = true;
				break;
			}
		}

		if (!existe) {
			LineaLote lt = new LineaLote();

			lt.setCodigoProveedor((Long) session.getAttribute("codigoProveedor"));
			lt.setCodigoPlanta(codigo);
			lt.setCantidad(cantidad);
			lt.setUrgente((boolean) session.getAttribute("urgente"));

			lineasLote.add(lt);
		}

		session.setAttribute("lotesSession", lineasLote);
		model.addAttribute("lotesSession", lineasLote);
		model.addAttribute("success", "Producto añadido al lote con éxito.");

		return "redirect:/lote/verLotes";
	}

	// Confirmar una linea lote (antes de guardar)
	@GetMapping("/confirmarLineaLote/{codigoProveedor}/{codigoPlanta}")
	public String confirmarLineaLote(@PathVariable("codigoProveedor") String codigoProveedor,
			@PathVariable("codigoPlanta") String codigoPlanta, HttpSession session, Model model) {

		System.out.println("++++++confirmar codigoProveedor" + codigoProveedor);
		System.out.println("++++++confirmar codigoPlanta" + codigoPlanta);

		ArrayList<LineaLote> lista = (ArrayList<LineaLote>) session.getAttribute("lotesSession");

		ArrayList<LineaLote> listaConfirmada = (ArrayList<LineaLote>) session.getAttribute("listaConfirmada");
		if (listaConfirmada == null) {
			listaConfirmada = new ArrayList<>();
		}
		// Cambio del lote a confirmada
		if (lista != null) {
			for (LineaLote item : lista) {
				System.out.println("++++++1 añadir listaConfirmada ");
				if (item.getCodigoPlanta().equals(codigoPlanta)
						&& String.valueOf(item.getCodigoProveedor()).equals(codigoProveedor)) {
					System.out.println("++++++ añadir listaConfirmada " + item);
					listaConfirmada.add(item);
				}
			}
		}
		// borroar de l lote
		if (lista != null) {
			// borra el item que coincida con ambos códigos
			lista.removeIf(item -> item.getCodigoPlanta().equals(codigoPlanta)
					&& String.valueOf(item.getCodigoProveedor()).equals(codigoProveedor));
			session.setAttribute("lista", lista);
		}
		session.setAttribute("listaConfirmada", listaConfirmada);
		// System.out.println("++++++ listaConfirmada++"+ listaConfirmada);

		model.addAttribute("listaConfirmada", listaConfirmada);

		// model.addAttribute("proveedor",
		// proveedorRepo.findById(proveedor).orElse(null));
		// model.addAttribute("urgente", false);

		return "/personal/ConfirmarLote";

	}

	// Confirmar lote (antes de guardar)
	@GetMapping("/confirmarLote")
	public String confirmarLote(HttpSession session, Model model) {
		List<LineaLote> lineas = (List<LineaLote>) session.getAttribute("lotesSession");

		if (lineas == null) {
			model.addAttribute("error", "Debe seleccionar proveedor y añadir al menos una planta.");
			return "redirect:/lote/mostrarCrearLote";
		}

		model.addAttribute("lineas", lineas);
		// model.addAttribute("proveedor",
		// proveedorRepo.findById(proveedor).orElse(null));
		// model.addAttribute("urgente", urgente != null && urgente);

		return "/personal/ConfirmarLote";
	}

	@GetMapping("/retirarDeLote/{codigoProveedor}/{codigoPlanta}")
	public String retirarDeCesta(@PathVariable("codigoProveedor") String codigoProveedor,
			@PathVariable("codigoPlanta") String codigoPlanta, HttpSession session, Model model) {

		ArrayList<LineaLote> lista = (ArrayList<LineaLote>) session.getAttribute("lotesSession");

		if (lista != null) {
			// borra el item que coincida con ambos códigos
			lista.removeIf(item -> item.getCodigoPlanta().equals(codigoPlanta)
					&& String.valueOf(item.getCodigoProveedor()).equals(codigoProveedor));
			session.setAttribute("lista", lista);
		}

		Proveedor p = (Proveedor) session.getAttribute("proveedor");
		model.addAttribute("nombreProveedor", p.getNombre());
		model.addAttribute("lotesSession", lista);
		return "/personal/verLotes";

	}

	// Eliminar )
	@PostMapping("/eliminarlineaLote")
	public String eliminarPlanta(@RequestParam("index") int index, HttpSession session) {
		List<LineaLote> lineas = (List<LineaLote>) session.getAttribute("lineasLote");
		if (lineas != null && index >= 0 && index < lineas.size()) {
			lineas.remove(index);
			session.setAttribute("lineasLote", lineas);
		}
		return "redirect:/lote/confirmarLote";
	}

	// Guardar lote definitivo
	@PostMapping("/guardarLote")
	public String guardarLote(HttpSession session, Model model) {
		List<LineaLote> lineas = (List<LineaLote>) session.getAttribute("lineasLote");
		Long proveedor = (Long) session.getAttribute("codigoProveedor");
		Boolean urgente = (Boolean) session.getAttribute("urgente");

		if (lineas == null || lineas.isEmpty() || proveedor == null) {
			model.addAttribute("error", "Información incompleta del lote.");
			return "redirect:/lote/mostrarCrearLote";
		}

		loteServ.crearLoteDesdeLineas(proveedor, urgente != null && urgente, (ArrayList<LineaLote>) lineas);

		session.removeAttribute("lineasLote");
		session.removeAttribute("codigoProveedor");
		session.removeAttribute("urgente");

		model.addAttribute("exito", "Lote guardado correctamente.");
		return "redirect:/lote/mostrarCrearLote";
	}

	@GetMapping("/verLotes")
	public String verlotes(HttpSession session, Model model) {

		List<LineaLote> lineas = (List<LineaLote>) session.getAttribute("lotesSession");

		Proveedor p = (Proveedor) session.getAttribute("proveedor");
		model.addAttribute("nombreProveedor", p.getNombre());
		
		
		if (lineas == null || lineas.isEmpty()) {
			model.addAttribute("mensaje", "NO hay lotes.");
		} else {
			model.addAttribute("lotesSession", lineas);
		}

		return "/personal/verLotes";
	}

	@PostMapping("/SolicitarLote")
	public String solicitarLote(HttpSession session, Model model) {

		System.out.println("Solicitando lote...");

		List<LineaLote> lineas = (List<LineaLote>) session.getAttribute("listaConfirmada");

		Lote lote = new Lote();

		lote.setUrgente((boolean) session.getAttribute("urgente"));
		lote.setFechapeticion(LocalDateTime.now());
		lote.setPersona(personaServ.buscarPersonaPorUsuarioCredencial((String) session.getAttribute("usuario")));

		Optional<Proveedor> p = proveedorServ.buscarProveedorPorId((Long) session.getAttribute("codigoProveedor"));

		lote.setProveedor(p.get());

		lote.setEstado(Estado.NUEVO);

		loteServ.insertarLote(lote);

		Long idLote = lote.getId();

		for (LineaLote item : lineas) {
			LineaLote nl = new LineaLote();

			nl.setLote(loteServ.buscarLotesPorId(idLote).get());
			nl.setCodigoPlanta(item.getCodigoPlanta());
			nl.setCantidad(item.getCantidad());
			nl.setCodigoProveedor(item.getCodigoProveedor());

			lineasLoteServ.insertarlinealote(nl);

		}
		System.out.println("Id del lote: " + lote.getId());
		return "/personal/MenuPersonal";
	}

}
