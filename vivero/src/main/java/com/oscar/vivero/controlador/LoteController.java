package com.oscar.vivero.controlador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.Estado;
import com.oscar.vivero.modelo.FormularioLote;
import com.oscar.vivero.modelo.LineaLote;
import com.oscar.vivero.modelo.Lote;
import com.oscar.vivero.modelo.Planta;
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

	@GetMapping("/mostrarCrearLote")
	public String mostrarFormularioLote(HttpSession session, Model model) {

		ArrayList<LineaLote> lotesSesion = (ArrayList<LineaLote>) session.getAttribute("lotesSesion");

		if (lotesSesion == null || lotesSesion.isEmpty()) {
			lotesSesion = new ArrayList<LineaLote>();
			session.setAttribute("lotesSesion", lotesSesion);
		}

		List<Planta> plantas = plantaRepo.findAll();
		List<Proveedor> proveedores = proveedorRepo.findAll();

		List<LineaLote> lineas = plantas.stream().map(p -> {
			LineaLote l = new LineaLote();
			l.setCodigoPlanta(p.getCodigo());
			return l;
		}).collect(Collectors.toList());

		FormularioLote formulario = new FormularioLote();
		formulario.setLineas(lineas);

		model.addAttribute("loteFormulario", formulario);
		model.addAttribute("plantas", plantas);
		model.addAttribute("proveedores", proveedores);

		return "/personal/CrearLote";
	}

	@PostMapping("/guardarDatosLote")
	public String guardarLote(@ModelAttribute FormularioLote loteFormulario, HttpSession session, Model model) {
		ArrayList<LineaLote> lotesSesion = (ArrayList<LineaLote>) session.getAttribute("lotesSesion");

		Long proveedorId;
		boolean urgente;

		proveedorId = loteFormulario.getCodigoProveedor();
		urgente = loteFormulario.isUrgente();

		for (LineaLote linea : loteFormulario.getLineas()) {
			if (linea.getCantidad() > 0) {
				linea.setCodigoProveedor(proveedorId);
				Optional<Proveedor> p = proveedorServ.buscarProveedorPorId(proveedorId);
				linea.setProveedor(p.get());
				linea.setUrgente(urgente);

				boolean yaExiste = false;

				for (LineaLote existente : lotesSesion) {
					if (existente.getCodigoProveedor().equals(linea.getCodigoProveedor())
							&& existente.getCodigoPlanta().equals(linea.getCodigoPlanta())
							&& existente.isUrgente() == linea.isUrgente()) {

						// como en cada formulario max 50 limito la suma toal a 50....
						if (existente.getCantidad() + linea.getCantidad() > 50) {
							existente.setCantidad(50);
						} else {
							existente.setCantidad(existente.getCantidad() + linea.getCantidad());
						}
						yaExiste = true;
						break;
					}
				}

				if (!yaExiste) {
					lotesSesion.add(linea);
				}
			}
		}

		// se ordena los lotes por codigo de Proveedor y urgencia
		// para mostrarlos ordenados y agruparlos para hacer ditintos
		// lotes segun el Proveedor y la urgencia
		lotesSesion.sort(Comparator.comparing(LineaLote::getCodigoProveedor).thenComparing(LineaLote::isUrgente));

		session.setAttribute("lotesSesion", lotesSesion);

		return "/personal/verLotes";
	}

	
	@GetMapping("/retirarDeLote/{codigoProveedor}/{codigoPlanta}")
	public String retirarDeLote(@PathVariable("codigoProveedor") Long codigoProveedor,
			@PathVariable("codigoPlanta") String codigoPlanta, HttpSession session, Model model) {

		ArrayList<LineaLote> lista = (ArrayList<LineaLote>) session.getAttribute("lotesSesion");

		if (lista != null) {
			lista.removeIf(item -> item.getCodigoPlanta().equals(codigoPlanta)
					&& (item.getCodigoProveedor()) == (codigoProveedor));
			session.setAttribute("lotesSesion", lista);
		} else {
			model.addAttribute("error", "No hay datos de lotes");
		}
		return "/personal/verLotes";
	}

	@PostMapping("/actualizarCantidad")
	public String actualizarCantidadLote(@RequestParam("codigoProveedor") Long codigoProveedor,
			@RequestParam("codigoPlanta") String codigoPlanta, @RequestParam("cantidad") int cantidad,
			HttpSession session, Model model) {

		List<LineaLote> lotesSesion = (List<LineaLote>) session.getAttribute("lotesSesion");

		for (LineaLote lote : lotesSesion) {
			if (lote.getCodigoProveedor().equals(codigoProveedor) && lote.getCodigoPlanta().equals(codigoPlanta)) {
				lote.setCantidad(cantidad);
				model.addAttribute("mensaje", codigoPlanta +" cantidad modificada a "+ cantidad );
				break;
			}
		}

		session.setAttribute("lotesSesion", lotesSesion);
		model.addAttribute("proveedores", session.getAttribute("proveedores"));
		model.addAttribute("lotesSesion", lotesSesion);
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
	public String guardarLote(@RequestParam("codigoProveedor") long codigoProveedor,
			@RequestParam(value = "urgente", required = false) boolean urgente, HttpSession session, Model model) {
		Optional<Proveedor> p = proveedorServ.buscarProveedorPorId(codigoProveedor);

		if (p.isEmpty()) {
			model.addAttribute("error", "Proveedor no encontrado.");
			return "redirect:/lote/mostrarCrearLote";
		}

		Proveedor proveedor = p.get();

		session.setAttribute("codigoProveedor", codigoProveedor);
		session.setAttribute("proveedor", proveedor);
		session.setAttribute("urgente", urgente);

		model.addAttribute("codigoProveedor", codigoProveedor);
		model.addAttribute("nombreProveedor", proveedor.getNombre());
		model.addAttribute("plantas", plantaRepo.findAll());

		return "/personal/LineasLote";
	}

	@GetMapping("/verLotes")
	public String verlotes(HttpSession session, Model model) {
		List<LineaLote> lineas = (List<LineaLote>) session.getAttribute("lotesSesion");

		if (lineas == null || lineas.isEmpty()) {
			model.addAttribute("mensaje", "NO hay lotes.");
		} else {
			//model.addAttribute("lotesSesion", lineas);
		}

		return "/personal/verLotes";
	}

	
	@PostMapping("/borrarTodo")
	public String borrarTodo(HttpSession session,Model model) {
		session.removeAttribute("lotesSesion");
		session.removeAttribute("proveedor");
		session.removeAttribute("codigoProveedor");
		session.removeAttribute("urgente");
		model.addAttribute("mensaje", "Se han borrado los lotes");
		return "/personal/verLotes";
	}

	// Confirmar= solicitar ell lote....
	@PostMapping("/SolicitarLote")
	public String solicitarLote(HttpSession session, Model model) {
		System.out.println("Solicitando lote...");

		List<LineaLote> lineas = (List<LineaLote>) session.getAttribute("lotesSesion");

		Boolean urgente = (Boolean) session.getAttribute("urgenteSesion");

		Map<Long, Map<Boolean, List<LineaLote>>> lineasPorProveedorYUrgencia = lineas.stream().collect(
				Collectors.groupingBy(LineaLote::getCodigoProveedor, Collectors.groupingBy(LineaLote::isUrgente)));

		for (Map.Entry<Long, Map<Boolean, List<LineaLote>>> entry : lineasPorProveedorYUrgencia.entrySet()) {
			Long proveedorId = entry.getKey();
			Map<Boolean, List<LineaLote>> lineasPorUrgencia = entry.getValue();

			for (Map.Entry<Boolean, List<LineaLote>> urgenciaEntry : lineasPorUrgencia.entrySet()) {
				Boolean esUrgente = urgenciaEntry.getKey();
				List<LineaLote> lineasProveedor = urgenciaEntry.getValue();

				Optional<Proveedor> proveedorOpt = proveedorServ.buscarProveedorPorId(proveedorId);
				if (!proveedorOpt.isPresent()) {
					System.out.println("Proveedor no encontrado: " + proveedorId);
					continue;
				}

				Lote lote = new Lote();
				lote.setUrgente(esUrgente != null && esUrgente);
				lote.setFechapeticion(LocalDateTime.now());

				String usuario = (String) session.getAttribute("usuario");
				lote.setPersona(personaServ.buscarPersonaPorUsuarioCredencial(usuario));
				lote.setProveedor(proveedorOpt.get());
				lote.setEstado(Estado.NUEVO);

				loteServ.insertarLote(lote);
				Long idLote = lote.getId();
				System.out.println("Nuevo lote creado con ID: " + idLote + " para proveedor: "
						+ proveedorOpt.get().getNombre() + " (Urgente: " + esUrgente + ")");

				for (LineaLote item : lineasProveedor) {
					LineaLote nl = new LineaLote();
					nl.setLote(lote);
					nl.setCodigoPlanta(item.getCodigoPlanta());
					nl.setCantidad(item.getCantidad());
					nl.setCodigoProveedor(item.getCodigoProveedor());

					lineasLoteServ.insertarlinealote(nl);
				}
			}
		}
		session.removeAttribute("urgenteSesion");
		session.removeAttribute("codigoProveedorSesion");
		session.removeAttribute("lotesSesion");

		return "/personal/MenuPersonal";
	}
	
	
	
	
	
	
	
	// Añadir planta (línea lote) a la lista en sesión
//	@PostMapping("/añadirALote")
//	public String añadirALote(@RequestParam("codigo") String codigo, @RequestParam("cantidad") int cantidad,
//
//			HttpSession session, Model model) {
//
//		List<LineaLote> lineasLote = (List<LineaLote>) session.getAttribute("lotesSesion");
//		if (lineasLote == null) {
//			System.out.println("+++++++++++");
//			lineasLote = new ArrayList<>();
//		}
//
//		boolean existe = false;
//		Long id = (Long) session.getAttribute("codigoProveedor");
//
//		System.out.println("session.getAttribute( codigoProveedor )" + session.getAttribute("codigoProveedor"));
//
//		for (LineaLote item : lineasLote) {
//			if (item.getCodigoPlanta().equals(codigo) && item.getCodigoProveedor() == id) {
//				item.setCantidad(item.getCantidad() + cantidad);
//				existe = true;
//				break;
//			}
//		}
//
//		if (!existe) {
//			LineaLote lt = new LineaLote();
//
//			lt.setCodigoProveedor((Long) session.getAttribute("codigoProveedor"));
//			lt.setCodigoPlanta(codigo);
//			lt.setCantidad(cantidad);
//			lt.setUrgente((boolean) session.getAttribute("urgente"));
//
//			lineasLote.add(lt);
//		}
//
//		session.setAttribute("lotesSesion", lineasLote);
//		model.addAttribute("lotesSesion", lineasLote);
//		model.addAttribute("success", "Producto añadido al lote con éxito.");
//
//		return "redirect:/lote/verLotes";
//	}

	// Confirmar una linea lote (antes de guardar)
//	@GetMapping("/confirmarLineaLote/{codigoProveedor}/{codigoPlanta}")
//	public String confirmarLineaLote(@PathVariable("codigoProveedor") String codigoProveedor,
//			@PathVariable("codigoPlanta") String codigoPlanta, HttpSession session, Model model) {
//
//		System.out.println("++++++confirmar codigoPlanta" + codigoPlanta);
//
//		ArrayList<LineaLote> lista = (ArrayList<LineaLote>) session.getAttribute("lotesSesion");
//
//		ArrayList<LineaLote> listaConfirmada = (ArrayList<LineaLote>) session.getAttribute("listaConfirmada");
//		if (listaConfirmada == null) {
//			listaConfirmada = new ArrayList<>();
//		}
//
//		if (lista != null) {
//			for (LineaLote item : lista) {
//				System.out.println("++++++1 añadir listaConfirmada ");
//				if (item.getCodigoPlanta().equals(codigoPlanta)
//						&& String.valueOf(item.getCodigoProveedor()).equals(codigoProveedor)) {
//					System.out.println("++++++ añadir listaConfirmada " + item);
//					listaConfirmada.add(item);
//				}
//			}
//		}
//		// borroar de l lote
//		if (lista != null) {
//			lista.removeIf(item -> item.getCodigoPlanta().equals(codigoPlanta)
//					&& String.valueOf(item.getCodigoProveedor()).equals(codigoProveedor));
//			session.setAttribute("lista", lista);
//		}
//		session.setAttribute("listaConfirmada", listaConfirmada);
//		// System.out.println("++++++ listaConfirmada++"+ listaConfirmada);
//
//		model.addAttribute("listaConfirmada", listaConfirmada);
//
//		return "/personal/ConfirmarLote";
//
//	}

	// Confirmar lote
//	@GetMapping("/confirmarLote")
//	public String confirmarLote(HttpSession session, Model model) {
//		List<LineaLote> lineas = (List<LineaLote>) session.getAttribute("lotesSesion");
//
//		if (lineas == null) {
//			model.addAttribute("error", "Debe seleccionar proveedor y añadir al menos una planta.");
//			return "redirect:/lote/mostrarCrearLote";
//		}
//
//		model.addAttribute("lineas", lineas);
//
//		return "/personal/ConfirmarLote";
//	}

	
}
