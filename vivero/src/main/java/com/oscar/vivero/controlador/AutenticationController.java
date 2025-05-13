package com.oscar.vivero.controlador;

import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.servicio.ServiciosCredenciales;
import com.oscar.vivero.servicio.ServiciosPlanta;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AutenticationController {

	private final Logger log = (Logger) LoggerFactory.getLogger(MainController.class);

	@Autowired
	ServiciosPlanta servPlanta;

	@Autowired
	ServiciosCredenciales servCredenciales;

	@GetMapping("/redireccionar")
	public String redireccionar(Authentication authentication, HttpSession session) {
		log.info("**/auth/redireccionar:authentication.getName()= " + authentication.getName());
		log.info("**/auth/redireccionar:authentication.getAuthorities()= " + authentication.getAuthorities());
		log.info("**/auth/redireccionar:= session.getAttribute(\"usuario\")" + session.getAttribute("usuario"));

		session.setAttribute("usuario", authentication.getName());

		log.info("Asignado a sesion /auth/redireccionar:= " + session.getAttribute("usuario"));

		for (GrantedAuthority authority : authentication.getAuthorities()) {
			String rol = authority.getAuthority();

			switch (rol) {
			case "ROLE_ADMIN":
				session.setAttribute("rol", "ADMIN");
				return "/admin/MenuAdmin";
			case "ROLE_PERSONAL":
				session.setAttribute("rol", "PERSONAL");
				return "/personal/MenuPersonal";
			case "ROLE_CLIENTE":
				session.setAttribute("rol", "CLIENTE");
				return "/cliente/Menucliente";
			default:
				return "inicio";
			}
		}

		return "inicio";
	}

	@PostMapping("/leerlogin")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password,
			Model model) {

		// Verifica si los campos están vacíos
		if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
			model.addAttribute("error", "Por favor, ingrese ambos campos.");
			return "redirect:/inicio?error=Por%20favor,%20ingrese%20ambos%20campos.";
		}

		// Verifica las credenciales
		Optional<Credenciales> credencial = servCredenciales.buscarCredencialPorUsuario(username);
		if (!credencial.isPresent() || !new BCryptPasswordEncoder().matches(password, credencial.get().getPassword())) {
			model.addAttribute("error", "Credenciales incorrectas");
			return "redirect:/inicio?error=Credenciales%20incorrectas";
		}

		// Si todo es correcto, redirige a la página de inicio (o el destino deseado)
		return "redirect:/inicio";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		System.out.println("Cerrando sesión: " + session.getAttribute("usuario"));
		session.removeAttribute("usuario");
		session.removeAttribute("rol");
		session.removeAttribute("lista");
		session.invalidate();
		System.out.println("Sesión invalidada.");
		return "redirect:/inicio";
	}

	@GetMapping("/prohibido")
	public String prohibido() {

		return "prohibido";

	}

}