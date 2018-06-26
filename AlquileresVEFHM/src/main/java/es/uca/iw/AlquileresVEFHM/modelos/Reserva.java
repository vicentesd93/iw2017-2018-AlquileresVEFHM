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
	@Column
	private boolean rechazada;
	@JoinColumn(name = "factura", nullable = true)
	@OneToOne(fetch = FetchType.LAZY)
	private Factura factura;
	@OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<ReservaOferta> reservasofertas;
	@OneToMany(mappedBy="reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Incidencia> incidencias;
	
	public Reserva() {}

	public Integer getId() {
		return id;
	}

	public User getHuesped() {
		return huesped;
	}

	public boolean isAceptada() {
		return aceptada;
	}

	public boolean isRechazada() {
		return rechazada;
	}

	public Factura getFactura() {
		return factura;
	}

	public Set<ReservaOferta> getReservasofertas() {
		return reservasofertas;
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

	public void setRechazada(boolean rechazada) {
		this.rechazada = rechazada;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public void setReservasofertas(Set<ReservaOferta> reservasofertas) {
		this.reservasofertas = reservasofertas;
	}
	
	public Set<Incidencia> getIncidencias() {
		return incidencias;
	}

	public void setIncidencias(Set<Incidencia> incidencias) {
		this.incidencias = incidencias;
	}

	@Override
	public boolean equals(Object object) {
		if(object == null || object.getClass() != getClass()) return false;
		return ((Reserva)object).getId() == id;
	}

	@Override
	public int hashCode() {
		return id;
	}
	
	public float getTotal() {
		float total = 0;
		for(ReservaOferta ro : getReservasofertas()) {
			total += ro.getOferta().getPrecio();
		}
		return total;
	}

	@Override
	public String toString() {
		return "Apartamento: " + reservasofertas.iterator().next().getOferta().getApartamento().getDireccionCompleta();
	}
	
	public User getAnfitrion() {
		return reservasofertas.iterator().next().getOferta().getApartamento().getAnfitrion();
	}
}
