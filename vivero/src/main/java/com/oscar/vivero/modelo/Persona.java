package com.oscar.vivero.modelo;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "personas")

public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "email")
	private String email;

	@OneToMany(mappedBy = "persona", cascade = CascadeType.ALL)
	private List<Mensaje> mensajes;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idcredencial")
	private Credenciales credencial;

	public Persona() {

	}

	public Persona(Long id, String nombre, String email, Credenciales credencial) {
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.credencial = credencial;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Mensaje> getMensaje() {
		return getMensaje();
	}

	public void setMensaje(List<Mensaje> mensaje) {
		this.mensajes = mensaje;
	}

	public Credenciales getCredencial() {
		return credencial;
	}

	public void setCredencial(Credenciales credencial) {
		this.credencial = credencial;
	}

	@Override
	public int hashCode() {
		return Objects.hash(credencial, email, id, mensajes, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Persona other = (Persona) obj;
		return Objects.equals(credencial, other.credencial) && Objects.equals(email, other.email)
				&& Objects.equals(id, other.id) && Objects.equals(mensajes, other.mensajes)
				&& Objects.equals(nombre, other.nombre);
	}

	@Override
	public String toString() {
		return "Persona [id=" + id + ", nombre=" + nombre + ", email=" + email + ", mensaje=" + mensajes
				+ ", credencial=" + credencial + "]";
	}

}
