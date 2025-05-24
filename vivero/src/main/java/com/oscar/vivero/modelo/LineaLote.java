package com.oscar.vivero.modelo;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lineaslote")
public class LineaLote implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private long idLote;

	@Column
	private long codigoProveedor;

	@Column
	private String codigoPlanta;

	@Column
	private int cantidad;

	@Column
	private boolean urgente = false;

	public LineaLote() {
		super();
	}

	public LineaLote(Long id, long idLote, long codigoProveedor, String codigoPlanta, int cantidad, boolean urgente) {
		super();
		this.id = id;
		this.idLote = idLote;
		this.codigoProveedor = codigoProveedor;
		this.codigoPlanta = codigoPlanta;
		this.cantidad = cantidad;
		this.urgente = urgente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getIdLote() {
		return idLote;
	}

	public void setIdLote(long idLote) {
		this.idLote = idLote;
	}

	public long getCodigoProveedor() {
		return codigoProveedor;
	}

	public void setCodigoProveedor(long codigoProveedor) {
		this.codigoProveedor = codigoProveedor;
	}

	public String getCodigoPlanta() {
		return codigoPlanta;
	}

	public void setCodigoPlanta(String codigoPlanta) {
		this.codigoPlanta = codigoPlanta;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public boolean isUrgente() {
		return urgente;
	}

	public void setUrgente(boolean urgente) {
		this.urgente = urgente;
	}

	@Override
	public String toString() {
		return "LineaLote [id=" + id + ", idLote=" + idLote + ", codigoProveedor=" + codigoProveedor + ", codigoPlanta="
				+ codigoPlanta + ", cantidad=" + cantidad + ", urgente=" + urgente + "]";
	}

}
