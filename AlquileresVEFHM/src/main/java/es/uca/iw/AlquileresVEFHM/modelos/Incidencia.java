package es.uca.iw.AlquileresVEFHM.modelos;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "incidencia")
public class Incidencia {
	@Id
	@Column(name = "_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@JoinColumn(name = "emisor")
	@ManyToOne(fetch = FetchType.LAZY)
	private User emisor;
	
	@JoinColumn(name = "receptor")
	@ManyToOne(fetch = FetchType.LAZY)
	private User receptor;
	
	@JoinColumn(name = "reserva")
	@ManyToOne(fetch = FetchType.LAZY)
	private Reserva reserva;
	
	@Column
	@NotEmpty(message = "Introduzca la descripción de su incidencia.")
	@NotNull
	private String descripcion;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	
	@Column
	private String resolucion;
	
	@Column
	@NotNull
	private boolean abierta;
	
	public Incidencia() {}

	public Integer getId() {
		return id;
	}

	public User getEmisor() {
		return emisor;
	}

	public User getReceptor() {
		return receptor;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public String getResolucion() {
		return resolucion;
	}

	public boolean isAbierta() {
		return abierta;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setEmisor(User emisor) {
		this.emisor = emisor;
	}

	public void setReceptor(User receptor) {
		this.receptor = receptor;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}

	public void setAbierta(boolean abierta) {
		this.abierta = abierta;
	}
	
	
	
	
	
	
	
	
	
	
}
