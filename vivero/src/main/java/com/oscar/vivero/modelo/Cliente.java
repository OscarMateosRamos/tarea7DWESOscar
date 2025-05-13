package com.oscar.vivero.modelo;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
	@Table(name = "clientes")
	public class Cliente implements Serializable {

		private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;

		@Column(name = "nombre")
		private String nombre;

		@Column(name = "fechanac")
		private Date fechanac;

		@Column(name = "nif", unique = true)
		private String nif;

		@Column(name = "direccion")
		private String direccion;

		@Column(name = "email")
		private String email;

		@Column(name = "telefono")
		private String telefono;

		@OneToOne(cascade = CascadeType.ALL)
		@JoinColumn(name = "idcredencial")
		private Credenciales credencial;

		@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
		private List<Pedido> pedidos;

		public Cliente() {

		}

		public Cliente(String nombre, Date fechanac, String nif, String direccion, String email, String telefono,
				Credenciales credencial, List<Pedido> pedidos) {
			super();
			this.nombre = nombre;
			this.fechanac = fechanac;
			this.nif = nif;
			this.direccion = direccion;
			this.email = email;
			this.telefono = telefono;
			this.credencial = credencial;
			this.pedidos = pedidos;
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

		public Date getFechanac() {
			return fechanac;
		}

		public void setFechanac(Date fechanac) {
			this.fechanac = fechanac;
		}

		public String getNif() {
			return nif;
		}

		public void setNif(String nif) {
			this.nif = nif;
		}

		public String getDireccion() {
			return direccion;
		}

		public void setDireccion(String direccion) {
			this.direccion = direccion;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getTelefono() {
			return telefono;
		}

		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}

		public Credenciales getCredencial() {
			return credencial;
		}

		public void setCredencial(Credenciales credencial) {
			this.credencial = credencial;
		}

		public List<Pedido> getPedidos() {
			return pedidos;
		}

		public void setPedidos(List<Pedido> pedidos) {
			this.pedidos = pedidos;
		}

		@Override
		public int hashCode() {
			return Objects.hash(credencial, direccion, email, fechanac, id, nif, nombre, pedidos, telefono);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cliente other = (Cliente) obj;
			return Objects.equals(credencial, other.credencial) && Objects.equals(direccion, other.direccion)
					&& Objects.equals(email, other.email) && Objects.equals(fechanac, other.fechanac)
					&& Objects.equals(id, other.id) && Objects.equals(nif, other.nif)
					&& Objects.equals(nombre, other.nombre) && Objects.equals(pedidos, other.pedidos)
					&& Objects.equals(telefono, other.telefono);
		}

		@Override
		public String toString() {
			return "Cliente [id=" + id + ", nombre=" + nombre + ", fechanac=" + fechanac + ", nif=" + nif + ", direccion="
					+ direccion + ", email=" + email + ", telefono=" + telefono + ", credencial=" + credencial
					+ ", pedidos=" + pedidos + "]";
		}


}
