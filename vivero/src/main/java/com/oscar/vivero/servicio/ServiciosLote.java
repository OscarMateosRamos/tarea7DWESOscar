package com.oscar.vivero.servicio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.LineaLote;
import com.oscar.vivero.modelo.Lote;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.modelo.Proveedor;
import com.oscar.vivero.repository.LoteRepository;
import com.oscar.vivero.repository.PlantaRepository;
import com.oscar.vivero.repository.ProveedorRepository;

@Service
public class ServiciosLote {
	@Autowired
	private ProveedorRepository proveedorRepo;
	@Autowired
	private PlantaRepository plantaRepo;
	@Autowired
	private LoteRepository loteRepo;

	public void insertarLote(Lote lt) {
		loteRepo.saveAndFlush(lt);
	}

	public void crearLoteDesdeLineas(long codigoProveedor, boolean urgente, ArrayList<LineaLote> lineas) {
		Proveedor proveedor = proveedorRepo.findById(codigoProveedor).orElse(null);
		if (proveedor == null)
			return;

		Lote lote = new Lote();
		lote.setProveedor(proveedor);
		lote.setUrgente(urgente);
		lote.setFechapeticion(LocalDateTime.now());

		ArrayList<Ejemplar> ejemplares = new ArrayList<>();

		for (LineaLote linea : lineas) {
			Planta planta = (Planta) plantaRepo.findByCodigo(linea.getCodigoPlanta());
			if (planta != null) {
				for (int i = 0; i < linea.getCantidad(); i++) {
					Ejemplar e = new Ejemplar();
					e.setPlanta(planta);
					ejemplares.add(e);
				}
			}
		}

		lote.setEjemplares(ejemplares);
		loteRepo.save(lote);
	}

	public ArrayList<Lote> buscarLotesRecibidos() {
		return loteRepo.findByFecharecepcionIsNotNull();

	}

	public ArrayList<Lote> buscarLotesNoRecibidos() {
		return loteRepo.findByFecharecepcionIsNull();

	}

	public Optional<Lote> buscarLotesPorId(Long id) {
		return loteRepo.findById(id);
	}

	public Optional<Lote> buscarLotesPorProveedor(Proveedor p) {
		return loteRepo.findByProveedor(p);
	}

}
