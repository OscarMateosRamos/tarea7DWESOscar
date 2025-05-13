package com.oscar.vivero.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Planta;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {

	@Query("SELECT e FROM Ejemplar e WHERE e.planta.codigo = :codigoPlanta")
	List<Ejemplar> ejemplaresPorTipoPlanta(@Param("codigoPlanta") String codigoPlanta);

	Optional<Ejemplar> findByNombre(String nombre);

	boolean existsByNombre(String nombre);

	@Query("SELECT e FROM Ejemplar e LEFT JOIN FETCH e.mensajes WHERE e.id = :id")
	Ejemplar findByIdWithMensajes(@Param("id") Long id);

	List<Ejemplar> findByPlantaCodigoAndDisponibleTrue(String codigoPlanta);
	
	
	List<Ejemplar> findEjemplarById(Long ejemplarId) ;

	List<Ejemplar> findByPlanta(Planta planta);

	long countByPlantaAndDisponible(Planta planta, boolean b);
	
	
}
