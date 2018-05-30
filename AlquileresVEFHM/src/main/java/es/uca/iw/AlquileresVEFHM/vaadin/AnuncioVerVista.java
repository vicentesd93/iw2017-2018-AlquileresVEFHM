package es.uca.iw.AlquileresVEFHM.vaadin;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
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
		GridLayout panfoto = new GridLayout();
		panfoto.setWidth("70%");
		
		Image foto = new Image();
		foto.setSource(apartamento.getFotos_apartamento().iterator().next().getStreamResource());
		foto.setSizeFull();
		panfoto.addComponent(foto);
		panfoto.setComponentAlignment(foto, Alignment.MIDDLE_CENTER);
		addComponent(panfoto);
		setComponentAlignment(panfoto, Alignment.MIDDLE_CENTER);
		
		Label titulo = new Label("Descripción");
		titulo.addStyleName(ValoTheme.LABEL_H3);
		addComponent(titulo);
		
		Label contenido = new Label(apartamento.getDescripcion());
		addComponent(contenido);
		
		addComponent(new Label("Aqui iria los servicios"));
		
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
