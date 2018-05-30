package es.uca.iw.AlquileresVEFHM.vaadin;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.ApartamentoDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;
import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;

@SuppressWarnings("serial")
@SpringView(name = AnuncioVerVista.NOMBRE)
public class AnuncioVerVista extends VerticalLayout implements View {
	public final static String NOMBRE = "ver_anuncio";

	private Apartamento apartamento;
	
	private final ApartamentoDAO aparDao;
	
	@Autowired
	public AnuncioVerVista(ApartamentoDAO ad) {
		aparDao = ad;
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent e) {
		apartamento = aparDao.findById(Integer.parseInt(e.getParameters())).get();
		if(apartamento == null) Page.getCurrent().reload();
		
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		setMargin(true);
		
		Label titulo = new Label(apartamento.getDireccionCompleta());
		titulo.addStyleName(ValoTheme.LABEL_H3);
		addComponent(titulo);
		
		GridLayout panfoto = new GridLayout();
		panfoto.setWidth("70%");
		Image foto = new Image();
		foto.setSource(apartamento.getFotos_apartamento().iterator().next().getStreamResource());
		foto.setSizeFull();
		panfoto.addComponent(foto);
		panfoto.setComponentAlignment(foto, Alignment.MIDDLE_CENTER);
		addComponent(panfoto);
		setComponentAlignment(panfoto, Alignment.MIDDLE_CENTER);
		
		titulo = new Label("Descripción");
		titulo.addStyleName(ValoTheme.LABEL_H3);
		addComponent(titulo);
		
		TextArea contenido = new TextArea();
		contenido.setValue(apartamento.getDescripcion());
		contenido.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
		contenido.setReadOnly(true);
		contenido.setWidth("90%");
		addComponent(contenido);
		
		titulo = new Label("Características");
		titulo.addStyleName(ValoTheme.LABEL_H3);
		addComponent(titulo);
		
		HorizontalLayout hl = new HorizontalLayout();
		FormLayout fl = new FormLayout();
		TextField pro = new TextField("Metros cuadrados");
		pro.setValue(Integer.toString(apartamento.getM2()));
		pro.setReadOnly(true);
		pro.setWidth(80.0f, Unit.PIXELS);
		pro.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		fl.addComponent(pro);
		hl.addComponent(fl);
		
		fl = new FormLayout();
		pro = new TextField("Aseos");
		pro.setReadOnly(true);
		pro.setWidth(80.0f, Unit.PIXELS);
		pro.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		pro.setValue(Integer.toString(apartamento.getAseos()));
		fl.addComponent(pro);
		hl.addComponent(fl);
		
		fl = new FormLayout();
		pro = new TextField("Dormitorios");
		pro.setReadOnly(true);
		pro.setWidth(80.0f, Unit.PIXELS);
		pro.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		pro.setValue(Integer.toString(apartamento.getDormitorios()));
		fl.addComponent(pro);
		hl.addComponent(fl);
		addComponent(hl);
		
		hl = new HorizontalLayout();
		Label op = new Label("Amueblado");
		if(apartamento.isAmueblado()) op.addStyleName(ValoTheme.LABEL_SUCCESS);
		else op.addStyleName(ValoTheme.LABEL_FAILURE);
		op.setWidth(143.0f, Unit.PIXELS);
		hl.addComponent(op);
		
		op = new Label("Ascensor");
		if(apartamento.isAscensor()) op.addStyleName(ValoTheme.LABEL_SUCCESS);
		else op.addStyleName(ValoTheme.LABEL_FAILURE);
		op.setWidth(143.0f, Unit.PIXELS);
		hl.addComponent(op);
		
		op = new Label("Garaje");
		if(apartamento.isGaraje()) op.addStyleName(ValoTheme.LABEL_SUCCESS);
		else op.addStyleName(ValoTheme.LABEL_FAILURE);
		op.setWidth(143.0f, Unit.PIXELS);
		hl.addComponent(op);
		addComponent(hl);
		
		hl = new HorizontalLayout();
		op = new Label("Trastero");
		if(apartamento.isTrastero()) op.addStyleName(ValoTheme.LABEL_SUCCESS);
		else op.addStyleName(ValoTheme.LABEL_FAILURE);
		op.setWidth(143.0f, Unit.PIXELS);
		hl.addComponent(op);
		
		op = new Label("Jardin");
		if(apartamento.isJardin()) op.addStyleName(ValoTheme.LABEL_SUCCESS);
		else op.addStyleName(ValoTheme.LABEL_FAILURE);
		op.setWidth(143.0f, Unit.PIXELS);
		hl.addComponent(op);
		
		op = new Label("Piscina");
		if(apartamento.isPiscina()) op.addStyleName(ValoTheme.LABEL_SUCCESS);
		else op.addStyleName(ValoTheme.LABEL_FAILURE);
		op.setWidth(143.0f, Unit.PIXELS);
		hl.addComponent(op);
		addComponent(hl);
		
		hl = new HorizontalLayout();
		op = new Label("Se aceptan mascotas");
		if(apartamento.isMascotas()) op.addStyleName(ValoTheme.LABEL_SUCCESS);
		else op.addStyleName(ValoTheme.LABEL_FAILURE);
		hl.addComponent(op);
		addComponent(hl);
		
		Boolean login = false;
		if(!SeguridadUtil.isLoggedIn()) login = true;
		if(!login && !SeguridadUtil.getRol().equals("Huesped")) login = true;
		if(login) {
			Button Login = new Button("Inicie sesión como huesped para reservar");
			Login.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
					getUI().getNavigator().navigateTo(LoginVista.NOMBRE);
				}
			});
			addComponent(Login);
		}else {
			Button reservar = new Button("Reservar");
			reservar.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
					getUI().getNavigator().navigateTo(ReservaVista.NOMBRE + "/" + apartamento.getId());
				}
			});
			addComponent(reservar);
		}
	}
	
	@PostConstruct
	void init() {
		setSizeFull();
	}
}
