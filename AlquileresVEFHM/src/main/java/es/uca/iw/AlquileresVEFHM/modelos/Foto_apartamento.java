package es.uca.iw.AlquileresVEFHM.modelos;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "foto_apartamento")
public class Foto_apartamento {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer _id;
	@Column
	private Integer apartamento;
	@Column
	@NotEmpty(message = "Introduzca el nombre del archivo")
	private String nombre;
	@Column
	@Lob
	private Blob foto;
	
	public Integer getId() {
		return _id;
	}
	public void setId(Integer _id) {
		this._id = _id;
	}
	public Integer getApartamento() {
		return apartamento;
	}
	public void setApartamento(Integer apartamento) {
		this.apartamento = apartamento;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Blob getFoto() {
		return foto;
	}
	public void setFoto(Blob foto) {
		this.foto = foto;
	}	
}
