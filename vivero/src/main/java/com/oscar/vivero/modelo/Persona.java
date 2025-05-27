package com.oscar.vivero.modelo;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "personas")

public class Persona implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "email")
	private String email;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "idpersona")
	private List<Mensaje> mensajes;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "idlote")
	private List<Lote> lotes;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idcredencial")
	private Credenciales credencial;

	public Persona() {

	}

	public Persona(Long id, String nombre, String email, List<Mensaje> mensajes, List<Lote> lotes,
			Credenciales credencial) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.mensajes = mensajes;
		this.lotes = lotes;
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

	public List<Mensaje> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public List<Lote> getLotes() {
		return lotes;
	}

	public void setLotes(List<Lote> lotes) {
		this.lotes = lotes;
	}

	public Credenciales getCredencial() {
		return credencial;
	}

	public void setCredencial(Credenciales credencial) {
		this.credencial = credencial;
	}

	@Override
	public String toString() {
		return "Persona [id=" + id + ", nombre=" + nombre + ", email=" + email + ", mensajes=" + mensajes + ", lotes="
				+ lotes + ", credencial=" + credencial + "]";
	}

}
