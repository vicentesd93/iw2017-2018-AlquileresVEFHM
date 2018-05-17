package es.uca.iw.AlquileresVEFHM.modelos;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "reserva")
public class Reserva {
	@Id
	@Column(name = "_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@JoinColumn(name = "huesped", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private User huesped;
	@Column
	private boolean aceptada;
	@JoinColumn(name = "factura", nullable = true)
	@OneToOne(fetch = FetchType.LAZY)
	private Factura factura;
	@OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Oferta> ofertas;
	
	public Reserva() {}
	public Reserva(@NotNull User huesped, boolean aceptada) {
		super();
		this.huesped = huesped;
		this.aceptada = aceptada;
	}
	
	public Integer getId() {
		return id;
	}
	public User getHuesped() {
		return huesped;
	}
	public boolean isAceptada() {
		return aceptada;
	}
	public Factura getFactura() {
		return factura;
	}
	public Set<Oferta> getOfertas() {
		return ofertas;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public void setHuesped(User huesped) {
		this.huesped = huesped;
	}
	public void setAceptada(boolean aceptada) {
		this.aceptada = aceptada;
	}
	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	public void setOfertas(Set<Oferta> ofertas) {
		this.ofertas = ofertas;
	}
}
