package es.uca.iw.AlquileresVEFHM.modelos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "reservaoferta")
public class ReservaOferta {
	@Id
	@Column(name = "_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@JoinColumn(name = "reserva", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	private Reserva reserva;
	@JoinColumn(name = "oferta", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	private Oferta oferta;
	public Integer getId() {
		return id;
	}
	public Reserva getReserva() {
		return reserva;
	}
	public Oferta getOferta() {
		return oferta;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
	public void setOferta(Oferta oferta) {
		this.oferta = oferta;
	}
	
	
}
