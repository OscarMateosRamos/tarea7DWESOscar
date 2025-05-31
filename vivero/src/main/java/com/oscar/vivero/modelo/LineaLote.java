package com.oscar.vivero.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "lineaslote")
public class LineaLote implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idLote", nullable = false)
	private Lote lote;

	@Column(name = "codigoProveedor", nullable = false)
	private Long codigoProveedor;

	@ManyToOne
	@JoinColumn(name = "codigoProveedor", referencedColumnName = "id", insertable = false, updatable = false)
	private Proveedor proveedor;

	@Column(nullable = false)
	private String codigoPlanta;

	@Column(nullable = false)
	private int cantidad;

	@Column(nullable = false)
	private boolean urgente = false;

	public LineaLote() {
		super();
	}

	public LineaLote(Long id, Lote lote, Long codigoProveedor, Proveedor proveedor, String codigoPlanta, int cantidad,
			boolean urgente) {
		super();
		this.id = id;
		this.lote = lote;
		this.codigoProveedor = codigoProveedor;
		this.proveedor = proveedor;
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

	public Long getCodigoProveedor() {
		return codigoProveedor;
	}

	public void setCodigoProveedor(Long codigoProveedor) {
		this.codigoProveedor = codigoProveedor;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
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
		return "LineaLote{" + "id=" + id + ", lote=" + (lote != null ? lote.getId() : null) + ", codigoProveedor="
				+ codigoProveedor + ", codigoPlanta='" + codigoPlanta + '\'' + ", cantidad=" + cantidad + ", urgente="
				+ urgente + '}';
	}
}
