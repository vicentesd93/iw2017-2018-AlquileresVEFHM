package es.uca.iw.AlquileresVEFHM.modelos;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "metodo_pago")
public class Metodo_pago {
	@Id
	@Column(name = "_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	@NotNull
	private String descripcion;
	@Column
	@NotNull
	private float cargo_adicional;
	@OneToMany(mappedBy = "metodo_pago", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Factura> facturas;
	
	public Metodo_pago() {}
	public Metodo_pago(@NotNull String descripcion, @NotNull float cargo_adicional) {
		super();
		this.descripcion = descripcion;
		this.cargo_adicional = cargo_adicional;
	}

	public Integer getId() {
		return id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public float getCargo_adicional() {
		return cargo_adicional;
	}
	public Set<Factura> getFacturas() {
		return facturas;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public void setCargo_adicional(float cargo_adicional) {
		this.cargo_adicional = cargo_adicional;
	}
	public void setFacturas(Set<Factura> facturas) {
		this.facturas = facturas;
	}
	@Override
	public String toString() {
		return descripcion;
	}
	
}
