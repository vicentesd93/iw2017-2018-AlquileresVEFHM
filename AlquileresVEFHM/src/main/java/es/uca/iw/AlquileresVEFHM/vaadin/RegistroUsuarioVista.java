package es.uca.iw.AlquileresVEFHM.vaadin;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.AlquileresVEFHM.DAO.RolDAO;
import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.User;
import es.uca.iw.AlquileresVEFHM.seguridad.UserService;

@SpringView(name = RegistroUsuarioVista.NOMBRE)
public class RegistroUsuarioVista extends VerticalLayout implements View {
	public static final String NOMBRE = "registro_usuario";
	
	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private RolDAO rolDao;
	
	@Autowired
	private UserService us;
	
	public RegistroUsuarioVista() {
		setMargin(true);
        setSpacing(true);

        Binder<User> binder = new Binder<>();
        
        TextField usuario = new TextField("Nombre usuario");
        binder.forField(usuario)
        	.asRequired("Introduzca un nombre de usuario")
        	.withValidator(login -> userDao.findByLogin(login) == null, "Ya existe un usuario con ese nombre")
        	.bind(User::getLogin, User::setLogin);
        addComponent(usuario);

        TextField email = new TextField("Correo electronico");
        binder.forField(email)
        	.asRequired("Introduzca su correo electronico")
        	.withValidator(new EmailValidator("Introduzca un correo electronico válido"))
        	.withValidator(mail -> userDao.findByEmail(mail) == null, "Ya existe un usuario con ese correo electronico.")
        	.bind(User::getEmail, User::setEmail);
        addComponent(email);
        
        PasswordField clave = new PasswordField("Contraseña");
        binder.forField(clave)
        	.asRequired("Introduzca contraseña")
        	.bind(User::getClave, User::setClave);
        addComponent(clave);
        
        PasswordField clave1 = new PasswordField("Repita su contraseña");
        binder.forField(clave1)
        	.asRequired("Repita su contraseña")
        	.withValidator(pass -> pass.equals(clave.getValue()),"Las contraseñas no coinciden")
        	.bind(User::getClave, User::setClave);
        addComponent(clave1);
        
        TextField dni = new TextField("DNI");
        binder.forField(dni)
        	.asRequired("Introduzca su DNI")
        	.bind(User::getDni, User::setDni);
        addComponent(dni);
        
        TextField nombre = new TextField("Nombre");
        binder.forField(nombre)
        	.asRequired("Introduzca su nombre")
        	.bind(User::getNombre, User::setNombre);
        addComponent(nombre);
        
        TextField apellidos = new TextField("Apellidos");
        binder.forField(apellidos)
        	.asRequired("Introduzca sus apellidos")
        	.bind(User::getApellidos, User::setApellidos);
        addComponent(apellidos);
        
        DateField f_nacimiento = new DateField("Fecha de nacimiento");
        f_nacimiento.setValue(LocalDate.now());
        binder.forField(f_nacimiento)
    	.asRequired("Introduzca su fecha de nacimiento")
    	.bind(User::getLDF_nacimiento, User::setLDF_nacimiento);
        addComponent(f_nacimiento);
    
        TextField direccion = new TextField("Dirección");
        binder.forField(direccion)
        	.asRequired("Introduzca su dirección")
        	.bind(User::getDireccion, User::setDireccion);
        addComponent(direccion);
        
        TextField telefono = new TextField("Teléfono");
        binder.forField(telefono)
        	.asRequired("Introduzca su telefono")
        	.bind(User::getTelefono, User::setTelefono);
        addComponent(telefono);
        
        RadioButtonGroup<Boolean> sexo = new RadioButtonGroup<>("Sexo");
        sexo.setItems(Boolean.TRUE, Boolean.FALSE);
        sexo.setValue(Boolean.TRUE);
        sexo.setItemCaptionGenerator(new ItemCaptionGenerator<Boolean>() {
        	@Override
        	public String apply(Boolean item) {
        		return item ? "Mujer" : "Hombre";
        	}
        });
        binder.forField(sexo).bind(User::isSexo, User::setSexo);
        addComponent(sexo);
        
        NativeSelect<Integer> rol = new NativeSelect<>("Tipo usuario");
        rol.setItems(1,2);
        rol.setItemCaptionGenerator(new ItemCaptionGenerator<Integer>() {
			@Override
			public String apply(Integer item) {
				String etiqueta = "";
				switch(item) {
				case 1: etiqueta = "Huesped"; break;
				case 2: etiqueta = "Anfitrión"; break;
				}
				return etiqueta;
			}
        });
        rol.setValue(1);
        rol.setEmptySelectionAllowed(false);
        addComponent(rol);
        
        Button ResistrarBoton = new Button("Registrar", event -> {
        	  if (binder.validate().isOk()) {
        		  User u = new User();
        		  try {
					binder.writeBean(u);
					u.setRol(rolDao.findById(rol.getValue()).get());
					us.save(u);
					Notification.show("Usuario registrado");
				} catch (ValidationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		  
        	  }else {
        		  clave.setValue("");
        		  clave1.setValue("");
        		  Notification.show("Revise los datos e intentelo de nuevo");
        	  }
        });
        addComponent(ResistrarBoton);
	}
}
