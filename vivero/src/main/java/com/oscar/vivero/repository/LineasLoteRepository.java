package com.oscar.vivero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscar.vivero.modelo.LineaLote;

@Repository
public interface LineasLoteRepository extends JpaRepository<LineaLote, Long> {

}
