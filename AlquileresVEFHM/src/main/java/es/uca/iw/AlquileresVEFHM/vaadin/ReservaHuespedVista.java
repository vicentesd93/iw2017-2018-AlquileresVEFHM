package es.uca.iw.AlquileresVEFHM.vaadin;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Oferta;
import es.uca.iw.AlquileresVEFHM.modelos.Reserva;
import es.uca.iw.AlquileresVEFHM.modelos.ReservaOferta;
import es.uca.iw.AlquileresVEFHM.modelos.User;
import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;

@SuppressWarnings({"serial", "deprecation"})
@SpringView(name = ReservaHuespedVista.NOMBRE)
public class ReservaHuespedVista extends VerticalLayout implements View {
	public final static String NOMBRE = "reserva_huesped";
	
private User usuario;
	
	private final UserDAO userDao;
	
	@Autowired
	public ReservaHuespedVista(UserDAO ud) {
		userDao = ud;
	}
	
	@PostConstruct
	void init() {
		if(!SeguridadUtil.isLoggedIn() || SeguridadUtil.isLoggedIn() && !SeguridadUtil.getRol().equals("Huesped")) {
			Notification.show("No tiene permisos para acceder a la página", Notification.TYPE_ERROR_MESSAGE);
			Page.getCurrent().open("/#!login", null);
			return;
		}
		
		setSizeFull();
		usuario = userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado());
		
		Set<Reserva> reservasaceptadas = new HashSet<Reserva>();
		Set<Reserva> reservasrechazadas = new HashSet<Reserva>();
		Set<Reserva> reservaspendientes = new HashSet<Reserva>();

		for(Reserva r : usuario.getReservas()) {
			if(r.isAceptada()) reservasaceptadas.add(r);
			if(r.isRechazada()) reservasrechazadas.add(r);
			if(!r.isAceptada() && !r.isRechazada()) reservaspendientes.add(r);
		}
		
		Label titulo = new Label("Reservas");
		titulo.addStyleNames(ValoTheme.LABEL_HUGE);
		addComponent(titulo);
		
		Label lblaceptada = new Label("Reservas aceptadas");
		lblaceptada.addStyleName(ValoTheme.LABEL_H3);
		addComponent(lblaceptada);
		
		
		Grid<Reserva> gridaceptadas = new Grid<>();
		gridaceptadas.setSelectionMode(SelectionMode.NONE);
		gridaceptadas.setWidth("100%");
		gridaceptadas.addComponentColumn(reserva -> {
			Button b = new Button("Ver ofertas");
			b.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			b.addClickListener(verofertas(reserva));
			return b;
		}).setCaption("Ofertas");
		gridaceptadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getDireccion())).setCaption("Dirección");
		gridaceptadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getPoblacion())).setCaption("Población");
		gridaceptadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getPais())).setCaption("País");
		gridaceptadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getDescripcion())).setCaption("Descripción");
		gridaceptadas.setItems(reservasaceptadas);
		addComponent(gridaceptadas);
		
		lblaceptada = new Label("Reservas rechazadas");
		lblaceptada.addStyleName(ValoTheme.LABEL_H3);
		addComponent(lblaceptada);
		
		Grid<Reserva> gridrechazadas = new Grid<>();
		gridrechazadas.setSelectionMode(SelectionMode.NONE);
		gridrechazadas.setWidth("100%");
		gridrechazadas.addComponentColumn(reserva -> {
			Button b = new Button("Ver ofertas");
			b.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			b.addClickListener(verofertas(reserva));
			return b;
		}).setCaption("Ofertas");
		gridrechazadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getDireccion())).setCaption("Dirección");
		gridrechazadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getPoblacion())).setCaption("Población");
		gridrechazadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getPais())).setCaption("País");
		gridrechazadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getDescripcion())).setCaption("Descripción");
		gridrechazadas.setItems(reservasrechazadas);
		addComponent(gridrechazadas);
		
		lblaceptada = new Label("Reservas pendientes");
		lblaceptada.addStyleName(ValoTheme.LABEL_H3);
		addComponent(lblaceptada);
		
		Grid<Reserva> gridpendientes = new Grid<>();
		gridpendientes.setSelectionMode(SelectionMode.NONE);
		gridpendientes.setWidth("100%");
		gridpendientes.addComponentColumn(reserva -> {
			Button b = new Button("Ver ofertas");
			b.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			b.addClickListener(verofertas(reserva));
			return b;
		}).setCaption("Ofertas");
		gridpendientes.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getDireccion())).setCaption("Dirección");
		gridpendientes.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getPoblacion())).setCaption("Población");
		gridpendientes.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getPais())).setCaption("País");
		gridpendientes.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getDescripcion())).setCaption("Descripción");
		gridpendientes.setItems(reservaspendientes);
		addComponent(gridpendientes);
		
	}
	
	private ClickListener verofertas(Reserva reserva) {
		return new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Window ofertas = new Window();
				ofertas.setDraggable(false);
				ofertas.setModal(true);
				ofertas.setResizable(false);
				
				VerticalLayout vl = new VerticalLayout();
				
				Set<Oferta> ofers = new HashSet<Oferta>();
				
				for(ReservaOferta ro : reserva.getReservasofertas()) {
					ofers.add(ro.getOferta());
				}
				
				Label titulo = new Label("Ofertas");
				vl.addComponent(titulo);
				vl.setComponentAlignment(titulo, Alignment.MIDDLE_CENTER);
				
				Grid<Oferta> grid = new Grid<>();
				grid.setSelectionMode(SelectionMode.NONE);
				grid.addColumn(Oferta::getLDFecha).setCaption("Fecha");
				grid.addColumn(Oferta::getPrecio).setCaption("Precio");
				grid.addColumn(Oferta::getPenalizacion).setCaption("Penalización");
				grid.setItems(ofers);
				vl.addComponent(grid);
				vl.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
				
				Button b = new Button("Cerrar");
				b.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						ofertas.close();
					}
				});
				vl.addComponent(b);
				vl.setComponentAlignment(b, Alignment.MIDDLE_CENTER);
				
				ofertas.setContent(vl);
				getUI().addWindow(ofertas);
			}
		};
	}
}