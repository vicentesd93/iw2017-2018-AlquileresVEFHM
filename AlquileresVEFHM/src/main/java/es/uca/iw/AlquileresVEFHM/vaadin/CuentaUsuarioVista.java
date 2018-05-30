package es.uca.iw.AlquileresVEFHM.vaadin;

import java.time.ZoneId;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.RadioButtonGroup;
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
	private PasswordEncoder passwordEncoder;
	
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
		Label  f_creacion = new Label(usuario.getF_creacion().toLocaleString());
		f_creacion.addStyleName(ValoTheme.LABEL_LIGHT);
		hfcre.addComponent(lb_f_creacion);
		hfcre.addComponent(f_creacion);
		hfcre.setComponentAlignment(f_creacion, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout hnaci = new HorizontalLayout();
		Label  lb_f_nacimiento = new Label("Fecha de Nacimiento: ");
		lb_f_nacimiento.addStyleName(ValoTheme.LABEL_LARGE);
		Label  f_nacimiento = new Label(usuario.getF_nacimiento().toLocaleString());
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
		        
        TextField nombreUser = new TextField("Nombre");
        nombreUser.setValue(userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado()).getNombre());
        binder.forField(nombreUser)
        	.asRequired("Introduzca un nombre de usuario")
        	.bind(User::getNombre, User::setNombre);
       
        vl.addComponent(nombreUser);
        
        TextField apellidosUser = new TextField("Apellidos");
        apellidosUser.setValue(userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado()).getApellidos());
        binder.forField(apellidosUser)
        	.asRequired("Introduzca los apellidos")
        	.bind(User::getApellidos, User::setApellidos);
       
        vl.addComponent(apellidosUser);
        
        TextField emailUser = new TextField("Correo electronico");
        emailUser.setValue(userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado()).getEmail());
        binder.forField(emailUser)
        	.asRequired("Introduzca su correo electronico")
        	.withValidator(new EmailValidator("Introduzca un correo electronico válido"))
        	.bind(User::getEmail, User::setEmail);

        vl.addComponent(emailUser);
        
        TextField direccionUser = new TextField("Dirección");
        direccionUser.setValue(userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado()).getDireccion());
        binder.forField(direccionUser)
        	.asRequired("Introduzca la dirección")
        	.bind(User::getDireccion, User::setDireccion);
       
        vl.addComponent(direccionUser);
        
        TextField telUser = new TextField("Telefono");
        telUser.setValue(userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado()).getTelefono());
        binder.forField(telUser)
        	.asRequired("Introduzca el telefono")
        	.bind(User::getTelefono, User::setTelefono);
       
        vl.addComponent(telUser);
        
        DateField fechNacUser = new DateField("Fecha de nacimiento");
        fechNacUser.setValue(userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado()).getLDF_nacimiento());
        binder.forField(fechNacUser)
    		.asRequired("Introduzca su fecha de nacimiento")
    		.bind(User::getLDF_nacimiento, User::setLDF_nacimiento);
       
        vl.addComponent(fechNacUser);
        

        RadioButtonGroup<Boolean> sexoUser = new RadioButtonGroup<>("Sexo");
        sexoUser.setItems(Boolean.TRUE, Boolean.FALSE);
        sexoUser.setSelectedItem(userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado()).isSexo());
        sexoUser.setItemCaptionGenerator(new ItemCaptionGenerator<Boolean>() {
			private static final long serialVersionUID = 1L;
			@Override
        	public String apply(Boolean item) {
        		return item ? "Mujer" : "Hombre";
        	}
        });
        binder.forField(sexoUser).bind(User::isSexo, User::setSexo);
        vl.addComponent(sexoUser);
        
        Button Guardar = new Button("Guardar", event -> {
      	  if (binder.validate().isOk()) {
      		  User u = userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado());
      		  try {
					binder.writeBean(u);
					u.setNombre(nombreUser.getValue());
					u.setApellidos(apellidosUser.getValue());
					u.setDireccion(direccionUser.getValue());
					u.setTelefono(telUser.getValue());
					u.setF_nacimiento(Date.from(fechNacUser.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
					u.setSexo(sexoUser.getValue());
					userDao.save(u);
					Notification.show("Usuario modificado");
					Page.getCurrent().reload();
				} catch (ValidationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      		  
      	  }else {
      		  Notification.show("Revise los datos e intentelo de nuevo");
      	  }
      });
      vl.addComponent(Guardar);
        
		return vl;
	}
	
	private Component modificarClave() {
		VerticalLayout vl = new VerticalLayout();
		passwordEncoder = new BCryptPasswordEncoder();
		vl.setSizeFull();	
		
		Binder<User> binder = new Binder<>();
		PasswordField claveAntigua = new PasswordField("Antigua contraseña");
        claveAntigua.setValue("");
        claveAntigua.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        claveAntigua.setIcon(FontAwesome.LOCK);
        binder.forField(claveAntigua)
        	.asRequired("Introduzca la contraseña antigua")
        	.withValidator(clave -> passwordEncoder.matches(claveAntigua.getValue(), userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado()).getClave()), "La clave antigua no es correcta")
        	.bind(User::getClave, User::setClave);

        vl.addComponent(claveAntigua);

        PasswordField claveNueva = new PasswordField("Nueva contraseña");   
        claveNueva.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        claveNueva.setIcon(FontAwesome.LOCK);
        binder.forField(claveNueva)
        	.asRequired("Introduzca la nueva contraseña")
        	.bind(User::getClave, User::setClave);
        
        vl.addComponent(claveNueva);
        
        PasswordField claveNueva1 = new PasswordField("Repita nueva contraseña");
        claveNueva1.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        claveNueva1.setIcon(FontAwesome.LOCK);
        binder.forField(claveNueva1)
        	.asRequired("Introduzca la nueva contraseña")
        	.withValidator(pass -> pass.equals(claveNueva.getValue()),"Las contraseñas no coinciden")
        	.bind(User::getClave, User::setClave);
      
        vl.addComponent(claveNueva1);
        
        Button Guardar = new Button("Guardar", event -> {
      	  if (binder.validate().isOk()) {
      		  User u = userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado());
      		  try {
					binder.writeBean(u);
					u.setClave(passwordEncoder.encode(claveNueva.getValue()));
					userDao.save(u);
					Notification.show("Usuario modificado");
				} catch (ValidationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      		  
      	  }else {
      		  claveNueva.setValue("");
    		  claveNueva1.setValue("");
      		  Notification.show("Revise los datos e intentelo de nuevo");
      	  }
      });
      vl.addComponent(Guardar);
        
		return vl;
	}
	
	private Component borrarCuenta() {
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		
		Binder<User> binder = new Binder<>();
        
        Button Eliminar = new Button("Eliminar", event -> {
      		User u = userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado());
      		//System.out.println("------------------->"+userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado()).getNombre());
      		//userDao.delete(u);
      		userDao.deleteById(userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado()).getId());
			Notification.show("Usuario Eliminado");
			
			//cerramos sesion
			getUI().getPage().reload();
			getSession().close();
      });
      Eliminar.addStyleName(ValoTheme.BUTTON_DANGER);
      vl.addComponent(Eliminar);
        
		return vl;
	}
	
	@Autowired
	public CuentaUsuarioVista(UserDAO ud) {
		userDao = ud;
	}
	
	@PostConstruct
	void init() {
		if(!SeguridadUtil.isLoggedIn()) {
			Notification.show("No tiene permisos para acceder a la página", Notification.TYPE_ERROR_MESSAGE);
			Page.getCurrent().open("/#!login", null);
			return;
		}
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
		Panel tab3 = new Panel(modificarClave());
		tabs.addTab(tab3, "Cambiar contraseña");
		Panel tab4 = new Panel(borrarCuenta());
		tabs.addTab(tab4, "Borrar cuenta");
		addComponent(tabs);
	}
}