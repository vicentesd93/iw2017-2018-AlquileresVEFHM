package es.uca.iw.AlquileresVEFHM.vaadin;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionMessage.Style;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.ApartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.OfertaDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Oferta;
import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;

@SpringView(name = AnuncioVerVista.NOMBRE)
public class AnuncioVerVista extends VerticalLayout implements View {
	public final static String NOMBRE = "ver_anuncio";

	private Apartamento apartamento;
	
	private final ApartamentoDAO aparDao;
	private final OfertaDAO ofertaDao;
	
	@Autowired
	public AnuncioVerVista(ApartamentoDAO ad, OfertaDAO od) {
		aparDao = ad;
		ofertaDao = od;
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
