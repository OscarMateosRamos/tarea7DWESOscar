package com.oscar.vivero.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscar.vivero.modelo.Lote;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {
	ArrayList<Lote> findByFecharecepcionIsNull();

	ArrayList<Lote> findByFecharecepcionIsNotNull();

}
