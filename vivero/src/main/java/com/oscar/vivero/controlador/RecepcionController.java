package com.oscar.vivero.controlador;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.LineaLote;
import com.oscar.vivero.modelo.Lote;
import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.modelo.Persona;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.repository.EjemplarRepository;
import com.oscar.vivero.repository.PersonaRepository;
import com.oscar.vivero.servicio.ServiciosCredenciales;
import com.oscar.vivero.servicio.ServiciosEjemplar;
import com.oscar.vivero.servicio.ServiciosLote;
import com.oscar.vivero.servicio.ServiciosPlanta;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/recepcion")
public class RecepcionController {

	@Autowired
	ServiciosLote servlote;

	@Autowired
	ServiciosEjemplar servEjemplar;

	@Autowired
	ServiciosPlanta servPlanta;

	@Autowired
	ServiciosCredenciales servCred;

	@Autowired
	EjemplarRepository ejemplarRepo;

	@Autowired
	PersonaRepository personaRepo;

	@Transactional
	@GetMapping("/info")
	public String info(HttpSession session, Model model) {

		ArrayList<Lote> lotesRecibidos = servlote.buscarLotesRecibidos();
		ArrayList<Lote> lotesNoRecibidos = servlote.buscarLotesNoRecibidos();
		ArrayList<Ejemplar> ejemplares = (ArrayList<Ejemplar>) servEjemplar.vertodosEjemplares();

		lotesRecibidos.forEach(l -> {
			if (l.getLineasLote() != null)
				l.getLineasLote().size();
		});
		lotesNoRecibidos.forEach(l -> {
			if (l.getLineasLote() != null)
				l.getLineasLote().size();
		});

		System.out.println("++++++Lotes recibidos+++++ " + lotesRecibidos.size());
		System.out.println("++++++Lotes no recibidos+++++ " + lotesNoRecibidos.size());

		model.addAttribute("ejemplares", ejemplares);
		model.addAttribute("lotesRecibidos", lotesRecibidos);
		model.addAttribute("lotesNoRecibidos", lotesNoRecibidos);

		return "personal/infolotes";
	}

	@Transactional
	@GetMapping("/recepcionLote/{idLote}/")
	public String recepcionLote(@PathVariable("idLote") Long idLote, HttpSession session) {
	    Optional<Lote> loteOpt = servlote.buscarLotesPorId(idLote);
	    if (loteOpt.isEmpty()) {
	        return "redirect:/recepcion/info?error=LoteNoEncontrado";
	    }

	    Lote lote = loteOpt.get();
	    LocalDateTime now = LocalDateTime.now();
	    lote.setFecharecepcion(now);

	    String usuario = (String) session.getAttribute("usuario");

	    for (LineaLote linea : lote.getLineasLote()) {
	        for (int i = 0; i < linea.getCantidad(); i++) {

	            int numeroEjemplar = ejemplarRepo.findAll().size() + 1;

	            String nuevoNombre = linea.getCodigoPlanta().toUpperCase() + "_" + numeroEjemplar;

	            Ejemplar ej = new Ejemplar();
	            ej.setNombre(nuevoNombre);

	            Mensaje mensaje = new Mensaje();
	            DateTimeFormatter formatoLocalDate = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm:ss");

	            mensaje.setMensaje(
	                "Ejemplar " + nuevoNombre + " recibido el " + now.format(formatoLocalDate)
	                + " en el lote " + lote.getId()
	                + " del proveedor " + lote.getProveedor().getNombre()
	                + " solicitado por " + lote.getPersona().getNombre()
	                + " y confirmado por " + usuario
	            );

	            mensaje.setFechahora(now); 

	            for (Persona p : personaRepo.findAll()) {
	                if (p.getCredencial().getId().equals(servCred.buscarCredencialPorUsuario(usuario).get().getId())) {
	                    System.out.println("Encontrado el id persona y el id Credencial");
	                    mensaje.setPersona(p);
	                    lote.setRecepcionista(p);
	                }
	            }

	            ej.setLote(lote);
	            ej.setPlanta(servPlanta.buscarPlantaPorCodigo(linea.getCodigoPlanta()));
	            ej.setMensajes(new ArrayList<>());
	            ej.getMensajes().add(mensaje);

	            servEjemplar.insertarEjemplar(ej);

	            Planta planta = servPlanta.buscarPlantaPorCodigo(linea.getCodigoPlanta());
	            planta.setCantidadDisponible(planta.getCantidadDisponible() + 1);
	            servPlanta.modificarPlanta(planta);
	        }
	    }

	    session.setAttribute("mensajeExito", "Lote " + lote.getId() + " recibido correctamente.");

	    return "redirect:/recepcion/info";
	}


}
