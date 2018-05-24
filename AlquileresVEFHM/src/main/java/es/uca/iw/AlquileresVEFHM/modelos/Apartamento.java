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
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "apartamento")
public class Apartamento {
	@Id
	@Column(name = "_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@JoinColumn(name = "anfitrion", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User anfitrion;
	@Column
	@NotEmpty(message = "*Introduzca la descripción del inmueble")
	@NotNull
	private String descripcion;
	@Column
	@NotEmpty(message = "*Introduzca la dirección del inmueble")
	@NotNull
	private String direccion;
	@Column
	@NotEmpty(message = "*Introduzca la población del inmueble")
	@NotNull
	private String poblacion;
	@Column
	@NotEmpty(message = "*Introduzca el pais del inmueble")
	@NotNull
	private String pais;
	@JoinColumn(name = "tipo_apartamento")
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	private Tipo_apartamento tipo_apartamento;
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
	@OneToMany(mappedBy = "apartamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Foto_apartamento> fotos_apartamento;
	@OneToMany(mappedBy = "apartamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Oferta> ofertas;
	
	public Apartamento() {}
	
	public Apartamento(User anfitrion, String descripcion, String direccion, String poblacion, String pais, Tipo_apartamento tipo_apartamento) {
		super();
		this.anfitrion = anfitrion;
		this.descripcion = descripcion;
		this.direccion = direccion;
		this.poblacion = poblacion;
		this.pais = pais;
		this.tipo_apartamento = tipo_apartamento;
	}
	
	public Apartamento(User anfitrion, String descripcion, String direccion, String poblacion, String pais, Tipo_apartamento tipo_apartamento,
			Integer aseos, Integer dormitorios, Integer m2, boolean garaje, boolean mascotas, boolean amueblado,
			boolean piscina, boolean jardin, boolean trastero, boolean ascensor, Set<Foto_apartamento> fotos_apartamento) {
		super();
		this.anfitrion = anfitrion;
		this.descripcion = descripcion;
		this.direccion = direccion;
		this.poblacion = poblacion;
		this.pais = pais;
		this.tipo_apartamento = tipo_apartamento;
		this.aseos = aseos;
		this.dormitorios = dormitorios;
		this.m2 = m2;
		this.garaje = garaje;
		this.mascotas = mascotas;
		this.amueblado = amueblado;
		this.piscina = piscina;
		this.jardin = jardin;
		this.trastero = trastero;
		this.ascensor = ascensor;
		this.fotos_apartamento = fotos_apartamento;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public User getAnfitrion() {
		return anfitrion;
	}
	public void setAnfitrion(User anfitrion) {
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
	public Tipo_apartamento getTipo_apartamento() {
		return tipo_apartamento;
	}
	public void setTipo_apartamento(Tipo_apartamento tipo_apartamento) {
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
	public boolean isGaraje() {
		return garaje;
	}
	public void setGaraje(boolean garaje) {
		this.garaje = garaje;
	}
	public boolean isMascotas() {
		return mascotas;
	}
	public void setMascotas(boolean mascotas) {
		this.mascotas = mascotas;
	}
	public boolean isAmueblado() {
		return amueblado;
	}
	public void setAmueblado(boolean amueblado) {
		this.amueblado = amueblado;
	}
	public boolean isPiscina() {
		return piscina;
	}
	public void setPiscina(boolean piscina) {
		this.piscina = piscina;
	}
	public boolean isJardin() {
		return jardin;
	}
	public void setJardin(boolean jardin) {
		this.jardin = jardin;
	}
	public boolean isTrastero() {
		return trastero;
	}
	public void setTrastero(boolean trastero) {
		this.trastero = trastero;
	}
	public boolean isAscensor() {
		return ascensor;
	}
	public void setAscensor(boolean ascensor) {
		this.ascensor = ascensor;
	}
	public Set<Foto_apartamento> getFotos_apartamento() {
		return fotos_apartamento;
	}
	public void setFotos_apartamento(Set<Foto_apartamento> fotos_apartamento) {
		this.fotos_apartamento = fotos_apartamento;
	}

	@Override
	public String toString() {
		return "Apartamento [id=" + id + ", descripcion=" + descripcion + ", direccion="
				+ direccion + ", poblacion=" + poblacion + ", pais=" + pais + ", tipo_apartamento=" + tipo_apartamento
				+ ", aseos=" + aseos + ", dormitorios=" + dormitorios + ", m2=" + m2 + ", garaje=" + garaje
				+ ", mascotas=" + mascotas + ", amueblado=" + amueblado + ", piscina=" + piscina + ", jardin=" + jardin
				+ ", trastero=" + trastero + ", ascensor=" + ascensor + "]";
	}
	
}
