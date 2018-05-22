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
import javax.validation.constraints.NotNull;

@Entity
@Table(name="rol")
public class Rol {
	@Id
	@Column(name="_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column
	@NotNull
	private String nombre;
	@OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Usuario> usuarios;
	
	public Rol() {}
	public Rol(@NotNull String nombre) {
		super();
		this.nombre = nombre;
	}
	public Rol(@NotNull String nombre, Set<Usuario> usuarios) {
		super();
		this.nombre = nombre;
		this.usuarios = usuarios;
	}
	
	public int getId() {
		return id;
	}
	public String getNombre() {
		return nombre;
	}
	public Set<Usuario> getUsuarios() {
		return usuarios;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
}
