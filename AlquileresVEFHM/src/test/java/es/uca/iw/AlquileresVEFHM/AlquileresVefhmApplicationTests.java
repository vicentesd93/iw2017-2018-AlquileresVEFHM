package es.uca.iw.AlquileresVEFHM;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import es.uca.iw.AlquileresVEFHM.DAO.ApartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.Metodo_pagoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.RolDAO;
import es.uca.iw.AlquileresVEFHM.DAO.Tipo_apartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Metodo_pago;
import es.uca.iw.AlquileresVEFHM.modelos.Rol;
import es.uca.iw.AlquileresVEFHM.modelos.Tipo_apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.User;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AlquileresVefhmApplicationTests {
	@Autowired
	private UserDAO userdao;
	@Autowired
	private ApartamentoDAO apartDao; 
	@Autowired 
	private RolDAO rolDao;
	@Autowired
	private Tipo_apartamentoDAO tipoApDAO;
	@Autowired
	private Metodo_pagoDAO metodoPagoDao;
	
	@Test
	public void contextLoads() {
	}

	
	 @Test
	   public void creacionUsuarios() {
		 
		 User usuario = new User();
		 usuario.setActivo(true);
		 usuario.setClave("clave");
		 usuario.setDireccion("direccion");
		 usuario.setDni("dni");
		 usuario.setEmail("email@email.com");
		 usuario.setF_creacion(new Date());
		 usuario.setF_nacimiento(new Date());
		 usuario.setId(1);
		 usuario.setLogin("login");
		 usuario.setNombre("nombre");
		 
	     assertNotNull(usuario);
	 }
	 
	 @Test
	 public void crearUsuariosConParametros()
	 {
		 String clave = "clave";
		 String direccion = "direccion";
		 String dni = "dni";
		 String email = "emailPrueba@correo.com";
		 String login = "logindepruebasusuario";
		 String nombre = "nombre";
		 String apellidos = "apellidos";
		 String telefono = "777777777";
		 Date fech_nac = new Date();
		 Date fech_reg = new Date();
		 boolean sex= true;
		 Rol rol = rolDao.findById(rolDao.findByNombre("Anfitrion").getId()).get();
		 boolean activo = true;
		 
		 User user = new User(login, clave, email, dni, nombre, apellidos,fech_nac, sex, direccion, telefono,rol, fech_reg, activo);
		User usuarioCreado = userdao.save(user);
		 
		 
		 assertNotNull(user);
		 assertEquals(usuarioCreado.getEmail(), user.getEmail());
		 assertEquals(usuarioCreado.getDni(), user.getDni());
		 assertEquals(usuarioCreado.getRol(), user.getRol());
	 }
	 
	 
	 @Test 
	 public void añadirApartamento() {
		 final  Apartamento apartamento = new Apartamento();
		 final Tipo_apartamento tipoApart = new Tipo_apartamento();
		 tipoApart.setId(1);
		 tipoApart.setNombre("atico");
		 
		 apartamento.setId(3);
		 final User anfitrion = new  User();
		 anfitrion.setActivo(true);
		 anfitrion.setClave("clave");
		 anfitrion.setDireccion("direccion");
		 anfitrion.setDni("dni");
		 anfitrion.setEmail("email@email.com");
		 Date fecha = new Date();
		 anfitrion.setF_creacion(fecha);
		 anfitrion.setF_nacimiento(fecha);
		 anfitrion.setId(1);
		 anfitrion.setLogin("login");
		 anfitrion.setNombre("nombre");
		 anfitrion.setApellidos("apellidos");
		 anfitrion.setRol(rolDao.findById(1).get());
		 anfitrion.setTelefono("777777777");
		 apartamento.setAmueblado(true);
		 apartamento.setAscensor(true);
		 apartamento.setAseos(2);
		 apartamento.setDescripcion("descripcion");
		 apartamento.setDireccion("calle");
		 apartamento.setDormitorios(2);
		 apartamento.setGaraje(true);
		 apartamento.setJardin(true);
		 apartamento.setM2(80);
		 apartamento.setMascotas(false);
		 apartamento.setPais("ES");
		 apartamento.setPiscina(true);
		 apartamento.setPoblacion("jerez");
		 apartamento.setTrastero(true);
		 apartamento.setAnfitrion(anfitrion);
		
		 tipoApDAO.save(tipoApart);
		 apartamento.setTipo_apartamento(tipoApDAO.findById(1).get());
		 apartDao.save(apartamento);
		 userdao.save(anfitrion);
		 assertNotNull(userdao.findById(anfitrion.getId()).get().getApartamentos());
	 }
	 
	 @Test
	 public void buscarRol() {

		 String nombreRol = "Anfitrion";
		assertNotNull(rolDao.findById(rolDao.findByNombre(nombreRol).getId()));
		assertEquals(nombreRol, rolDao.findByNombre(nombreRol).getNombre());
		 
	 }
	 
	 @Test
	 public void crearMetododoPago() {
		 String descripcion = "metodopago";
		 float cargo = 2.2f;
		 Metodo_pago mpago = new Metodo_pago(descripcion,cargo);
		 
		 Metodo_pago mpagoGuardado = metodoPagoDao.save(mpago);
		 assertNotNull(mpagoGuardado);
		 assertEquals(mpagoGuardado.getDescripcion(), mpago.getDescripcion());

		 
	 }

}
