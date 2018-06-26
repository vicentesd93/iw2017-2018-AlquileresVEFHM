package es.uca.iw.AlquileresVEFHM.modelos;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
@Entity
@Table(name="usuario")
public class User implements UserDetails{	
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
	@OneToMany(mappedBy = "anfitrion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Apartamento> apartamentos;
	@OneToMany(mappedBy = "huesped", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Reserva> reservas;
	@OneToMany(mappedBy="emisor", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Incidencia> incidenciasemisor;
	@OneToMany(mappedBy="receptor", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Incidencia> incidenciasreceptor;
	
	public User() {}
	public User(@NotEmpty(message = "*Introduza su nombre de usuario") @NotNull String login,
			@NotEmpty(message = "*Introduza su contraseña") @NotNull String clave,
			@NotEmpty(message = "*Introduza un correo eléctronico") @Email(message = "*Introduzca un correo eléctronico válido") @NotNull String email,
			@NotEmpty(message = "*Introduza su DNI") @NotNull String dni,
			@NotEmpty(message = "*Introduza su nombre") @NotNull String nombre,
			@NotEmpty(message = "*Introduza sus apellidos") @NotNull String apellidos,
			@NotNull(message = "*Introduza su fecha de nacimiento") Date f_nacimiento, boolean sexo,
			@NotEmpty(message = "*Introduza su dirección") @NotNull String direccion,
			@NotEmpty(message = "*Introduza su teléfono") @NotNull String telefono, @NotNull Rol rol,
			@NotNull Date f_creacion, boolean activo) {
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
	public Set<Reserva> getReservas() {
		return reservas;
	}
	public Set<Incidencia> getIncidenciasemisor() {
		return incidenciasemisor;
	}
	public Set<Incidencia> getIncidenciasreceptor() {
		return incidenciasreceptor;
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
	public void setReservas(Set<Reserva> reservas) {
		this.reservas = reservas;
	}
	public void setIncidenciasemisor(Set<Incidencia> incidenciasemisor) {
		this.incidenciasemisor = incidenciasemisor;
	}
	public void setIncidenciasreceptor(Set<Incidencia> incidenciasreceptor) {
		this.incidenciasreceptor = incidenciasreceptor;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Rol> r = new HashSet<Rol>();
		r.add(rol);
		return r;
	}
	@Override
	public String getPassword() {
		return clave;
	}
	@Override
	public String getUsername() {
		return login;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return activo;
	}
	
	@Override
	public String toString() {
		return nombre + " " + apellidos;
	}
	public LocalDate getLDF_nacimiento() {
		if(f_nacimiento == null) return null;
		return Instant.ofEpochMilli(f_nacimiento.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}
	public void setLDF_nacimiento(LocalDate F_nacimiento) {
		f_nacimiento = Date.from(F_nacimiento.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
}