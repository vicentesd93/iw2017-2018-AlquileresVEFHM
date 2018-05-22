package es.uca.iw.AlquileresVEFHM.modelos;

import java.util.Date;
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
	@Column(name = "_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column
	@NotEmpty(message = "*Introduza su nombre de usuario")
	@NotNull
	private String login;
	@Column
	@NotEmpty(message = "*Introduza su contraseña")
	@NotNull
	private String clave;
	@Column
	@NotEmpty(message = "*Introduza un correo eléctronico")
	@Email(message = "*Introduzca un correo eléctronico válido")
	@NotNull
	private String email;
	@Column
	@NotEmpty(message = "*Introduza su DNI")
	@NotNull
	private String dni;
	@Column
	@NotEmpty(message = "*Introduza su nombre")
	@NotNull
	private String nombre;
	@Column
	@NotEmpty(message = "*Introduza sus apellidos")
	@NotNull
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
	@NotNull
	private String direccion;
	@Column
	@NotEmpty(message = "*Introduza su teléfono")
	@NotNull
	private String telefono;
	@JoinColumn(name = "rol")
	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	private Rol rol;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date f_creacion;
	@Column
	private boolean activo;
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Apartamento> apartamentos;
	
	public Usuario() {}
	public Usuario(@NotEmpty(message = "*Introduza su nombre de usuario") @NotNull String login,
			@NotEmpty(message = "*Introduza su contraseña") @NotNull String clave,
			@NotEmpty(message = "*Introduza un correo eléctronico") @Email(message = "*Introduzca un correo eléctronico válido") @NotNull String email,
			@NotEmpty(message = "*Introduza su DNI") @NotNull String dni,
			@NotEmpty(message = "*Introduza su nombre") @NotNull String nombre,
			@NotEmpty(message = "*Introduza sus apellidos") @NotNull String apellidos,
			@NotNull(message = "*Introduza su fecha de nacimiento") Date f_nacimiento, boolean sexo,
			@NotEmpty(message = "*Introduza su dirección") @NotNull String direccion,
			@NotEmpty(message = "*Introduza su teléfono") @NotNull String telefono, @NotNull Rol rol,
			@NotNull Date f_creacion, boolean activo, Set<Apartamento> apartamentos) {
		super();
		this.login = login;
		this.clave = clave;
		this.email = email;
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.f_nacimiento = f_nacimiento;
		this.sexo = sexo;
		this.direccion = direccion;
		this.telefono = telefono;
		this.rol = rol;
		this.f_creacion = f_creacion;
		this.activo = activo;
		this.apartamentos = apartamentos;
	}
	
	public Integer getId() {
		return id;
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
	public boolean isSexo() {
		return sexo;
	}
	public String getDireccion() {
		return direccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public Rol getRol() {
		return rol;
	}
	public Date getF_creacion() {
		return f_creacion;
	}
	public boolean isActivo() {
		return activo;
	}
	public Set<Apartamento> getApartamentos() {
		return apartamentos;
	}
	
	public void setId(Integer id) {
		this.id = id;
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
	public void setRol(Rol rol) {
		this.rol = rol;
	}
	public void setF_creacion(Date f_creacion) {
		this.f_creacion = f_creacion;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public void setApartamentos(Set<Apartamento> apartamentos) {
		this.apartamentos = apartamentos;
	}
}