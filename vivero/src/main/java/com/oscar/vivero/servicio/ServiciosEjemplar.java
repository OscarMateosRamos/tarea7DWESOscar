package com.oscar.vivero.servicio;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Ejemplar;
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

	public void insertarEjemplar(Ejemplar ej) {
		ejemplarrepo.saveAndFlush(ej);
	}

	@Transactional
	public List<Ejemplar> listaejemplaresPorTipoPlanta(List<String> codigos) {
	    return ejemplarrepo.ejemplaresPorTiposPlanta(codigos);
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
