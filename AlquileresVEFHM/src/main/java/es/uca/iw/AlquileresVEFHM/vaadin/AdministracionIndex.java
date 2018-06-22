package es.uca.iw.AlquileresVEFHM.vaadin;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.server.StreamVariable;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.EditorSaveEvent;
import com.vaadin.ui.components.grid.EditorSaveListener;
import com.vaadin.ui.dnd.FileDropTarget;
import com.vaadin.ui.themes.ValoTheme;
import es.uca.iw.AlquileresVEFHM.DAO.ApartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.RolDAO;
import es.uca.iw.AlquileresVEFHM.DAO.Tipo_apartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Foto_apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Tipo_apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.User;
import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;
import es.uca.iw.AlquileresVEFHM.seguridad.UserService;

@SpringView(name = AdministracionIndex.NOMBRE)
public class AdministracionIndex extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String NOMBRE = "AdministracionIndex";
	private VerticalLayout vl = new VerticalLayout();
	private VerticalLayout vlDinamico = new VerticalLayout();
	private VerticalLayout vlEstatico = new VerticalLayout();
	private RadioButtonGroup<String> gestionar;
	private RadioButtonGroup<String> crud;
	Grid<User> gridUser = new Grid<User>();
	
	private UserDAO userDao;
	private RolDAO rolDao;
	private Tipo_apartamentoDAO taDao;
	private ApartamentoDAO aparDao;
	private UserService us;
	
	@Autowired
	public AdministracionIndex (UserDAO ud, RolDAO rd, UserService us,Tipo_apartamentoDAO ta,ApartamentoDAO ap) {
		userDao = ud;
		rolDao = rd;
		taDao = ta;
		aparDao = ap;
		this.us = us;
	}
	private Component radioGestionar() {
		HorizontalLayout hl = new HorizontalLayout();
		
		Label titulo = new Label("Administracion General");
		titulo.addStyleName(ValoTheme.LABEL_HUGE);
		vl.addComponent(titulo);
		
		gestionar = new RadioButtonGroup<>("Gestionar: ");
		gestionar.setItems("Usuarios",/* "Rol de Usuario",*/ "Apartamentos"/*, "Tipos de Apartamento"
						,"Fotos de Apartamento", "Ofertas", "Reservas", "Facturas","Metodos de pago"*/);
		
		gestionar.addValueChangeListener( event -> hl.addComponent(Crud()));
		
		hl.addComponent(gestionar);
		return hl;
	}
	private Component Crud() {
		if (crud == null) {
			crud = new RadioButtonGroup<>("Accion: ");
			crud.setItems("Crear", "Mostrar/Modificar/Eliminar");
			crud.addValueChangeListener( event -> accionMostrar());
			return crud;	
		}else {
			return new RadioButtonGroup<String>();
		}
	}
	private void accionMostrar() {
		VerticalLayout vlAux = null;
		HorizontalLayout hlAux = null;
		
		if (gestionar.getSelectedItem().get().equals("Usuarios")) {
			if (crud.getSelectedItem().get().equals("Crear")) {
				vlDinamico.removeAllComponents();
				hlAux = crearUsuario();
			}else if(crud.getSelectedItem().get().equals("Mostrar/Modificar/Eliminar")) {
				vlDinamico.removeAllComponents();
				vlAux = modificarUsuarios();
			}
		}/*else if(gestionar.getSelectedItem().get().equals("Rol de Usuario")) {
			if (crud.getSelectedItem().get().equals("Crear")) {
				
			}else if(crud.getSelectedItem().get().equals("Mostrar/Modificar/Eliminar")) {
				
			}

		}*/else if(gestionar.getSelectedItem().get().equals("Apartamentos")) {
			if (crud.getSelectedItem().get().equals("Crear")) {
				vlDinamico.removeAllComponents();
				vlAux = crearApartamento();
			}else if(crud.getSelectedItem().get().equals("Mostrar/Modificar/Eliminar")) {
				vlDinamico.removeAllComponents();
				vlAux = modificarApartamentos();
			}

		}/*else if(gestionar.getSelectedItem().get().equals("Tipos de Apartamento")) {
			if (crud.getSelectedItem().get().equals("Crear")) {
				
			}else if(crud.getSelectedItem().get().equals("Mostrar/Modificar/Eliminar")) {
				
			}

		}else if(gestionar.getSelectedItem().get().equals("Fotos de Apartamento")) {
			if (crud.getSelectedItem().get().equals("Crear")) {
				
			}else if(crud.getSelectedItem().get().equals("Mostrar/Modificar/Eliminar")) {
				
			}
		}else if(gestionar.getSelectedItem().get().equals("Ofertas")) {
			if (crud.getSelectedItem().get().equals("Crear")) {
				
			}else if(crud.getSelectedItem().get().equals("Mostrar/Modificar/Eliminar")) {
				
			}
		}else if(gestionar.getSelectedItem().get().equals("Reservas")) {
			if (crud.getSelectedItem().get().equals("Crear")) {
				
			}else if(crud.getSelectedItem().get().equals("Mostrar/Modificar/Eliminar")) {
				
			}
		}else if(gestionar.getSelectedItem().get().equals("Facturas")) {
			if (crud.getSelectedItem().get().equals("Crear")) {
				
			}else if(crud.getSelectedItem().get().equals("Mostrar/Modificar/Eliminar")) {
				
			}
		}else if(gestionar.getSelectedItem().get().equals("Metodos de pago")) {
			if (crud.getSelectedItem().get().equals("Crear")) {
				
			}else if(crud.getSelectedItem().get().equals("Mostrar/Modificar/Eliminar")) {
				
			}
		}*/
		
		if (vlAux == null) {
			vlDinamico.addComponent(hlAux);
		}else {
			vlDinamico.addComponent(vlAux);
		}
	}
	private VerticalLayout modificarApartamentos() {
		VerticalLayout vl = new VerticalLayout();
		
		Set<Apartamento> apartamentos = aparDao.findAll();
		
		TextField Edescripcion = new TextField();
		TextField Edireccion = new TextField();
		TextField Epoblacion = new TextField();
		TextField Epais = new TextField();
		
		Grid<Apartamento> grid = new Grid<>();
		grid.setSizeFull();
		grid.setItems(apartamentos);
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addColumn(Apartamento::getDescripcion).setCaption("Descripción").setEditorComponent(Edescripcion, Apartamento::setDescripcion);
		grid.addColumn(Apartamento::getDireccion).setCaption("Dirección").setEditorComponent(Edireccion, Apartamento::setDireccion);
		grid.addColumn(Apartamento::getPoblacion).setCaption("Población").setEditorComponent(Epoblacion, Apartamento::setPoblacion);
		grid.addColumn(Apartamento::getPais).setCaption("País").setEditorComponent(Epais, Apartamento::setPais);
		grid.getEditor().setEnabled(true);
		grid.getEditor().setSaveCaption("Guardar");
		grid.getEditor().setCancelCaption("Cancelar");
		grid.getEditor().addSaveListener(new EditorSaveListener<Apartamento>() {
			@Override
			public void onEditorSave(EditorSaveEvent<Apartamento> event) {
				aparDao.save(event.getBean());
				Notification.show("Apartamento actualizado", Notification.TYPE_TRAY_NOTIFICATION).setPosition(Notification.POSITION_CENTERED_TOP);;
			}
		});
		
		CssLayout opciones = new CssLayout();
		opciones.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		opciones.setEnabled(false);
		
		grid.addSelectionListener(new SelectionListener<Apartamento>() {
			@Override
			public void selectionChange(SelectionEvent<Apartamento> event) {
				opciones.setEnabled(event.getFirstSelectedItem().isPresent());
			}
		});
		
		
		opciones.addComponent(crearBotonOpcion("Eliminar", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show("Apartamento eliminado correctamente", Notification.TYPE_TRAY_NOTIFICATION);
				aparDao.deleteById(grid.asSingleSelect().getOptionalValue().get().getId());
				grid.setItems(aparDao.findAll());
			}
		}));
		
		vl.addComponent(grid);
		vl.addComponent(opciones);
		return vl;
	}
	private VerticalLayout crearApartamento() {
		VerticalLayout vl = new VerticalLayout();
		
		vl.setSpacing(true);
		vl.setMargin(true);
		
		Binder<Apartamento> binder = new Binder<>();
		
		Label titulo = new Label("Añadir apartamento");
        titulo.addStyleName(ValoTheme.LABEL_HUGE);
        vl.addComponent(titulo);
        
        FormLayout form = new FormLayout();
        form.setSizeFull();
        form.setMargin(false);
        vl.addComponent(form);
        
        Label seccion = new Label("Información básica");
        seccion.addStyleName(ValoTheme.LABEL_H3);
        seccion.addStyleName(ValoTheme.LABEL_COLORED);
        form.addComponent(seccion);
        //FALTA PONER ANFITRIONES!!!!!
        NativeSelect<String> anfitrion = new NativeSelect<>("Anfitrion");
        Set<String> anfitriones = new HashSet<String>(); 
        for (Iterator<User> it = userDao.findAll().iterator(); it.hasNext();) {
        	User u = it.next();

        	if(u.getRol().getNombre().equals("Anfitrion")) {
        		anfitriones.add(u.getLogin());
        		System.out.println("------------------------>ENTRA");
        	}
        }
        
        anfitrion.setItems(anfitriones);
        
        anfitrion.setEmptySelectionAllowed(false);
        //anfitrion.setSelectedItem(anfitriones.iterator().next());
        form.addComponent(anfitrion);
        
        NativeSelect<Tipo_apartamento> t_apar = new NativeSelect<>("Tipo apartamento");
        Set<Tipo_apartamento> tipos_a = taDao.findAll();
        t_apar.setItems(tipos_a);
        t_apar.setEmptySelectionAllowed(false);
        t_apar.setSelectedItem(tipos_a.iterator().next());
        form.addComponent(t_apar);
        binder.forField(t_apar).bind(Apartamento::getTipo_apartamento, Apartamento::setTipo_apartamento);
        
        TextArea descripcion = new TextArea("Descripción");
        form.addComponent(descripcion);
        binder.forField(descripcion)
        	.asRequired("Introduzca la descripción del inmueble")
        	.bind(Apartamento::getDescripcion, Apartamento::setDescripcion);
        
        seccion = new Label("Ubicación");
        seccion.addStyleName(ValoTheme.LABEL_H3);
        seccion.addStyleName(ValoTheme.LABEL_COLORED);
        form.addComponent(seccion);
        
        TextField direccion = new TextField("Dirección");
        form.addComponent(direccion);
        binder.forField(direccion)
	    	.asRequired("Introduzca la dirección del inmueble")
	    	.bind(Apartamento::getDireccion, Apartamento::setDireccion);
        
        TextField poblacion = new TextField("Población");
        form.addComponent(poblacion);
        binder.forField(poblacion)
	    	.asRequired("Introduzca la población del inmueble")
	    	.bind(Apartamento::getPoblacion, Apartamento::setPoblacion);
        
        TextField pais = new TextField("País");
        form.addComponent(pais);
        binder.forField(pais)
	    	.asRequired("Introduzca el país donde se encuentra del inmueble")
	    	.bind(Apartamento::getPais, Apartamento::setPais);
        
        seccion = new Label("Datos");
        seccion.addStyleName(ValoTheme.LABEL_H3);
        seccion.addStyleName(ValoTheme.LABEL_COLORED);
        form.addComponent(seccion);
        
        TextField dormitorios = new TextField("Dormitorios");
        form.addComponent(dormitorios);
        binder.forField(dormitorios)
	    	.asRequired("Introduzca el número de dormitorios del inmueble")
	    	.withConverter(Integer::valueOf, String::valueOf, "Debe introducir un numero")
	    	.withValidator(integer -> integer >= 0, "Debe introducir un valor positivo")
	    	.bind(Apartamento::getDormitorios, Apartamento::setDormitorios);
        
        TextField aseos = new TextField("Aseos");
        form.addComponent(aseos);
        binder.forField(aseos)
	    	.asRequired("Introduzca el número de aseos del inmueble")
	    	.withConverter(Integer::valueOf, String::valueOf, "Debe introducir un numero")
	    	.withValidator(integer -> integer >= 0, "Debe introducir un valor positivo")
	    	.bind(Apartamento::getAseos, Apartamento::setAseos);
        
        TextField m2 = new TextField("M2");
        form.addComponent(m2);
        binder.forField(m2)
	    	.asRequired("Introduzca los metros cuadrados del inmueble")
	    	.withConverter(Integer::valueOf, String::valueOf, "Debe introducir un numero")
	    	.withValidator(integer -> integer >= 0, "Debe introducir un valor positivo")
	    	.bind(Apartamento::getM2, Apartamento::setM2);
        
        CheckBox amueblado = new CheckBox("Amueblado", false);
        form.addComponent(amueblado);
        binder.forField(amueblado)
    		.bind(Apartamento::isAmueblado, Apartamento::setAmueblado);
        
        CheckBox ascensor = new CheckBox("Ascensor", false);
        form.addComponent(ascensor);
        binder.forField(ascensor)
    		.bind(Apartamento::isAscensor, Apartamento::setAscensor);
        
        CheckBox garaje = new CheckBox("Garaje", false);
        form.addComponent(garaje);
        binder.forField(garaje)
    		.bind(Apartamento::isGaraje, Apartamento::setGaraje);
        
        CheckBox trastero = new CheckBox("Trastero", false);
        form.addComponent(trastero);
        binder.forField(trastero)
    		.bind(Apartamento::isTrastero, Apartamento::setTrastero);
        
        CheckBox jardin = new CheckBox("Jardin", false);
        form.addComponent(jardin);
        binder.forField(jardin)
    	.bind(Apartamento::isJardin, Apartamento::setJardin);
        
        CheckBox piscina = new CheckBox("Piscina", false);
        form.addComponent(piscina);
        binder.forField(piscina)
    		.bind(Apartamento::isPiscina, Apartamento::setPiscina);
        
        CheckBox mascotas = new CheckBox("Mascotas", false);
        form.addComponent(mascotas);
        binder.forField(mascotas)
    		.bind(Apartamento::isMascotas, Apartamento::setMascotas);
        
        seccion = new Label("Imágenes");
        seccion.addStyleName(ValoTheme.LABEL_H3);
        seccion.addStyleName(ValoTheme.LABEL_COLORED);
        form.addComponent(seccion);
        
        Label dropArea = new Label("Arrastra las fotos hasta aqui");
        Styles estilo = Page.getCurrent().getStyles();
        estilo.add(".drop-area {border-radius: 69px 69px 69px 69px;" + 
        		"-moz-border-radius: 69px 69px 69px 69px;" + 
        		"-webkit-border-radius: 69px 69px 69px 69px;" + 
        		"border: 9px dashed #adadad;}");
        dropArea.addStyleName("drop-area");
        Label img_label = new Label();
        form.addComponent(dropArea);
        ProgressBar progress = new ProgressBar();
        progress.setIndeterminate(true);
        progress.setVisible(false);
        form.addComponent(progress);
        form.addComponent(img_label);
        ArrayList<Html5File> imagenes = new ArrayList<Html5File>();
        FileDropTarget<Label> dropTarget = new FileDropTarget<>(dropArea, event -> {
            Collection<Html5File> files = event.getFiles();
            files.forEach(file -> {
            	if(file.getFileSize() <= 5 * 1024 * 1024) {
            		imagenes.add(file);
            		img_label.setValue(img_label.getValue() + ", " + file.getFileName());
            		final ByteArrayOutputStream bas = new ByteArrayOutputStream();
                    final StreamVariable streamVariable = new StreamVariable() {

                         @Override
                         public ByteArrayOutputStream getOutputStream() {
                              return bas;
                         }

                         @Override
                         public boolean listenProgress() {
                              return false;
                         }

                         @Override
                         public void onProgress(
                              final StreamingProgressEvent event) {
                         }

                         @Override
                         public void streamingStarted(
                              final StreamingStartEvent event) {
                         }

                         @Override
                         public void streamingFinished(
                              final StreamingEndEvent event) {
                              progress.setVisible(false);
                         }

                         @Override
                         public void streamingFailed(
                              final StreamingErrorEvent event) {
                              progress.setVisible(false);
                         }

                         @Override
                         public boolean isInterrupted() {
                              return false;
                         }
                     };
                     file.setStreamVariable(streamVariable);
                     progress.setVisible(true);
                }else {
            		Notification.show("Tamaño maximo de imagen 5MB", Notification.TYPE_ERROR_MESSAGE);
            	}
            });
        });
        
        Button registrar = new Button("Registrar", event -> {
        	if(binder.validate().isOk()) {
        		Apartamento a = new Apartamento();
        		Set<Foto_apartamento> fotos = new HashSet<Foto_apartamento>();
        		try {
					binder.writeBean(a);
					a.setAnfitrion(userDao.findByLogin(anfitrion.getSelectedItem().get()));
					for(Html5File f: imagenes) {
						ByteArrayOutputStream baos = (ByteArrayOutputStream) f.getStreamVariable().getOutputStream();
						Foto_apartamento fa = new Foto_apartamento();
						fa.setFoto(new SerialBlob(baos.toByteArray()));
						fa.setNombre(f.getFileName());
						fa.setApartamento(a);
						fotos.add(fa);
					}
					Notification.show("Apartamento registrado correctamente");
					a.setFotos_apartamento(fotos);
					aparDao.save(a);
				} catch (ValidationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SerialException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        });
        registrar.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        
		vl.addComponent(registrar);
		return vl;
	}
	private HorizontalLayout crearUsuario() {
		HorizontalLayout hl = new HorizontalLayout();
		VerticalLayout vl1 = new VerticalLayout();
		VerticalLayout vl2 = new VerticalLayout();
				
		Label titulo = new Label("Crear Nuevo Usuario");
		titulo.addStyleName(ValoTheme.LABEL_H3);
		vl1.addComponent(titulo);
		
		vl2.addComponent(new Label());vl2.addComponent(new Label());
		
		Binder<User> binder = new Binder<>();
        
        TextField usuario = new TextField("Nombre usuario");
        binder.forField(usuario)
        	.asRequired("Introduzca un nombre de usuario")
        	.withValidator(login -> userDao.findByLogin(login) == null, "Ya existe un usuario con ese nombre")
        	.bind(User::getLogin, User::setLogin);
        vl1.addComponent(usuario);

        TextField email = new TextField("Correo electronico");
        binder.forField(email)
        	.asRequired("Introduzca su correo electronico")
        	.withValidator(new EmailValidator("Introduzca un correo electronico válido"))
        	.withValidator(mail -> userDao.findByEmail(mail) == null, "Ya existe un usuario con ese correo electronico.")
        	.bind(User::getEmail, User::setEmail);
        vl1.addComponent(email);
        
        PasswordField clave = new PasswordField("Contraseña");
        binder.forField(clave)
        	.asRequired("Introduzca contraseña")
        	.bind(User::getClave, User::setClave);
        vl1.addComponent(clave);
        
        PasswordField clave1 = new PasswordField("Repita su contraseña");
        binder.forField(clave1)
        	.asRequired("Repita su contraseña")
        	.withValidator(pass -> pass.equals(clave.getValue()),"Las contraseñas no coinciden")
        	.bind(User::getClave, User::setClave);
        vl1.addComponent(clave1);
        
        TextField dni = new TextField("DNI");
        binder.forField(dni)
        	.asRequired("Introduzca su DNI")
        	.bind(User::getDni, User::setDni);
        vl1.addComponent(dni);
        
        TextField nombre = new TextField("Nombre");
        binder.forField(nombre)
        	.asRequired("Introduzca su nombre")
        	.bind(User::getNombre, User::setNombre);
        vl2.addComponent(nombre);
        
        TextField apellidos = new TextField("Apellidos");
        binder.forField(apellidos)
        	.asRequired("Introduzca sus apellidos")
        	.bind(User::getApellidos, User::setApellidos);
        vl2.addComponent(apellidos);
        
        DateField f_nacimiento = new DateField("Fecha de nacimiento");
        binder.forField(f_nacimiento)
    	.asRequired("Introduzca su fecha de nacimiento")
    	.bind(User::getLDF_nacimiento, User::setLDF_nacimiento);
        vl2.addComponent(f_nacimiento);
    
        TextField direccion = new TextField("Dirección");
        binder.forField(direccion)
        	.asRequired("Introduzca su dirección")
        	.bind(User::getDireccion, User::setDireccion);
        vl2.addComponent(direccion);
        
        TextField telefono = new TextField("Teléfono");
        binder.forField(telefono)
        	.asRequired("Introduzca su telefono")
        	.bind(User::getTelefono, User::setTelefono);
        vl2.addComponent(telefono);
        
        RadioButtonGroup<Boolean> sexo = new RadioButtonGroup<>("Sexo");
        sexo.setItems(Boolean.TRUE, Boolean.FALSE);
        sexo.setValue(Boolean.TRUE);
        sexo.setItemCaptionGenerator(new ItemCaptionGenerator<Boolean>() {
			private static final long serialVersionUID = 1L;
			@Override
        	public String apply(Boolean item) {
        		return item ? "Mujer" : "Hombre";
        	}
        });
        binder.forField(sexo).bind(User::isSexo, User::setSexo);
        vl2.addComponent(sexo);
        
        NativeSelect<Integer> rol = new NativeSelect<>("Tipo usuario");
        rol.setItems(1,2);
        rol.setItemCaptionGenerator(new ItemCaptionGenerator<Integer>() {
			private static final long serialVersionUID = 1L;
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
        vl2.addComponent(rol);
        
        vl1.addComponent(new Label());vl1.addComponent(new Label());
        vl1.addComponent(new Label());
        
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
        ResistrarBoton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        
        vl1.addComponent(ResistrarBoton);
        hl.addComponent(vl1);
        hl.addComponent(vl2);
        
        return hl;
	}
	private VerticalLayout modificarUsuarios() {
		VerticalLayout vlPrincipal = new VerticalLayout();
		vlPrincipal.setSizeFull();
		
		Label titulo = new Label("Usuarios");
		titulo.addStyleName(ValoTheme.LABEL_HUGE);
		/*Editar*/
		
		TextField Eapellidos = new TextField();
		TextField Edireccion = new TextField();
		TextField Edni = new TextField();
		TextField Eemail = new TextField();
		//DateField Efecnac= new DateField();
		TextField Eusuario= new TextField();
		TextField Enombre = new TextField();
		//RadioButtonGroup<Boolean> Esexo = new RadioButtonGroup<Boolean>();
		TextField Etelefono = new TextField();
		
		gridUser.setItems((Collection<User>) userDao.findAll());
		gridUser.setSizeFull();		
		gridUser.setSelectionMode(SelectionMode.SINGLE);
		gridUser.removeAllColumns();
		gridUser.addColumn(User::getId).setCaption("Id");
		gridUser.addColumn(User::isActivo).setCaption("Activo");
		gridUser.addColumn(User::getApellidos).setCaption("Apellidos").setEditorComponent(Eapellidos, User::setApellidos);
		gridUser.addColumn(User::getDireccion).setCaption("Direccion").setEditorComponent(Edireccion, User::setDireccion);
		gridUser.addColumn(User::getDni).setCaption("DNI").setEditorComponent(Edni, User::setDni);
		gridUser.addColumn(User::getEmail).setCaption("Email").setEditorComponent(Eemail, User::setEmail);
		// NO SE PUEDE EDITAR FECHA DE NACIMIENTO
		gridUser.addColumn(User::getF_nacimiento).setCaption("Fecha de Nacimiento")/*.setEditorComponent(Efecnac, User::setF_nacimiento)*/; 
		gridUser.addColumn(User::getUsername).setCaption("Usuario").setEditorComponent(Eusuario, User::setLogin);
		gridUser.addColumn(User::getNombre).setCaption("Nombre").setEditorComponent(Enombre, User::setNombre);
		//gridUser.addColumn(User::isSexo).setCaption("Sexo").setEditorComponent(Esexo, User::setSexo);
		gridUser.addColumn(User::getTelefono).setCaption("Telefono").setEditorComponent(Etelefono, User::setTelefono);
		gridUser.getEditor().setEnabled(true);
		gridUser.getEditor().setSaveCaption("Guardar");
		gridUser.getEditor().setCancelCaption("Cancelar");
		gridUser.getEditor().addSaveListener(new EditorSaveListener<User>() {
			@Override
			public void onEditorSave(EditorSaveEvent<User> event) {
				userDao.save(event.getBean());
				Notification.show("Usuario actualizado");
				
			}
		});
		
		CssLayout opciones = new CssLayout();
		opciones.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		opciones.setEnabled(false);
		
		gridUser.addSelectionListener(new SelectionListener<User>() {
			@Override
			public void selectionChange(SelectionEvent<User> event) {
				opciones.setEnabled(event.getFirstSelectedItem().isPresent());
			}
		});
		
		
		opciones.addComponent(crearBotonOpcion("Eliminar", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show("Usuario eliminado correctamente", Notification.TYPE_TRAY_NOTIFICATION);
				userDao.deleteById(gridUser.asSingleSelect().getOptionalValue().get().getId());
				gridUser.setItems((Collection<User>) userDao.findAll());
			}
		}));
		
		vlPrincipal.addComponent(titulo);
		vlPrincipal.addComponent(gridUser);
		vlPrincipal.addComponent(opciones);
		
		return vlPrincipal;
	}
	private Button crearBotonOpcion(String caption, ClickListener clicklistener) {
		Button boton = new Button(caption);
		boton.addStyleName(ValoTheme.BUTTON_TINY);
		boton.addClickListener(clicklistener);
		return boton;
	}
	
	@PostConstruct
	void init() {
		if(!SeguridadUtil.isLoggedIn() || (SeguridadUtil.isLoggedIn() && !SeguridadUtil.getLoginUsuarioLogeado().equals("admin"))) {
			Notification.show("No tiene permisos para acceder a la página", Notification.TYPE_ERROR_MESSAGE);
			Page.getCurrent().open("/#!login", null);
			return;
		}
		setWidth("100%");
		vl.setSizeFull();
		vlEstatico.addComponent(radioGestionar());
		vl.addComponent(vlEstatico);
		vl.addComponent(vlDinamico);
		
		addComponent(vl);
	}
}