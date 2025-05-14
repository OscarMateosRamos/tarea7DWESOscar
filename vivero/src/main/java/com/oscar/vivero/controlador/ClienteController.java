package com.oscar.vivero.controlador;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oscar.vivero.modelo.Cliente;
import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.servicio.ServiciosCliente;
import com.oscar.vivero.servicio.ServiciosCredenciales;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	ServiciosCliente servcliente;

	@Autowired
	ServiciosCredenciales servCredenciales;

	@PostMapping("/CamposCliente")
	public String RegistrarCliente(@ModelAttribute Cliente RegistroCliente, Model model) {

		String nombre = RegistroCliente.getNombre();
		Date fechanac = (Date) RegistroCliente.getFechanac();
		String nif = RegistroCliente.getNif();
		String direccion = RegistroCliente.getDireccion();
		String email = RegistroCliente.getEmail();
		String telefono = RegistroCliente.getTelefono();

		String usuario = RegistroCliente.getCredencial().getUsuario();
		String password = RegistroCliente.getCredencial().getPassword();

		Cliente c = new Cliente();
		c.setNombre(nombre);
		c.setFechanac(fechanac);
		c.setNif(nif);
		c.setDireccion(direccion);
		c.setEmail(email);
		c.setTelefono(telefono);

		Credenciales cr = new Credenciales();
		cr.setUsuario(usuario);
		cr.setPassword(password);
		cr.setRol("CLIENTE");

		c.setCredencial(cr);

		boolean nombreValido = servcliente.validarNombre(nombre);

		if (!nombreValido) {
			model.addAttribute("error", "Nombre Invalido.");
			model.addAttribute("cliente", RegistroCliente);
			return "/registro/RegistroCliente";
		}

		boolean emailValido = servcliente.validarEmail(email);

		if (!emailValido) {
			model.addAttribute("error", "Email Invalido.");
			model.addAttribute("cliente", RegistroCliente);
			return "/registro/RegistroCliente";
		}

		boolean direccionValido = servcliente.validarDireccion(direccion);

		if (!direccionValido) {
			model.addAttribute("error", "Direccion Invalida.");
			model.addAttribute("cliente", RegistroCliente);
			return "/registro/RegistroCliente";
		}

		boolean nifValido = servcliente.validarNIF(nif);

		if (!nifValido) {
			model.addAttribute("error", "Nif Invalido.");
			model.addAttribute("cliente", RegistroCliente);
			return "/registro/RegistroCliente";
		}

		boolean telefonoValido = servcliente.validarTelefono(telefono);

		if (!telefonoValido) {
			model.addAttribute("error", "Telefono Invalido.");
			model.addAttribute("cliente", RegistroCliente);
			return "/registro/RegistroCliente";
		}

		boolean usuarioValido = servcliente.validarUsuario(usuario);

		if (!usuarioValido) {
			model.addAttribute("error", "Usuario Invalido.");
			model.addAttribute("cliente", RegistroCliente);
			return "/registro/RegistroCliente";
		}

		boolean passwordValido = servcliente.validarPassword(password);

		if (!passwordValido) {
			model.addAttribute("error", "Password Invalido.");
			model.addAttribute("cliente", RegistroCliente);
			return "/registro/RegistroCliente";
		}

		boolean emailExistente = servcliente.validarEmailUnico(email);

		if (!emailExistente) {
			model.addAttribute("error", "El email ya existe.");
			model.addAttribute("cliente", RegistroCliente);
			return "/registro/RegistroCliente";
		}

		boolean usuarioExistente = servcliente.validarUsuarioUnico(usuario);

		if (!usuarioExistente) {
			model.addAttribute("error", "El usuario ya existe.");
			model.addAttribute("cliente", RegistroCliente);
			return "/registro/RegistroCliente";
		}

		try {
			servcliente.insertarCliente(c);
			servCredenciales.insertarCredencial(cr);

			model.addAttribute("exitoCliente", "Cliente añadido correctamente.");
			model.addAttribute("cliente", new Cliente());

			return "/inicio";
		} catch (Exception e) {

			model.addAttribute("error", "Hubo un error al registrar el cliente. Por favor, intente nuevamente.");
			model.addAttribute("cliente", RegistroCliente);
		}

		return "/registro/RegistroCliente";
	}

	@GetMapping("/Registro")
	public String mostrarFormularioRegistroCliente(Model model) {
		model.addAttribute("cliente", new Cliente());
		return "/registro/RegistroCliente";
	}

}
