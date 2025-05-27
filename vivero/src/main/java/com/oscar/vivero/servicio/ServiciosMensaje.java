package com.oscar.vivero.servicio;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import java.text.ParseException;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.repository.MensajeRepository;

@Service
public class ServiciosMensaje {
	@Autowired
	MensajeRepository mensajerepo;

	@Transactional
	public void insertar(Mensaje m) {
		mensajerepo.saveAndFlush(m);
	}

	@Transactional
	public boolean validarFecha(String fecha) {

		String patron = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$";

		if (!Pattern.matches(patron, fecha)) {
			return false;
		}

		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
		formatoFecha.setLenient(false);
		try {
			formatoFecha.parse(fecha);
			return true; // Es válida
		} catch (ParseException e) {
			return false; // No es válida
		}
	}

	@Transactional
	public List<Mensaje> verTodosMensajesEjemplar() {
		List<Mensaje> mensajes = mensajerepo.findAll();

		for (Mensaje m : mensajes) {
			System.out.println("Id: " + m.getId() + " fecha: " + m.getFechahora() + " Mensaje : " + m.getMensaje()
					+ " Ejemplar: " + m.getEjemplar().getNombre() + " CREADO POR: " + m.getPersona().getNombre());
		}
		return mensajes;

	}

	@Transactional
	public List<Mensaje> verTodosMensajes() {
		List<Mensaje> mensajes = mensajerepo.findAll();

		for (Mensaje mensaje : mensajes) {
			if (mensaje.getEjemplar() == null) {
				System.out
						.println("Advertencia: mensaje con ID " + mensaje.getId() + " no tiene un ejemplar asociado.");
			} else {
				System.out.println("Mensaje con ejemplar: " + mensaje.getEjemplar().getNombre());
			}
		}

		return mensajes;
	}

	@Transactional
	public List<Mensaje> listamensajesPorIdEjemplar(Long id) {
		List<Mensaje> mensajes = mensajerepo.mensajesPorIdEjemplar(id);
		return mensajes;

	}

	@Transactional
	public List<Mensaje> listamensajesPorIdPersona(Long id) {
		List<Mensaje> mensajes = mensajerepo.mensajesPorIdPersona(id);
		return mensajes;

	}

	@Transactional
	public List<Mensaje> listamensajesPorCodigoPlanta(String tipoPlanta) {
		List<Mensaje> mensajes = mensajerepo.mensajesPorCodigoPlanta(tipoPlanta);
		return mensajes;

	}

	@Transactional
	public List<Mensaje> verMensajesRangoFechas(LocalDateTime fechaInicioParsed, LocalDateTime fechaFinParsed) {

		return mensajerepo.findByFechahoraBetween(fechaInicioParsed, fechaFinParsed);
	}

	public List<Mensaje> verPorIdEjemplar(Long id) {

		return mensajerepo.findByEjemplarId(id);
	}

}
