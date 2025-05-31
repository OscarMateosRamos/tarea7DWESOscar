package com.oscar.vivero.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "fecha")
	private LocalDateTime fecha;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cliente")
	private Cliente cliente;

//	@OneToMany
//	@JoinColumn(name = "idEjemplar")
//	private List<Ejemplar> ejemplares;

	@Column
	private long idCliente;

	public Pedido() {
		super();
	}

	public Pedido(Long id, LocalDateTime fecha, Cliente cliente, long idCliente) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.cliente = cliente;
		this.idCliente = idCliente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	@Override
	public String toString() {
		return "Pedido [id=" + id + ", fecha=" + fecha + ", cliente=" + cliente + ", idCliente=" + idCliente + "]";
	}

}
