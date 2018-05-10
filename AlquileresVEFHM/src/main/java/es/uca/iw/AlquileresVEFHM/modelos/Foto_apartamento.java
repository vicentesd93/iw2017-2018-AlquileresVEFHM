package es.uca.iw.AlquileresVEFHM.modelos;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "foto_apartamento")
public class Foto_apartamento {
	@Id
	@Column(name = "_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@JoinColumn(name = "apartamento")
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private Apartamento apartamento;
	@Column
	@NotEmpty(message = "Introduzca el nombre del archivo")
	@NotNull
	private String nombre;
	@Column
	@Lob
	@NotNull
	private Blob foto;
	
	public Foto_apartamento() {}
	public Foto_apartamento(@NotNull Apartamento apartamento,
			@NotEmpty(message = "Introduzca el nombre del archivo") @NotNull String nombre, @NotNull Blob foto) {
		super();
		this.apartamento = apartamento;
		this.nombre = nombre;
		this.foto = foto;
	}
	
	public Integer getId() {
		return id;
	}
	public Apartamento getApartamento() {
		return apartamento;
	}
	public String getNombre() {
		return nombre;
	}
	public Blob getFoto() {
		return foto;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public void setApartamento(Apartamento apartamento) {
		this.apartamento = apartamento;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setFoto(Blob foto) {
		this.foto = foto;
	}	
}
