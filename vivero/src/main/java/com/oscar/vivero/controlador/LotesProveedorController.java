package com.oscar.vivero.controlador;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Estado;
import com.oscar.vivero.modelo.Lote;
import com.oscar.vivero.modelo.Proveedor;
import com.oscar.vivero.servicio.ServiciosCredenciales;
import com.oscar.vivero.servicio.ServiciosEjemplar;
import com.oscar.vivero.servicio.ServiciosLote;
import com.oscar.vivero.servicio.ServiciosProveedor;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/lotesproveedor")
public class LotesProveedorController {

	@Autowired
	ServiciosLote servlote;

	@Autowired
	ServiciosEjemplar servEjemplar;

	@Autowired
	ServiciosCredenciales servCred;

	@Autowired
	ServiciosProveedor servProveedor;

	@Autowired
	ServiciosLote lotesServ;

	@Transactional
	@GetMapping("/verLotesProveedor")
	public String verlotesproveedor(HttpSession session, Model model) {

		String usuario = (String) session.getAttribute("usuario");

		System.out.println("+++Usuario: " + usuario);

		Optional<Credenciales> cr = servCred.buscarCredencialPorUsuario(usuario);
		System.out.println("+++Usuario Credenciales: " + cr.get().getId());

		Optional<Proveedor> proveedor = servProveedor.buscarProveedorPorIdCredencial(cr.get().getId());
		System.out.println("+++proveedor: " + proveedor.get().getId());

		// Optional<Lote> lotesProveedor =
		// servlote.buscarLotesPorProveedor(proveedor.get());

		ArrayList<Lote> lotesRecibidos = servlote.buscarLotesRecibidos();

		ArrayList<Lote> lotesRecibidosProveedor = new ArrayList<Lote>();

		for (Lote l : lotesRecibidos) {
			if (l.getProveedor().equals(proveedor.get())) {
				lotesRecibidosProveedor.add(l);
			}
		}
		ArrayList<Lote> lotesNoRecibidos = servlote.buscarLotesNoRecibidos();

		ArrayList<Lote> lotesNoRecibidosProveedor = new ArrayList<Lote>();

		for (Lote l : lotesNoRecibidos) {
			if (l.getProveedor().equals(proveedor.get())) {
				lotesNoRecibidosProveedor.add(l);
			}
		}

		ArrayList<Ejemplar> ejemplares = (ArrayList<Ejemplar>) servEjemplar.vertodosEjemplares();

		lotesRecibidosProveedor.forEach(l -> {
			if (l.getLineasLote() != null)
				l.getLineasLote().size();
		});

		lotesNoRecibidosProveedor.forEach(l -> {
			if (l.getLineasLote() != null)
				l.getLineasLote().size();
		});

		System.out.println("++++++Lotes no recibidos+++++ " + lotesNoRecibidosProveedor.size());
		System.out.println("++++++Lotes  recibidos+++++ " + lotesRecibidosProveedor.size());

		model.addAttribute("ejemplares", ejemplares);
		model.addAttribute("lotesRecibidos", lotesRecibidosProveedor);
		model.addAttribute("lotesNoRecibidos", lotesNoRecibidosProveedor);

		return "/proveedor/lotesproveedor";
	}

	@PostMapping("/cancelar/{idLote}")
	public String cancelarLote(@PathVariable("idLote") Long idLote, Model model) {
		System.out.println("Cancelando lote: " + idLote);
		Optional<Lote> l = lotesServ.buscarLotesPorId(idLote);

		if (l.isPresent()) {
			Lote lote = l.get();
			lote.setEstado(Estado.CANCELADO);
			lotesServ.insertarLote(lote);
		}

		return "redirect:/lotesproveedor/verLotesProveedor";
	}

}
