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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tipo_apartamento")
public class Tipo_apartamento {
	@Id
	@Column(name = "_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column
	@NotEmpty(message = "Introduzca el nombre del tipo de apartamento")
	@NotNull
	private String nombre;
	@OneToMany(mappedBy = "tipo_apartamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Apartamento> apartamentos;
	
	public Tipo_apartamento() {}
	public Tipo_apartamento(@NotEmpty(message = "Introduzca el nombre del tipo de apartamento") @NotNull String nombre) {
		super();
		this.nombre = nombre;
	}
	public Tipo_apartamento(@NotEmpty(message = "Introduzca el nombre del tipo de apartamento") @NotNull String nombre,
			Set<Apartamento> apartamentos) {
		super();
		this.nombre = nombre;
		this.apartamentos = apartamentos;
	}
	
	public Integer getId() {
		return id;
	}
	public String getNombre() {
		return nombre;
	}
	public Set<Apartamento> getApartamentos() {
		return apartamentos;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setApartamentos(Set<Apartamento> apartamentos) {
		this.apartamentos = apartamentos;
	}
	
}
