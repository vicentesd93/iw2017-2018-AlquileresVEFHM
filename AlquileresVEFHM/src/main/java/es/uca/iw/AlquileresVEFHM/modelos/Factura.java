package es.uca.iw.AlquileresVEFHM.modelos;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "factura")
public class Factura {
	@Id
	@Column(name = "_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@JoinColumn(name = "metodo_pago", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Metodo_pago metodo_pago;
	@Column
	@NotNull
	private float iva;
	@Column
	@NotNull
	private float comision;
	@Column
	@NotNull
	private float total;
	@OneToOne(mappedBy = "factura", cascade = CascadeType.ALL)
	private Reserva reserva;
	
	public Factura() {}
	public Factura(Metodo_pago metodo_pago, @NotNull float iva, @NotNull float comision, @NotNull float total) {
		super();
		this.metodo_pago = metodo_pago;
		this.iva = iva;
		this.comision = comision;
		this.total = total;
	}
	
	public Integer getId() {
		return id;
	}
	public Metodo_pago getMetodo_pago() {
		return metodo_pago;
	}
	public float getIva() {
		return iva;
	}
	public float getComision() {
		return comision;
	}
	public float getTotal() {
		return total;
	}
	public Reserva getReserva() {
		return reserva;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public void setMetodo_pago(Metodo_pago metodo_pago) {
		this.metodo_pago = metodo_pago;
	}
	public void setIva(float iva) {
		this.iva = iva;
	}
	public void setComision(float comision) {
		this.comision = comision;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
}
