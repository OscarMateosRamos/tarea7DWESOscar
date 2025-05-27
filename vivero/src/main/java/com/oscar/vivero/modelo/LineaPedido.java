package com.oscar.vivero.modelo;

import java.io.Serializable;

import jakarta.persistence.*;

public class LineaPedido implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String codigoPlanta;

	private int cantidad;

	public LineaPedido() {
		super();
	}

	public LineaPedido(String codigoPlanta, int cantidad) {
		super();
		this.codigoPlanta = codigoPlanta;
		this.cantidad = cantidad;
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

	@Override
	public String toString() {
		return "LineaPedido [codigoPlanta=" + codigoPlanta + ", cantidad=" + cantidad + "]";
	}

}
