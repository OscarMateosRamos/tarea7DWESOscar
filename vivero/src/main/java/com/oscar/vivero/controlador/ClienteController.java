package com.oscar.vivero.controlador;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

		List<String> errores = new ArrayList<>();

		String nombre = RegistroCliente.getNombre();
		Date fechanac = RegistroCliente.getFechanac();
		String nif = RegistroCliente.getNif();
		String direccion = RegistroCliente.getDireccion();
		String email = RegistroCliente.getEmail();
		String telefono = RegistroCliente.getTelefono();

		String usuario = RegistroCliente.getCredencial().getUsuario();
		String password = RegistroCliente.getCredencial().getPassword();

		if (!servcliente.validarNombre(nombre)) {
			errores.add("Nombre inválido.");
		}

		if (!servcliente.validarNombre(nombre)) {
			errores.add("Nombre inválido.");
		}

		if (!servcliente.validarEmail(email)) {
			errores.add("Email inválido.");
		}

		if (!servcliente.validarDireccion(direccion)) {
			errores.add("Dirección inválida.");
		}

		if (!servcliente.validarNIF(nif)) {
			errores.add("NIF inválido.");
		}

		if (!servcliente.validarTelefono(telefono)) {
			errores.add("Teléfono inválido.");
		}

		if (!servcliente.validarUsuario(usuario)) {
			errores.add("Usuario inválido.");
		}

		if (!servcliente.validarPassword(password)) {
			errores.add("Password inválido.");
		}

		if (!servcliente.validarEmailUnico(email)) {
			errores.add("El email ya existe.");
		}

		if (!servcliente.validarUsuarioUnico(usuario)) {
			errores.add("El usuario ya existe.");
		}

		if (!errores.isEmpty()) {
			model.addAttribute("errores", errores);
			model.addAttribute("cliente", RegistroCliente);
			return "/registro/RegistroCliente";
		}

		try {

			Credenciales cr = new Credenciales();
			cr.setUsuario(usuario);
			cr.setPassword(password);
			cr.setRol("CLIENTE");
			servCredenciales.insertarCredencial(cr);

			Cliente c = new Cliente();
			c.setNombre(nombre);
			c.setFechanac(fechanac);
			c.setNif(nif);
			c.setDireccion(direccion);
			c.setEmail(email);
			c.setTelefono(telefono);

			servcliente.insertarCliente(c);

			c.setCredencial(cr);
			servcliente.insertarCliente(c);

			model.addAttribute("exitoCliente", "Cliente añadido correctamente.");
			model.addAttribute("cliente", new Cliente());

			return "/registro/RegistroCliente";
		} catch (Exception e) {
			System.out.println("Excepcion al insertar cliente/credenciales: " + e.getLocalizedMessage());
			e.printStackTrace();
			model.addAttribute("error", "Hubo un errrrrrrrror al registrar el cliente. Por favor, intente nuevamente.");
			model.addAttribute("cliente", RegistroCliente);
			return "/registro/RegistroCliente";
		}
	}

	@GetMapping("/Registro")
	public String mostrarFormularioRegistroCliente(Model model) {
		model.addAttribute("cliente", new Cliente());
		return "/registro/RegistroCliente";
	}

}
