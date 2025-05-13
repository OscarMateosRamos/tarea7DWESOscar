package com.oscar.vivero.servicio;

import org.springframework.stereotype.Service;

//Servicio de control de usuario: invitado, admin, ...

@Service
public class Controlador {

	private String username;
	private Long usernameID;

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public Long getUsernameID() {
		return usernameID;
	}

	public void setUsernameID(Long usernameID) {
		this.usernameID = usernameID;
	}

	public void cerrarSesion() {
		this.username = null;
	}
}