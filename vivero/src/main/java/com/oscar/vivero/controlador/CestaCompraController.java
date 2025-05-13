package com.oscar.vivero.controlador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.LineaPedido;
import com.oscar.vivero.servicio.ServiciosEjemplar;
import com.oscar.vivero.servicio.ServiciosPedido;
import com.oscar.vivero.servicio.ServiciosPlanta;

import jakarta.servlet.http.HttpSession;

@Controller
public class CestaCompraController {

	@Autowired
	private ServiciosPedido servPedido;

	@Autowired
	private ServiciosPlanta servPlanta;
	
	@Autowired
	private ServiciosEjemplar servEjemplar;

	@GetMapping("/CestaCompra")
	public String mostrarCesta(HttpSession session, Model model) {
		ArrayList<LineaPedido> lista = (ArrayList<LineaPedido>) session.getAttribute("lista");

		if (lista == null || lista.isEmpty()) {
			model.addAttribute("mensaje", "Tu cesta está vacía.");
		} else {
			model.addAttribute("lista", lista);
		}

		return "/cliente/CestaCompra";
	}

	@GetMapping("/retirarDeCesta/{codigoPlanta}")
	public String retirarDeCesta(@PathVariable("codigoPlanta") String codigo, HttpSession session, Model model) {
		ArrayList<LineaPedido> lista = (ArrayList<LineaPedido>) session.getAttribute("lista");
		String usuario = (String) session.getAttribute("usuario");

		if (lista != null && usuario != null) {
			lista.removeIf(item -> item.getCodigoPlanta().equals(codigo));
			session.setAttribute("lista", lista);
		}
		
		return "redirect:/CestaCompra";
	}

	@GetMapping("/ConfirmarPedido")
	public String confirmarPedido(HttpSession session, Model model) {
		String usuario = (String) session.getAttribute("usuario");
		
		if (usuario == null) {
			model.addAttribute("error", "Debes estar autenticado para realizar un pedido.");
			return "login";
		}

		ArrayList<LineaPedido> cestaCompra = (ArrayList<LineaPedido>) session.getAttribute("lista");

		if (cestaCompra == null || cestaCompra.isEmpty()) {
			model.addAttribute("mensaje", "No tienes productos en la cesta para confirmar.");
			return "ConfirmarPedido";
		}

		List<Ejemplar> ejemplaresSeleccionados = new ArrayList<>();

		for (LineaPedido item : cestaCompra) {

			List<Ejemplar> ejemplaresDisponibles = servEjemplar
					.obtenerEjemplaresDisponiblesPorPlanta(item.getCodigoPlanta());

			int cantidadRestante = item.getCantidad();

			for (Ejemplar ejemplar : ejemplaresDisponibles) {
				if (ejemplar.isDisponible() && cantidadRestante > 0) {
					ejemplar.setDisponible(false);
					ejemplaresSeleccionados.add(ejemplar);
					cantidadRestante--;
				}
			}
		}

		model.addAttribute("lista", cestaCompra);

		return "/cliente/ConfirmarPedido";
	}

}
