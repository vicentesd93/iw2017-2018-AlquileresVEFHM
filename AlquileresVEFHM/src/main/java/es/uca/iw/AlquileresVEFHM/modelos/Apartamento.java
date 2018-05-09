package es.uca.iw.AlquileresVEFHM.modelos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="apartamento")
public class Apartamento {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer _id;
	@Column
	private Integer anfitrion;
	@Column
	@NotEmpty(message = "*Introduzca la descripción del inmueble")
	private String descripcion;
	@Column
	@NotEmpty(message = "*Introduzca la dirección del inmueble")
	private String direccion;
	@Column
	@NotEmpty(message = "*Introduzca la población del inmueble")
	private String poblacion;
	@Column
	@NotEmpty(message = "*Introduzca el pais del inmueble")
	private String pais;
	@Column
	private Integer tipo_apartamento;
	@Column
	private Integer aseos;
	@Column
	private Integer dormitorios;
	@Column
	private Integer m2;
	@Column
	private boolean garaje;
	@Column
	private boolean mascotas;
	@Column
	private boolean amueblado;
	@Column
	private boolean piscina;
	@Column
	private boolean jardin;
	@Column
	private boolean trastero;
	@Column
	private boolean ascensor;
	
	public Integer getId() {
		return _id;
	}
	public void setId(Integer _id) {
		this._id = _id;
	}
	public Integer getAnfitrion() {
		return anfitrion;
	}
	public void setAnfitrion(Integer anfitrion) {
		this.anfitrion = anfitrion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public Integer getTipo_apartamento() {
		return tipo_apartamento;
	}
	public void setTipo_apartamento(Integer tipo_apartamento) {
		this.tipo_apartamento = tipo_apartamento;
	}
	public Integer getAseos() {
		return aseos;
	}
	public void setAseos(Integer aseos) {
		this.aseos = aseos;
	}
	public Integer getDormitorios() {
		return dormitorios;
	}
	public void setDormitorios(Integer dormitorios) {
		this.dormitorios = dormitorios;
	}
	public Integer getM2() {
		return m2;
	}
	public void setM2(Integer m2) {
		this.m2 = m2;
	}
	public boolean getgaraje() {
		return garaje;
	}
	public void setgaraje(boolean garaje) {
		this.garaje = garaje;
	}
	public boolean getMascotas() {
		return mascotas;
	}
	public void setMascotas(boolean mascotas) {
		this.mascotas = mascotas;
	}
	public boolean getAmueblado() {
		return amueblado;
	}
	public void setAmueblado(boolean amueblado) {
		this.amueblado = amueblado;
	}
	public boolean getPiscina() {
		return piscina;
	}
	public void setPiscina(boolean piscina) {
		this.piscina = piscina;
	}
	public boolean getJardin() {
		return jardin;
	}
	public void setJardin(boolean jardin) {
		this.jardin = jardin;
	}
	public boolean getTrastero() {
		return trastero;
	}
	public void setTrastero(boolean trastero) {
		this.trastero = trastero;
	}
	public boolean getAscensor() {
		return ascensor;
	}
	public void setAscensor(boolean ascensor) {
		this.ascensor = ascensor;
	}
}
