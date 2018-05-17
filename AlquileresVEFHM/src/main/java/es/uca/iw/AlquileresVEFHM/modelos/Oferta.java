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
	private Date f_inicio;
	@Column
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date f_fin;
	@Column
	@NotNull
	private float penalizacion;
	@JoinColumn(name = "reserva", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Reserva reserva;
	
	public Oferta() {}
	
}
