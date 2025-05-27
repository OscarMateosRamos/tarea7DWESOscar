package com.oscar.vivero.controlador;

import java.sql.Date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Cliente;
import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.LineaPedido;
import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.servicio.ServiciosCliente;
import com.oscar.vivero.servicio.ServiciosCredenciales;
import com.oscar.vivero.servicio.ServiciosEjemplar;
import com.oscar.vivero.servicio.ServiciosMensaje;
import com.oscar.vivero.servicio.ServiciosPedido;
import com.oscar.vivero.servicio.ServiciosPlanta;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class ConfirmarPedidoController {

	private final ServiciosMensaje serviciosMensaje;

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

	ConfirmarPedidoController(ServiciosMensaje serviciosMensaje) {
		this.serviciosMensaje = serviciosMensaje;
	}

	@Transactional
	@PostMapping("/HacerPedido")
	public String HacerPedido(HttpSession session, Model model) {

		List<LineaPedido> lista = (List<LineaPedido>) session.getAttribute("lista");
		if (lista == null || lista.isEmpty()) {
			model.addAttribute("mensaje", "No tienes productos en la cesta para realizar un pedido.");
			return "redirect:/RealizarPedido";
		}

		String usuario = (String) session.getAttribute("usuario");
		Optional<Credenciales> credOpt = credencialesserv.buscarCredencialPorUsuario(usuario);
		if (!credOpt.isPresent()) {
			model.addAttribute("mensaje", "Usuario no válido.");
			return "redirect:/RealizarPedido";
		}

		Cliente cliente = clienteserv.buscarClientePorIdCredencial(credOpt.get().getId());

		Pedido pedido = new Pedido();
		pedido.setIdCliente(cliente.getId());
		pedido.setFecha(Date.valueOf(LocalDate.now()));
		pedidoserv.insertar(pedido);

		long idPedido = pedido.getId();
		System.out.println("ID DE PEDIDO: " + idPedido);

		Iterator<LineaPedido> iter = lista.iterator();
		while (iter.hasNext()) {
			LineaPedido l = iter.next();
			Planta planta = plantaserv.buscarPlantaPorCodigo(l.getCodigoPlanta());

			if (planta.getCantidadDisponible() < l.getCantidad()) {
				model.addAttribute("mensaje", "No hay suficientes existencias para " + planta.getNombrecomun());
				return "redirect:/RealizarPedido";
			}

			planta.setCantidadDisponible(planta.getCantidadDisponible() - l.getCantidad());
			plantaserv.modificarPlanta(planta);

			List<Ejemplar> ejemplares = ejemplarserv.listaejemplaresPorTipoPlanta(l.getCodigoPlanta());
			int contador = 0;

			for (Ejemplar ej : ejemplares) {
				if (ej.isDisponible() && contador < l.getCantidad()) {
					ej.setDisponible(false);
					ej.setIdPedido(idPedido);
					ejemplarserv.modificarEjemplar(ej);
					contador++;

					LocalDateTime fecha = LocalDateTime.now();
					Mensaje anotacion = new Mensaje();
					anotacion.setFechahora(fecha);
					anotacion.setEjemplar(ej);
					anotacion.setPedido(pedido);

					String msg = String.format("El ejemplar %s fue comprado por %s en el pedido %d en la fecha %s",
							ej.getNombre(), cliente.getNombre(), idPedido, fecha.toString());

					anotacion.setMensaje(msg);
					serviciosMensaje.insertar(anotacion);
				}
			}
		}

		lista.clear();

		model.addAttribute("mensaje", "Pedido realizado con éxito.");
		return "/cliente/RealizarPedido";
	}

}
