package es.uca.iw.AlquileresVEFHM.vaadin;

import java.text.DecimalFormat;
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
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.ReservaDAO;
import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Oferta;
import es.uca.iw.AlquileresVEFHM.modelos.Reserva;
import es.uca.iw.AlquileresVEFHM.modelos.ReservaOferta;
import es.uca.iw.AlquileresVEFHM.modelos.User;
import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;

@SuppressWarnings({"serial", "deprecation"})
@SpringView(name = ReservaAnfitrionVista.NOMBRE)
public class ReservaAnfitrionVista extends VerticalLayout implements View {
	public final static String NOMBRE = "reserva_anfitrion";
	
	private User usuario;
	
	private final UserDAO userDao;
	private final ReservaDAO reservaDao;
	
	@Autowired
	public ReservaAnfitrionVista(UserDAO ud, ReservaDAO rd) {
		userDao = ud;
		reservaDao = rd;
	}
	
	@PostConstruct
	void init() {
		if(!SeguridadUtil.isLoggedIn() || SeguridadUtil.isLoggedIn() && !SeguridadUtil.getRol().equals("Anfitrion")) {
			Notification.show("No tiene permisos para acceder a la página", Notification.TYPE_ERROR_MESSAGE);
			Page.getCurrent().open("/#!login", null);
			return;
		}
		
		setSizeFull();
		usuario = userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado());
		
		Set<Reserva> reservasaceptadas = new HashSet<Reserva>();
		Set<Reserva> reservasrechazadas = new HashSet<Reserva>();
		Set<Reserva> reservaspendientes = new HashSet<Reserva>();
		
		for(Apartamento a : usuario.getApartamentos()) {
			for(Oferta o : a.getOfertas()) {
				for(ReservaOferta ro : o.getReservasofertas()) {
					if(ro.getReserva().isAceptada()) {
						if(!reservasaceptadas.contains(ro.getReserva())) {
								reservasaceptadas.add(ro.getReserva());
						}
					}
					if(ro.getReserva().isAceptada()) {
						if(!reservasaceptadas.contains(ro.getReserva())) {
								reservasaceptadas.add(ro.getReserva());
						}
					}
					if(ro.getReserva().isRechazada()) {
						if(!reservasrechazadas.contains(ro.getReserva())) {
							reservasrechazadas.add(ro.getReserva());
						}
					}
					if(!ro.getReserva().isAceptada() && !ro.getReserva().isRechazada()) {
						if(!reservaspendientes.contains(ro.getReserva())) {
							reservaspendientes.add(ro.getReserva());
						}
					}
				}
			}
		}
		
		Label titulo = new Label("Reservas");
		titulo.addStyleNames(ValoTheme.LABEL_HUGE);
		addComponent(titulo);
		Grid<Reserva> gridaceptadas = new Grid<>();
		Grid<Reserva> gridrechazadas = new Grid<>();
		
