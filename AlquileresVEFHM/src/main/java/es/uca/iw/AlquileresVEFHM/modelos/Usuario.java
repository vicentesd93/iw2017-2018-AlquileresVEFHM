package es.uca.iw.AlquileresVEFHM.modelos;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer _id;
	@Column
	@NotNull
	private String login;
	@Column
	@NotNull
	private String clave;
	@Column
	@NotNull
	private String email;
	@Column
	@NotNull
	private String dni;
	@Column
	@NotNull
	private String nombre;
	@Column
	@NotNull
	private String apellidos;
	@Column
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date f_nacimiento;
	@Column
	@NotNull
	private boolean sexo;
	@Column
	@NotNull
	private String direccion;
	@Column
	@NotNull
	private String telefono;
	@Column
	@NotNull
	private Integer rol;
	@Column
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date f_creacion;
	
	public Usuario() {}
	
	public void setId(Integer id) {
		_id = id;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public void setF_nacimiento(Date f_nacimiento) {
		this.f_nacimiento = f_nacimiento;
	}
	public void setSexo(boolean sexo) {
		this.sexo = sexo;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public void setRol(Integer rol) {
		this.rol = rol;
	}
	public void setF_creacion(Date f_creacion) {
		this.f_creacion = f_creacion;
	}
	
	public Integer getId() {
		return _id;
	}
	public String getLogin() {
		return login;
	}
	public String getClave() {
		return clave;
	}
	public String getEmail() {
		return email;
	}
	public String getDni() {
		return dni;
	}
	public String getNombre() {
		return nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public Date getF_nacimiento() {
		return f_nacimiento;
	}
	public boolean getSexo() {
		return sexo;
	}
	public String getDireccion() {
		return direccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public Integer getRol() {
		return rol;
	}
	public Date getF_creacion() {
		return f_creacion;
	}
	
}