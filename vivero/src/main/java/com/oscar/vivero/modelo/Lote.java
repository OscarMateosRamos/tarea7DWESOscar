package com.oscar.vivero.modelo;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "lote")
public class Lote {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "urgente")
	private boolean urgente = false;

	@Column(name = "fechahora_peticion")
	private LocalDateTime fechapeticion = LocalDateTime.now();

	@Column(name = "fechahora_recepcion")
	private LocalDateTime fecharecepcion = null;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "idejemplar")
	private List<Ejemplar> ejemplares;

	@ManyToOne
	@JoinColumn(name = "Proveedor")
	private Proveedor proveedor;

	@ManyToOne
	@JoinColumn(name = "Persona")
	private Persona persona;

	public Lote() {
		super();
	}

	public Lote(Long id, boolean urgente, LocalDateTime fechapeticion, LocalDateTime fecharecepcion,
			List<Ejemplar> ejemplares, Proveedor proveedor, Persona persona) {
		super();
		this.id = id;
		this.urgente = urgente;
		this.fechapeticion = fechapeticion;
		this.fecharecepcion = fecharecepcion;
		this.ejemplares = ejemplares;
		this.proveedor = proveedor;
		this.persona = persona;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isUrgente() {
		return urgente;
	}

	public void setUrgente(boolean urgente) {
		this.urgente = urgente;
	}

	public LocalDateTime getFechapeticion() {
		return fechapeticion;
	}

	public void setFechapeticion(LocalDateTime fechapeticion) {
		this.fechapeticion = fechapeticion;
	}

	public LocalDateTime getFecharecepcion() {
		return fecharecepcion;
	}

	public void setFecharecepcion(LocalDateTime fecharecepcion) {
		this.fecharecepcion = fecharecepcion;
	}

	public List<Ejemplar> getEjemplares() {
		return ejemplares;
	}

	public void setEjemplares(List<Ejemplar> ejemplares) {
		this.ejemplares = ejemplares;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	@Override
	public String toString() {
		return "Lote [id=" + id + ", urgente=" + urgente + ", fechapeticion=" + fechapeticion + ", fecharecepcion="
				+ fecharecepcion + ", ejemplares=" + ejemplares + ", proveedor=" + proveedor + ", persona=" + persona
				+ "]";
	}

}
