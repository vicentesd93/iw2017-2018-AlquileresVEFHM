package es.uca.iw.AlquileresVEFHM.modelos;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "oferta")
public class Oferta {
	@Id
	@Column(name = "_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@JoinColumn(name = "apartamento", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Apartamento apartamento;
	@Column
	@NotNull
	private float precio;
	@Column
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecha;
	@Column
	@NotNull
	private float penalizacion;
	@OneToMany(mappedBy = "oferta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<ReservaOferta> reservasofertas;
	
	public Oferta() {}

	public Integer getId() {
		return id;
	}

	public Apartamento getApartamento() {
		return apartamento;
	}

	public float getPrecio() {
		return precio;
	}

	public Date getFecha() {
		return fecha;
	}

	public float getPenalizacion() {
		return penalizacion;
	}

	public Set<ReservaOferta> getReservasofertas() {
		return reservasofertas;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setApartamento(Apartamento apartamento) {
		this.apartamento = apartamento;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setPenalizacion(float penalizacion) {
		this.penalizacion = penalizacion;
	}

	public void setReservasofertas(Set<ReservaOferta> reservasofertas) {
		this.reservasofertas = reservasofertas;
	}

	public LocalDate getLDFecha() {
		if(fecha == null) return null;
		return Instant.ofEpochMilli(fecha.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}
	public void setLDFecha(LocalDate f) {
		fecha = Date.from(f.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	@Override
	public String toString() {
		return "Oferta [id=" + id + ", precio=" + precio + ", fecha=" + fecha + ", penalizacion=" + penalizacion + "]";
	}
	
	
}
