package com.oscar.vivero.modelo;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lineaslote")
public class LineaLote implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	
	@ManyToOne
	@JoinColumn(name = "idLote")
	private Lote lote;

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

	public LineaLote(Long id, Lote lote, long codigoProveedor, String codigoPlanta, int cantidad, boolean urgente) {
		super();
		this.id = id;
		this.lote = lote;
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

	public Lote getLote() {
		return lote;
	}

	public void setLote(Lote lote) {
		this.lote = lote;
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
		return "LineaLote [id=" + id + ", lote=" + (lote != null ? lote.getId() : null) + ", codigoProveedor="
				+ codigoProveedor + ", codigoPlanta=" + codigoPlanta + ", cantidad=" + cantidad + ", urgente=" + urgente
				+ "]";
	}
}
