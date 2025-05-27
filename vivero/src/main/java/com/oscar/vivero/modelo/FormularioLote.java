package com.oscar.vivero.modelo;

import java.util.ArrayList;
import java.util.List;

public class FormularioLote {

	private Long codigoProveedor;
	private boolean urgente;
	private List<LineaLote> lineas = new ArrayList<>();

	public FormularioLote() {
		super();
	}

	public FormularioLote(Long codigoProveedor, boolean urgente, List<LineaLote> lineas) {
		super();
		this.codigoProveedor = codigoProveedor;
		this.urgente = urgente;
		this.lineas = lineas;
	}

	public Long getCodigoProveedor() {
		return codigoProveedor;
	}

	public void setCodigoProveedor(Long codigoProveedor) {
		this.codigoProveedor = codigoProveedor;
	}

	public boolean isUrgente() {
		return urgente;
	}

	public void setUrgente(boolean urgente) {
		this.urgente = urgente;
	}

	public List<LineaLote> getLineas() {
		return lineas;
	}

	public void setLineas(List<LineaLote> lineas) {
		this.lineas = lineas;
	}

	@Override
	public String toString() {
		return "FormularioLote [codigoProveedor=" + codigoProveedor + ", urgente=" + urgente + ", lineas=" + lineas
				+ "]";
	}

}