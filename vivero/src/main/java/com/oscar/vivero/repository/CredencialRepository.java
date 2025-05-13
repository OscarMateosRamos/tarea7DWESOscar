package com.oscar.vivero.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oscar.vivero.modelo.Credenciales;

@Repository
public interface CredencialRepository extends JpaRepository<Credenciales, Long> {

	Optional<Credenciales> findByUsuario(String usuario);

	boolean existsByUsuario(String usuario);


}