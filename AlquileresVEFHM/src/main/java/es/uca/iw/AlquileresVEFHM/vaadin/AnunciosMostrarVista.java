package es.uca.iw.AlquileresVEFHM.vaadin;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.ApartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.User;
import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;

@SuppressWarnings("serial")
@SpringView(name = AnunciosMostrarVista.NOMBRE)
public class AnunciosMostrarVista extends VerticalLayout implements View {
	public static final String NOMBRE = "";
	
	private final ApartamentoDAO aparDao;
	private final UserDAO userDao;
	
	private List<Apartamento> apartamentos;
	private Integer anuncios = 3;
	
	@Autowired
	public AnunciosMostrarVista(ApartamentoDAO ad, UserDAO ud) {
		aparDao = ad;
		userDao = ud;
	}
	
	@PostConstruct
	void init() {
		apartamentos = aparDao.apartamentosOfertados();
		
		setWidth("100%");
		mostrar(1, this);
	}
	
	void mostrar(int pagina, VerticalLayout vl) {
		int ind = (pagina - 1) * anuncios;
		vl.removeAllComponents();
		vl.setWidth("100%");
		for(int i = 0; i < anuncios; i++) {
			if(ind < apartamentos.size()) {
				HorizontalLayout hl = new HorizontalLayout();
				hl.setWidth(580.0f, Unit.PIXELS);
				hl.setHeight(200.0f, Unit.PIXELS);
				Apartamento apartamento = apartamentos.get(ind++);
				GridLayout panfoto = new GridLayout();
				panfoto.setWidth(200.0f, Unit.PIXELS);
				panfoto.setHeight(200.0f, Unit.PIXELS);
				
				Image foto = new Image();
				foto.setSource(apartamento.getFotos_apartamento().iterator().next().getStreamResource());
				foto.setSizeFull();
				panfoto.addComponent(foto);
				panfoto.setComponentAlignment(foto, Alignment.MIDDLE_CENTER);
				hl.addComponent(panfoto);
				
				hl.addComponent(new Label(apartamento.getDescripcion()));
				
				Button ver = new Button("Ver mas");
				ver.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						getUI().getNavigator().navigateTo(AnuncioVerVista.NOMBRE + "/" + apartamento.getId());
					}
				});
				hl.addComponent(ver);
				
				vl.addComponent(hl);
				vl.setComponentAlignment(hl, Alignment.MIDDLE_CENTER);
			} else {
				i = anuncios;
			}
		}
		int paginas = (int) Math.ceil((float)apartamentos.size()/anuncios);
		HorizontalLayout nav = new HorizontalLayout();
		if(pagina != 1) {
			Button anterior = new Button();
			anterior.setIcon(FontAwesome.ARROW_LEFT);
			anterior.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
			anterior.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			anterior.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					mostrar(pagina - 1, vl);
				}
			});
			nav.addComponent(anterior);
			nav.setComponentAlignment(anterior, Alignment.MIDDLE_RIGHT);
		}
		int minf = 0;
		int msup = 0;
		if(pagina <= 3) {
			minf = 1;
		}else {
			minf = pagina - 2;
		}
		if((pagina + 2) >= paginas) {
			msup = paginas;
		} else {
			msup = minf + 4;
		}
		for(int i = minf; i <= msup; i++) {
			if(i == pagina) {
				Button b = new Button(Integer.toString(i));
				b.addStyleName(ValoTheme.BUTTON_LINK);
				b.setEnabled(false);
				nav.addComponent(b);
			}else {
				Button b = new Button(Integer.toString(i));
				b.addStyleName(ValoTheme.BUTTON_LINK);
				final int pag = i;
				b.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						mostrar(pag,vl);
					}
				});
				nav.addComponent(b);
			}
		}
		System.out.println(pagina + " " + paginas);
		if(pagina != paginas) {
			Button siguiente = new Button();
			siguiente.setIcon(FontAwesome.ARROW_RIGHT);
			siguiente.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
			siguiente.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			siguiente.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					mostrar(pagina + 1, vl);
				}
			});
			nav.addComponent(siguiente);
			nav.setComponentAlignment(siguiente, Alignment.MIDDLE_RIGHT);
		}
		vl.addComponent(nav);
		vl.setComponentAlignment(nav, Alignment.MIDDLE_CENTER);
	}
	
/*	private Window veranuncio(Apartamento apartamento) {
		Window anuncio = new Window();
		anuncio.setModal(true);
		anuncio.setDraggable(false);
		anuncio.setResizable(false);
		anuncio.setWidth("70%");
		anuncio.setHeight("70%");
		VerticalLayout vl = new VerticalLayout();
		GridLayout panfoto = new GridLayout();
		panfoto.setWidth("50%");
		
		Image foto = new Image();
		foto.setSource(apartamento.getFotos_apartamento().iterator().next().getStreamResource());
		foto.setSizeFull();
		panfoto.addComponent(foto);
		panfoto.setComponentAlignment(foto, Alignment.MIDDLE_CENTER);
		vl.addComponent(panfoto);
		vl.setComponentAlignment(panfoto, Alignment.MIDDLE_CENTER);
		
		Label titulo = new Label("Descripción");
		titulo.addStyleName(ValoTheme.LABEL_H3);
		vl.addComponent(titulo);
		
		Label contenido = new Label(apartamento.getDescripcion());
		vl.addComponent(contenido);
		
		vl.addComponent(new Label("Aqui iria los servicios"));
		
		if(!SeguridadUtil.isLoggedIn()) {
		if(SeguridadUtil.getRol().equals("Huesped")) {
			Button login = new Button("Inicie sesión como huesped para reservar");
			login.addClickListener(new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {					
					getUI().getNavigator().navigateTo(LoginVista.NOMBRE);
				}
			});
		}
		anuncio.setContent(vl);
		return anuncio;
	}*/
}
