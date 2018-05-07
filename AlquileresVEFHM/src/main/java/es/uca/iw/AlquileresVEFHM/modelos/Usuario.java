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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer _id;
	@Column
	@NotEmpty(message = "*Introduza su nombre de usuario")
	private String login;
	@Column
	@NotEmpty(message = "*Introduza su contraseña")
	private String clave;
	@Column
	@NotEmpty(message = "*Introduza un correo eléctronico")
	@Email(message = "*Introduzca un correo eléctronico válido")
	private String email;
	@Column
	@NotEmpty(message = "*Introduza su DNI")
	private String dni;
	@Column
	@NotEmpty(message = "*Introduza su nombre")
	private String nombre;
	@Column
	@NotEmpty(message = "*Introduza sus apellidos")
	private String apellidos;
	@Column
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "*Introduza su fecha de nacimiento")
	private Date f_nacimiento;
	@Column
	private boolean sexo;
	@Column
	@NotEmpty(message = "*Introduza su dirección")
	private String direccion;
	@Column
	@NotEmpty(message = "*Introduza su teléfono")
	private String telefono;
	@Column
	private Integer rol;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date f_creacion;
	@Column
	private boolean activo;
	
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
	public void setActivo(boolean activo) {
		this.activo = activo;
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
	public boolean getActivo() {
		return activo;
	}
}