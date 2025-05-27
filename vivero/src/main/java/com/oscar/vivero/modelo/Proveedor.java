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
@Table(name = "proveedor")
public class Proveedor  implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "cif", unique = true)
	private String cif;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "idlote")
	private List<Lote> lotes;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "idcredencial")
	private Credenciales credencial;

	public Proveedor() {
		super();
	}

	public Proveedor(Long id, String nombre, String cif, List<Lote> lotes, Credenciales credencial) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.cif = cif;
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

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
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
		return "Proveedor [id=" + id + ", nombre=" + nombre + ", cif=" + cif + ", lotes=" + lotes + ", credencial="
				+ credencial + "]";
	}

}
