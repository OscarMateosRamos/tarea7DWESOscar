package com.oscar.vivero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscar.vivero.modelo.Proveedor;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

	List<Proveedor> findByCif(String cif);

}
