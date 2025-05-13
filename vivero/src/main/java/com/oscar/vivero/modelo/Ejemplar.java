package com.oscar.vivero.modelo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
	@Table(name = "ejemplares")
	public class Ejemplar implements Serializable {
		private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;

		@Column
		private String nombre;

		@Column(nullable = false)
		private boolean disponible = true;
		
		@Column
		private long idPedido;
		
		@ManyToOne
		@JoinColumn(name = "idplanta")
		private Planta planta;

		@OneToMany(cascade = CascadeType.ALL)
		@JoinColumn(name = "idejemplar")
		private List<Mensaje> mensajes;

		@ManyToOne
		@JoinColumn(name = "Pedido")
		private Pedido pedido;

		
		public Ejemplar() {

		}


		public Ejemplar(Long id, String nombre, boolean disponible, long idPedido, Planta planta, List<Mensaje> mensajes,
				Pedido pedido) {
			super();
			this.id = id;
			this.nombre = nombre;
			this.disponible = disponible;
			this.idPedido = idPedido;
			this.planta = planta;
			this.mensajes = mensajes;
			this.pedido = pedido;
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


		public boolean isDisponible() {
			return disponible;
		}


		public void setDisponible(boolean disponible) {
			this.disponible = disponible;
		}


		public long getIdPedido() {
			return idPedido;
		}


		public void setIdPedido(long idPedido) {
			this.idPedido = idPedido;
		}


		public Planta getPlanta() {
			return planta;
		}


		public void setPlanta(Planta planta) {
			this.planta = planta;
		}


		public List<Mensaje> getMensajes() {
			return mensajes;
		}


		public void setMensajes(List<Mensaje> mensajes) {
			this.mensajes = mensajes;
		}


		public Pedido getPedido() {
			return pedido;
		}


		public void setPedido(Pedido pedido) {
			this.pedido = pedido;
		}
		
		

		@Override
		public int hashCode() {
			return Objects.hash(disponible, id, idPedido, mensajes, nombre, pedido, planta);
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Ejemplar other = (Ejemplar) obj;
			return disponible == other.disponible && Objects.equals(id, other.id) && idPedido == other.idPedido
					&& Objects.equals(mensajes, other.mensajes) && Objects.equals(nombre, other.nombre)
					&& Objects.equals(pedido, other.pedido) && Objects.equals(planta, other.planta);
		}


		@Override
		public String toString() {
			return "Ejemplar [id=" + id + ", nombre=" + nombre + ", disponible=" + disponible + ", idPedido=" + idPedido
					+ ", planta=" + planta + ", mensajes=" + mensajes + ", pedido=" + pedido + "]";
		}

	
}
