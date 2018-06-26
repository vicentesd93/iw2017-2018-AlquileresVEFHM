package es.uca.iw.AlquileresVEFHM.vaadin;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.server.StreamVariable;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.dnd.FileDropTarget;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.ApartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.Tipo_apartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Foto_apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Tipo_apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.User;
import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;


@SuppressWarnings({"serial", "deprecation", "unused"})
@SpringView(name = ApartamentoRegistroVista.NOMBRE)
public class ApartamentoRegistroVista extends VerticalLayout implements View {
	public static final String NOMBRE = "registro_apartamento";
	private User usuario;
	
	private final UserDAO userDao;

	private final Tipo_apartamentoDAO taDao;
	
	private final ApartamentoDAO aparDao;
	
	@Autowired
	public ApartamentoRegistroVista(UserDAO ud, Tipo_apartamentoDAO tad, ApartamentoDAO ad) {
		this.userDao = ud;
		this.taDao = tad;
		aparDao = ad;
	}
	
	@PostConstruct
	void init() {
		usuario = userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado());
		setSpacing(true);
		setMargin(true);
		
		Binder<Apartamento> binder = new Binder<>();
		
		Label titulo = new Label("Añadir apartamento");
        titulo.addStyleName(ValoTheme.LABEL_HUGE);
        addComponent(titulo);
        
        FormLayout form = new FormLayout();
        form.setSizeFull();
        form.setMargin(false);
        addComponent(form);
        
        Label seccion = new Label("Información básica");
        seccion.addStyleName(ValoTheme.LABEL_H3);
        seccion.addStyleName(ValoTheme.LABEL_COLORED);
        form.addComponent(seccion);
        
        TextField anfitrion = new TextField("Propietario");
        anfitrion.setValue(usuario.getNombre() + " " + usuario.getApellidos());
        anfitrion.setReadOnly(true);
        anfitrion.setEnabled(false);
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
            		if(file.getType().split("/")[0].equals("image")) {
	            		if(imagenes.isEmpty()) img_label.setValue(file.getFileName());
	            		else img_label.setValue(img_label.getValue() + ", " + file.getFileName());
	            		imagenes.add(file);
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
            			Notification.show("El archivo " + file.getFileName() + " no es una imagen", Notification.TYPE_ERROR_MESSAGE);
            		}
                }else {
            		Notification.show("Tamaño maximo de imagen 5MB", Notification.TYPE_ERROR_MESSAGE);
            	}
            });
        });
        
        Button registrar = new Button("Registrar", event -> {
        	if(imagenes.size() < 1) {
        		Notification.show("El apartamento debe tener al menos 1 foto", Notification.TYPE_ERROR_MESSAGE);
        		binder.validate();
        	}else if(binder.validate().isOk()) {
        		Apartamento a = new Apartamento();
        		Set<Foto_apartamento> fotos = new HashSet<Foto_apartamento>();
        		try {
					binder.writeBean(a);
					a.setAnfitrion(usuario);
					for(Html5File f: imagenes) {
						ByteArrayOutputStream baos = (ByteArrayOutputStream) f.getStreamVariable().getOutputStream();
						Foto_apartamento fa = new Foto_apartamento();
						fa.setFoto(new SerialBlob(baos.toByteArray()));
						fa.setNombre(f.getFileName());
						fa.setApartamento(a);
						fotos.add(fa);
					}
					a.setFotos_apartamento(fotos);
					a.setValoracion(0);
					a.setN_valoraciones(0);
					aparDao.save(a);
					Notification.show("Apartamento registrado correctamente", Notification.TYPE_TRAY_NOTIFICATION).setPosition(Notification.POSITION_CENTERED_TOP);
					getUI().getNavigator().navigateTo(ApartamentosVista.NOMBRE);
				} catch (Exception e) {
	        		Notification.show("Ha ocurrido un error intentelo de nuevo", Notification.TYPE_ERROR_MESSAGE);
				}
        	}
        });
		addComponent(registrar);       
	}
}