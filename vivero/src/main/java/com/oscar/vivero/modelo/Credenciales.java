package com.oscar.vivero.modelo;

import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "credenciales")
public class Credenciales {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "usuario", unique = true)
	private String usuario;

	@Column(name = "password")
	private String password;

	@Column(name = "rol")
	private String rol;

	public Credenciales() {

	}

	public Credenciales(Long id, String usuario, String password, String rol) {
		super();
		this.id = id;
		this.usuario = usuario;
		this.password = password;
		this.rol = rol;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, password, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Credenciales other = (Credenciales) obj;
		return Objects.equals(id, other.id) && Objects.equals(password, other.password)
				&& Objects.equals(usuario, other.usuario);
	}

	@Override
	public String toString() {
		return "Credenciales [id=" + id + ", usuario=" + usuario + ", password=" + password + ", rol=" + rol + "]";
	}


	
	
}
