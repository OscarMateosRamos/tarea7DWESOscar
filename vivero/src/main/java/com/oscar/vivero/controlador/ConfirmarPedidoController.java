package com.oscar.vivero.controlador;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Cliente;
import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.LineaPedido;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.servicio.ServiciosCliente;
import com.oscar.vivero.servicio.ServiciosCredenciales;
import com.oscar.vivero.servicio.ServiciosEjemplar;
import com.oscar.vivero.servicio.ServiciosPedido;
import com.oscar.vivero.servicio.ServiciosPlanta;

import jakarta.servlet.http.HttpSession;

@Controller
public class ConfirmarPedidoController {

	@Autowired
	private ServiciosPedido pedidoserv;

	@Autowired
	private ServiciosCliente clienteserv;

	@Autowired
	private ServiciosPlanta plantaserv;

	@Autowired
	private ServiciosEjemplar ejemplarserv;

	@Autowired
	private ServiciosCredenciales credencialesserv;


	@PostMapping("/HacerPedido")
	public String HacerPedido(HttpSession session, Model model) {

		List<LineaPedido> lista = (List<LineaPedido>) session.getAttribute("lista");
		if (lista == null || lista.isEmpty()) {
			model.addAttribute("mensaje", "No tienes productos en la cesta para realizar un pedido.");
			return "redirect:/RealizarPedido";
		}

		String usuario = (String) session.getAttribute("usuario");

		Cliente cliente = clienteserv
				.buscarClientePorIdCredencial(credencialesserv.buscarCredencialPorUsuario(usuario).get().getId());

		Pedido pedido = new Pedido();

		pedido.setIdCliente(cliente.getId());
		pedido.setFecha(Date.valueOf(LocalDate.now()));
		
		
		pedidoserv.insertar(pedido);
		
		long idPedido = pedido.getId();
		
		System.out.println("ID DE PEDIDO"+idPedido);
		for (LineaPedido l : lista) {
			Planta planta = plantaserv.buscarPlantaPorCodigo(l.getCodigoPlanta());
			if (planta.getCantidadDisponible() < l.getCantidad()) {
				model.addAttribute("mensaje", "No hay suficientes existencias para " + planta.getNombrecomun());
				return "redirect:/RealizarPedido";
			}

			// System.out.println("Cantidad disponible" + planta.getCantidadDisponible());
			planta.setCantidadDisponible(planta.getCantidadDisponible() - l.getCantidad());

			plantaserv.modificarPlanta(planta);
			
			
//			System.out.println("Cantidad disponible" + planta.getCantidadDisponible());

			List<Ejemplar> ejemplares = ejemplarserv.listaejemplaresPorTipoPlanta(l.getCodigoPlanta());

			int contador = 0;

			for (Ejemplar ej : ejemplares) {
				if (ej.isDisponible() && contador < l.getCantidad()) {
					System.out.println("Ejemplar: " + ej.getNombre());
					ej.setDisponible(false);
					ej.setIdPedido(idPedido);
					ejemplarserv.modificarEjemplar(ej);
					contador++;
				}
			}

		}

		session.removeAttribute("lista");
		model.addAttribute("mensaje", "Pedido realizado con éxito.");
		return "/cliente/RealizarPedido";
	}

}
