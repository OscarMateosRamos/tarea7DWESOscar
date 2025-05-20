package com.oscar.vivero.repository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oscar.vivero.modelo.Mensaje;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

	@Query("SELECT m FROM Mensaje m WHERE m.persona.id = :idPersona")
	List<Mensaje> mensajesPorIdPersona(@Param("idPersona") Long idPersona);

	@Query("SELECT m FROM Mensaje m WHERE m.ejemplar.id = :idEjemplar ORDER BY fechahora")
	List<Mensaje> mensajesPorIdEjemplar(@Param("idEjemplar") Long id);

	@Query("SELECT m FROM Mensaje m WHERE m.ejemplar.planta.codigo=:tipoPlanta ORDER BY ejemplar")
	List<Mensaje> mensajesPorCodigoPlanta(@Param("tipoPlanta")String tipoPlanta);
//	
//	@Query("SELECT m FROM Mensaje m WHERE m.fechahora BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechahora DESC")
//List<Mensaje> filtrarMensajePorFechas(@Param("fechaInicio")Date fechaInicio,@Param("fechaFin")Date fechaFin);
	
	List<Mensaje> findByFechahoraBetween(Date startDate, Date endDate);

//	@Query("SELECT m FROM Mensaje m WHERE m.fechahora BETWEEN fechaInicial and fechaFinal")
//	List<Mensaje> mensajesPorFechas(String fechaInicial, String fechaFinal);

	List<Mensaje> findByEjemplarId(Long id);

}
