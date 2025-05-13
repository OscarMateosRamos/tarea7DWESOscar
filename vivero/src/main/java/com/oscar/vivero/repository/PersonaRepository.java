package com.oscar.vivero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscar.vivero.modelo.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

	List<Persona> findByEmail(String email);

}
