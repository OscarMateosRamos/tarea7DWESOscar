package com.oscar.vivero.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscar.vivero.modelo.Lote;
import com.oscar.vivero.modelo.Proveedor;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {
	ArrayList<Lote> findByFecharecepcionIsNull();

	ArrayList<Lote> findByFecharecepcionIsNotNull();
	
	Optional<Lote> findById(Long id);

	Optional<Lote> findByProveedor(Proveedor p);

}
