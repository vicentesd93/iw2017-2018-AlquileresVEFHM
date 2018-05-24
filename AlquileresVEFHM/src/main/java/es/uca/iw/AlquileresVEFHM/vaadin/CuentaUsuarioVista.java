package es.uca.iw.AlquileresVEFHM.vaadin;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.User;
import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;

@SpringView(name = CuentaUsuarioVista.NOMBRE)
public class CuentaUsuarioVista extends HorizontalLayout implements View {
	private static final long serialVersionUID = 1L;
	public final static String NOMBRE = "cuenta_usuario";
	private User usuario;
	private UserDAO userDao;
	
	private Component misDatos() {
		
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		
		HorizontalLayout hapel = new HorizontalLayout();
		Label  apellidos = new Label(usuario.getApellidos());
		apellidos.addStyleName(ValoTheme.LABEL_LIGHT);
		Label  lb_apellidos = new Label("Apellidos: ");
		lb_apellidos.addStyleName(ValoTheme.LABEL_LARGE);
		hapel.addComponent(lb_apellidos);
		hapel.addComponent(apellidos);
		hapel.setComponentAlignment(apellidos, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout hdire = new HorizontalLayout();
		Label  lb_direccion= new Label("Direccion: ");
		lb_direccion.addStyleName(ValoTheme.LABEL_LARGE);
		Label  direccion= new Label(usuario.getDireccion());
		direccion.addStyleName(ValoTheme.LABEL_LIGHT);
		hdire.addComponent(lb_direccion);
		hdire.addComponent(direccion);
		hdire.setComponentAlignment(direccion, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout hdni = new HorizontalLayout();
		Label lb_dni = new Label("DNI: ");
		lb_dni.addStyleName(ValoTheme.LABEL_LARGE);
		Label dni = new Label(usuario.getDni());
		dni.addStyleName(ValoTheme.LABEL_LIGHT);
		hdni.addComponent(lb_dni);
		hdni.addComponent(dni);
		hdni.setComponentAlignment(dni, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout hemai = new HorizontalLayout();
		Label  lb_email = new Label("Email: ");
		lb_email.addStyleName(ValoTheme.LABEL_LARGE);
		Label  email = new Label(usuario.getEmail());
		email.addStyleName(ValoTheme.LABEL_LIGHT);
		hemai.addComponent(lb_email);
		hemai.addComponent(email);
		hemai.setComponentAlignment(email, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout hfcre = new HorizontalLayout();
		Label  lb_f_creacion = new Label("Fecha de Creacion: ");
		lb_f_creacion.addStyleName(ValoTheme.LABEL_LARGE);
		Label  f_creacion = new Label(usuario.getF_creacion().toGMTString());
		f_creacion.addStyleName(ValoTheme.LABEL_LIGHT);
		hfcre.addComponent(lb_f_creacion);
		hfcre.addComponent(f_creacion);
		hfcre.setComponentAlignment(f_creacion, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout hnaci = new HorizontalLayout();
		Label  lb_f_nacimiento = new Label("Fecha de Nacimiento: ");
		lb_f_nacimiento.addStyleName(ValoTheme.LABEL_LARGE);
		Label  f_nacimiento = new Label(usuario.getF_nacimiento().toGMTString());
		f_nacimiento.addStyleName(ValoTheme.LABEL_LIGHT);
		hnaci.addComponent(lb_f_nacimiento);
		hnaci.addComponent(f_nacimiento);
		hnaci.setComponentAlignment(f_nacimiento, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout hlogi = new HorizontalLayout();
		Label  lb_login = new Label("Usuario: ");
		lb_login.addStyleName(ValoTheme.LABEL_LARGE);
		Label  login = new Label(usuario.getLogin());
		login.addStyleName(ValoTheme.LABEL_LIGHT);
		hlogi.addComponent(lb_login);
		hlogi.addComponent(login);
		hlogi.setComponentAlignment(login, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout hnomb = new HorizontalLayout();
		Label  lb_nombre = new Label("Nombre: ");
		lb_nombre.addStyleName(ValoTheme.LABEL_LARGE);
		Label  nombre = new Label(usuario.getNombre());
		nombre.addStyleName(ValoTheme.LABEL_LIGHT);
		hnomb.addComponent(lb_nombre);
		hnomb.addComponent(nombre);
		hnomb.setComponentAlignment(nombre, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout hrol = new HorizontalLayout();
		Label  lb_rol = new Label("Rol: ");
		lb_rol.addStyleName(ValoTheme.LABEL_LARGE);
		Label  rol = new Label(usuario.getRol().getNombre());
		rol.addStyleName(ValoTheme.LABEL_LIGHT);
		hrol.addComponent(lb_rol);
		hrol.addComponent(rol);
		hrol.setComponentAlignment(rol, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout hsexo= new HorizontalLayout();
		Label  lb_sexo = new Label("Sexo: ");
		Label  sexo;
		if (usuario.isSexo()) {
			sexo = new Label("Mujer");
		}else {
			sexo = new Label("Hombre");
		}
		lb_sexo.addStyleName(ValoTheme.LABEL_LARGE);
		sexo.addStyleName(ValoTheme.LABEL_LIGHT);
		hsexo.addComponent(lb_sexo);
		hsexo.addComponent(sexo);
		hsexo.setComponentAlignment(sexo, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout htel = new HorizontalLayout();
		Label  lb_telefono = new Label("Teléfono: ");
		lb_telefono.addStyleName(ValoTheme.LABEL_LARGE);
		Label telefono = new Label(usuario.getTelefono());
		telefono.addStyleName(ValoTheme.LABEL_LIGHT);
		htel.addComponent(lb_telefono);
		htel.addComponent(telefono);
		htel.setComponentAlignment(telefono, Alignment.MIDDLE_CENTER);
		
		
		vl.addComponent(hnomb);
		vl.addComponent(hapel);
		vl.addComponent(hdire);
		vl.addComponent(htel);
		vl.addComponent(hnaci);
		vl.addComponent(hsexo);
		vl.addComponent(hlogi);
		vl.addComponent(hrol);
		vl.addComponent(hfcre);
		
		return vl;
	}
	
	private Component modificarDatos() {
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		
		Binder<User> binder = new Binder<>();
		        
        TextField usuario = new TextField("Nombre usuario");
        usuario.setValue(SeguridadUtil.getLoginUsuarioLogeado());
        binder.forField(usuario)
        	.asRequired("Introduzca un nombre de usuario")
        	.withValidator(login -> userDao.findByLogin(login) == null || SeguridadUtil.getLoginUsuarioLogeado() == login, "Ya existe un usuario con ese nombre")
        	.bind(User::getLogin, User::setLogin);
       
        vl.addComponent(usuario);
       
		return vl;
	}
	
	@Autowired
	public CuentaUsuarioVista(UserDAO ud) {
		userDao = ud;
	}
	
	@PostConstruct
	void init() {
		usuario = userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado());

		setSizeFull();
		TabSheet tabs = new TabSheet();
		tabs.setSizeFull();
		tabs.addStyleName(ValoTheme.TABSHEET_FRAMED);
		Panel tab1 = new Panel();
		tab1.setContent(misDatos());
		tabs.addTab(tab1, "Mis datos");
		Panel tab2 = new Panel();
		tab2.setContent(modificarDatos());
		tabs.addTab(tab2, "Modificar datos");
		Panel tab3 = new Panel();
		tabs.addTab(tab3, "Cambiar contraseña");
		Panel tab4 = new Panel();
		tabs.addTab(tab4, "Borrar cuenta");
		addComponent(tabs);
	}
}
