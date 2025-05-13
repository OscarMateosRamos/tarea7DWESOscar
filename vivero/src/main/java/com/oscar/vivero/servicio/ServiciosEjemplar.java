package com.oscar.vivero.servicio;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.modelo.Persona;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.repository.EjemplarRepository;
import com.oscar.vivero.repository.PlantaRepository;

import jakarta.transaction.Transactional;

@Service
public class ServiciosEjemplar {

	@Autowired
	EjemplarRepository ejemplarrepo;

	@Autowired
	PlantaRepository plantarrepo;

	@Autowired
	ServiciosPersona servPersona;

	@Autowired
	ServiciosMensaje servMensaje;

	@Autowired
	ServiciosPlanta servPlanta;

	@Autowired
	Controlador controlador;

	@Transactional
	public void insertarEjemplar(Ejemplar ej) {
		ejemplarrepo.saveAndFlush(ej);

		Optional<Ejemplar> existenteEjemplar = ejemplarrepo.findByNombre(ej.getNombre());

		if (existenteEjemplar.isPresent()) {

			Ejemplar existing = existenteEjemplar.get();
			String nuevoNombre = ej.getNombre() + "_" + existing.getId();
			ej.setNombre(nuevoNombre);
			ejemplarrepo.saveAndFlush(ej);
		}

		Mensaje m = new Mensaje();
		LocalDate fechahora = LocalDate.now();
		Date date = Date.valueOf(fechahora);

		Optional<Persona> p = servPersona.buscarPorId(1L);
		if (p.isPresent()) {
			m.setPersona(p.get());
		}

		String mensaje = "Añadido nuevo ejemplar " + ej.getNombre() + " por " + controlador.getUsername() + " ("
				+ fechahora + " ).";
		m.setEjemplar(ej);
		m.setFechahora(date);
		m.setMensaje(mensaje);

		Planta planta = ej.getPlanta();
		planta.setCantidadDisponible(planta.getCantidadDisponible() + 1);

		servPlanta.modificarPlanta(planta);

		servMensaje.insertar(m);
	}

	@Transactional
	public List<Ejemplar> listaejemplaresPorTipoPlanta(String codigo) {
		return ejemplarrepo.ejemplaresPorTipoPlanta(codigo);
	}

	@Transactional
	public void modificarEjemplar(Ejemplar ej) {
		ejemplarrepo.saveAndFlush(ej);
	}

	@Transactional
	public Ejemplar buscarPorNombre(String nombre) {
		return ejemplarrepo.findByNombre(nombre).orElse(null);
	}

	@Transactional
	public boolean existeIdEjemplar(Long id) {
		return ejemplarrepo.existsById(id);
	}

	@Transactional
	public boolean existeNombreEjemplar(String nombre) {
		return ejemplarrepo.existsByNombre(nombre);
	}

	@Transactional
	public List<Ejemplar> vertodosEjemplares() {
		return ejemplarrepo.findAll();
	}

	@Transactional
	public Ejemplar buscarPorId(Long ejemplarCodigo) {
		Optional<Ejemplar> ejemplarOptional = ejemplarrepo.findById(ejemplarCodigo);
		if (ejemplarOptional.isPresent()) {
			Ejemplar ejemplar = ejemplarOptional.get();

			if (!ejemplar.getMensajes().isEmpty()) {
				Hibernate.initialize(ejemplar.getMensajes());
			}
			return ejemplar;
		}
		return null;
	}

	@Transactional
	public String obtenerNombrePorId(Long ejemplarCodigo) {
		Optional<Ejemplar> ejemplarOptional = ejemplarrepo.findById(ejemplarCodigo);

		if (ejemplarOptional.isPresent()) {
			return ejemplarOptional.get().getNombre();
		}

		return null;
	}

	public void actualizarEjemplarAlRealizarPedido(Ejemplar ejemplar, String mensaje) {
		ejemplar.setDisponible(false);
		ejemplarrepo.save(ejemplar);
	}

	public List<Ejemplar> obtenerEjemplaresDisponiblesPorPlanta(String codigoPlanta) {
		return ejemplarrepo.findByPlantaCodigoAndDisponibleTrue(codigoPlanta);
	}

	public List<Ejemplar> obtenerEjemplaresConCantidadDisponible() {

		return ejemplarrepo.findAll();
	}
}