		Label lblaceptada = new Label("Reservas pendientes");
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
		gridpendientes.addComponentColumn(reserva -> {
			Button b = new Button("Aceptar");
			b.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			b.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					reserva.setAceptada(true);
					reservaDao.save(reserva);
					reservasaceptadas.add(reserva);
					gridaceptadas.setItems(reservasaceptadas);
					gridaceptadas.getDataProvider().refreshAll();
					reservaspendientes.remove(reserva);
					gridpendientes.setItems(reservaspendientes);
					gridpendientes.getDataProvider().refreshAll();
					Notification.show("Reserva aceptada", Notification.TYPE_WARNING_MESSAGE);
				}
			});
			return b;
		});
		gridpendientes.addComponentColumn(reserva -> {
			Button b = new Button("Rechazar");
			b.addStyleName(ValoTheme.BUTTON_DANGER);
			b.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					reserva.setRechazada(true);
					reservaDao.save(reserva);
					reservasrechazadas.add(reserva);
					gridrechazadas.setItems(reservasrechazadas);
					gridrechazadas.getDataProvider().refreshAll();
					reservaspendientes.remove(reserva);
					gridpendientes.setItems(reservaspendientes);
					gridpendientes.getDataProvider().refreshAll();
					Notification.show("Reserva rechazada", Notification.TYPE_WARNING_MESSAGE);
				}
			});
			return b;
		});
		gridpendientes.addComponentColumn(reserva -> new Label(reserva.getHuesped().getNombre()+" "+reserva.getHuesped().getApellidos())).setCaption("Nombre del huesped");
		gridpendientes.addComponentColumn(reserva -> new Label(reserva.getHuesped().getTelefono())).setCaption("Teléfono");
		gridpendientes.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getDireccionCompleta())).setCaption("Apartamento");
		gridpendientes.setItems(reservaspendientes);
		addComponent(gridpendientes);
		
		
		
		 lblaceptada = new Label("Reservas aceptadas");
		lblaceptada.addStyleName(ValoTheme.LABEL_H3);
		addComponent(lblaceptada);
		
		
		
		gridaceptadas.setSelectionMode(SelectionMode.NONE);
		gridaceptadas.setWidth("100%");
		gridaceptadas.addComponentColumn(reserva -> {
			Button b = new Button("Ver ofertas");
			b.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			b.addClickListener(verofertas(reserva));
			return b;
		}).setCaption("Ofertas");
		gridaceptadas.addComponentColumn(reserva -> {
			if(reserva.getFactura() == null ) return new Label("Pendiente de pago");
			Button b = new Button("Ver factura");
			b.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			b.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					Window wpago = new Window();
					wpago.setModal(true);
					wpago.setDraggable(false);
					wpago.setResizable(false);
					
					DecimalFormat df = new DecimalFormat();
					df.setMaximumFractionDigits(2);
					
					FormLayout vl = new FormLayout();
					vl.setMargin(true);
					vl.setWidth(500.0f, Unit.PIXELS);
					
					Label titulo = new Label("Factura");
					vl.addComponent(titulo);
					
					TextField metp = new TextField("Método pago");
					metp.setValue(reserva.getFactura().getMetodo_pago().getDescripcion());
					metp.setReadOnly(true);
					vl.addComponent(metp);
					
					TextField mpval = new TextField();
					mpval.setCaption(metp.getValue());
					mpval.setValue(reserva.getFactura().getMpvalor());
					mpval.setReadOnly(true);
					vl.addComponent(mpval);
					
					TextField tot1 = new TextField("IVA (21%)");
					tot1.setValue(df.format(reserva.getFactura().getIva()) + "€");
					tot1.setReadOnly(true);
					tot1.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					vl.addComponent(tot1);
					
					TextField tot2 = new TextField("Cargo por metodo de pago (" + reserva.getFactura().getMetodo_pago().getCargo_adicional() + "%)");
					tot2.setValue(df.format(reserva.getFactura().getComision()) + "€");
					tot2.setReadOnly(true);
					tot2.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					vl.addComponent(tot2);
					
					TextField tot3 = new TextField("Total");
					tot3.setValue(df.format(reserva.getFactura().getTotal()) + "€");
					tot3.setReadOnly(true);
					tot3.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					vl.addComponent(tot3);

					Button salir = new Button("Cerrar");
					salir.addClickListener(new ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
							wpago.close();
						}
					});
					vl.addComponent(salir);
					wpago.setContent(vl);
					getUI().addWindow(wpago);
				}
			});
			return b;
			
		}).setCaption("Factura");
		gridaceptadas.addComponentColumn(reserva -> new Label(reserva.getHuesped().getNombre()+" "+reserva.getHuesped().getApellidos())).setCaption("Nombre del huesped");
		gridaceptadas.addComponentColumn(reserva -> new Label(reserva.getHuesped().getTelefono())).setCaption("Teléfono");
		gridaceptadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getDireccionCompleta())).setCaption("Apartamento");
		gridaceptadas.setItems(reservasaceptadas);
		addComponent(gridaceptadas);
		
		lblaceptada = new Label("Reservas rechazadas");
		lblaceptada.addStyleName(ValoTheme.LABEL_H3);
		addComponent(lblaceptada);
		
		
		gridrechazadas.setSelectionMode(SelectionMode.NONE);
		gridrechazadas.setWidth("100%");
		gridrechazadas.addComponentColumn(reserva -> {
			Button b = new Button("Ver ofertas");
			b.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			b.addClickListener(verofertas(reserva));
			return b;
		}).setCaption("Ofertas");
		gridrechazadas.addComponentColumn(reserva -> new Label(reserva.getHuesped().getNombre()+" "+reserva.getHuesped().getApellidos())).setCaption("Nombre del huesped");
		gridrechazadas.addComponentColumn(reserva -> new Label(reserva.getHuesped().getTelefono())).setCaption("Teléfono");
		gridrechazadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getDireccionCompleta())).setCaption("Apartamento");
		gridrechazadas.setItems(reservasrechazadas);
		addComponent(gridrechazadas);
		
		
		
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
