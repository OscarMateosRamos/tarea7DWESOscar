package com.oscar.vivero.servicio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Pedido;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.repository.PedidoRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ServiciosPedido {

	@Autowired
	PedidoRepository pedidorepo;

	@Autowired
	HttpSession session;

	public void insertarPedido(Pedido p) {
		pedidorepo.saveAndFlush(p);
	}

	public Pedido obtenerUltimoPedido() {
		return pedidorepo.findTopByOrderByFechaDesc();
	}

	public void agregarPedidoACesta(Pedido pedido) {

		List<Pedido> cesta = (List<Pedido>) session.getAttribute("cesta");

		if (cesta == null) {
			cesta = new ArrayList<>();
		}

		cesta.add(pedido);

		session.setAttribute("cesta", cesta);
	}

	public Pedido obtenerPedidoPorId(Long idPedido) {

		return pedidorepo.findById(idPedido).orElse(null);
	}

	public String contarEjemplaresDisponibles(List<Planta> plantas) {
		StringBuilder disponibilidad = new StringBuilder();

		for (Planta planta : plantas) {

			if (planta.getEjemplares() != null && !planta.getEjemplares().isEmpty()) {

				int cantidadDisponible = planta.getEjemplares().size();
				disponibilidad.append("Planta: ").append(planta.getNombrecomun()).append(" - Ejemplares disponibles: ")
						.append(cantidadDisponible).append("\n");
			} else {

				disponibilidad.append("Planta: ").append(planta.getNombrecomun())
						.append(" - No hay ejemplares disponibles\n");
			}
		}

		return disponibilidad.toString();
	}

	private List<Ejemplar> ejemplaresEnPedido = new ArrayList<>();

	public List<Ejemplar> getEjemplaresEnPedido() {
		return ejemplaresEnPedido;
	}

	public void insertar(Pedido p) {
		pedidorepo.saveAndFlush(p);
	}
	
}
