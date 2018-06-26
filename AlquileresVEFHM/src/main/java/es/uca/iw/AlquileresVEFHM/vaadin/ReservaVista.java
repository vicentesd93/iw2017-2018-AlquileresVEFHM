package es.uca.iw.AlquileresVEFHM.vaadin;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.ApartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.OfertaDAO;
import es.uca.iw.AlquileresVEFHM.DAO.ReservaDAO;
import es.uca.iw.AlquileresVEFHM.DAO.ReservaOfertaDAO;
import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Oferta;
import es.uca.iw.AlquileresVEFHM.modelos.Reserva;
import es.uca.iw.AlquileresVEFHM.modelos.ReservaOferta;
import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;

@SuppressWarnings({"serial", "deprecation"})
@SpringView(name = ReservaVista.NOMBRE)
public class ReservaVista extends VerticalLayout implements View {
	public final static String NOMBRE = "reserva";
	
	private Apartamento apartamento;
	private Set<Oferta> ofertasseleccionadas = new HashSet<Oferta>();
	private LocalDate ld;
	private float importetotal = 0;
	
	private final ApartamentoDAO aparDao;
	private final OfertaDAO ofertaDao;
	private final UserDAO userDao;
	private final ReservaDAO resDao;
	private final ReservaOfertaDAO reofDao;
	
	@Autowired
	public ReservaVista(ApartamentoDAO ad, OfertaDAO od, UserDAO ud, ReservaDAO rd, ReservaOfertaDAO rod) {
		aparDao = ad;
		ofertaDao = od;
		userDao = ud;
		resDao = rd;
		reofDao = rod;
	}
	
	@PostConstruct
	void init() {
		if(!SeguridadUtil.isLoggedIn() || SeguridadUtil.isLoggedIn() && !SeguridadUtil.getRol().equals("Huesped")) {
			Notification.show("No tiene permisos para acceder a la página", Notification.TYPE_ERROR_MESSAGE);
			Page.getCurrent().open("/#!login", null);
			return;
		}
		setSizeFull();
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent e) {
		apartamento = aparDao.apartamentoOfertado(Integer.valueOf(e.getParameters()));
		if(apartamento == null) Page.getCurrent().reload();

		ld = LocalDate.now();
		
		Label titulo = new Label("Reservar piso " + apartamento.getDireccion() + ", " + apartamento.getPoblacion() + ", " + apartamento.getPais());
		addComponent(titulo);
		Panel cal = new Panel();
		cal.setWidth(1010.0f, Unit.PIXELS);
		addComponent(cal);
		setComponentAlignment(cal, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout navcal = new HorizontalLayout();
		Button ant = new Button(ld.minusMonths(1).getMonth().getDisplayName(TextStyle.FULL, getLocale()));
		ant.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		ant.setIcon(FontAwesome.ARROW_LEFT);
		navcal.addComponent(ant);
		
		Button sig = new Button(ld.plusMonths(1).getMonth().getDisplayName(TextStyle.FULL, getLocale()));
		sig.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		sig.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		sig.setIcon(FontAwesome.ARROW_RIGHT);
		navcal.addComponent(sig);
		
		addComponent(navcal);
		setComponentAlignment(navcal, Alignment.MIDDLE_CENTER);
		
		Grid<Oferta> grid = new Grid<>();
		grid.setSizeFull();
		grid.setItems(ofertasseleccionadas);
		grid.setSelectionMode(SelectionMode.NONE);
		grid.addColumn(Oferta::getLDFecha).setCaption("Fecha");
		grid.addColumn(oferta -> {
			return String.format("%.2f €", oferta.getPrecio());
		}).setCaption("Precio");
		grid.addColumn(oferta -> {
			return String.format("%.2f €", oferta.getPrecio()*0.21);
		}).setCaption("IVA");
		grid.addColumn(oferta -> {
			return String.format("%.2f €", oferta.getPrecio()*1.21);
		}).setCaption("Total");
		grid.addColumn(Oferta::getPenalizacion).setCaption("Penalizacion %");
		addComponent(grid);
		
		HorizontalLayout reservar = new HorizontalLayout();
		Label total = new Label("Total: " + Float.toString(importetotal) + "€");
		reservar.addComponent(total);
		
		Button res = new Button("Reservar");
		reservar.addComponent(res);
		reservar.setComponentAlignment(res, Alignment.MIDDLE_LEFT);
		addComponent(reservar);
		
		cal.setContent(calendario(ld, grid, total));
		
		ant.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				ld = ld.minusMonths(1);
				cal.setContent(calendario(ld, grid, total));
				ant.setCaption(ld.minusMonths(1).getMonth().getDisplayName(TextStyle.FULL, getLocale()));
			}
		});
		sig.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				ld = ld.plusMonths(1);
				cal.setContent(calendario(ld, grid, total));
				ant.setCaption(ld.plusMonths(1).getMonth().getDisplayName(TextStyle.FULL, getLocale()));
			}
		});
		res.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if(ofertasseleccionadas.isEmpty()) Notification.show("No ha seleccionado ninguna oferta", Notification.TYPE_WARNING_MESSAGE);
				else {
					try {
						Reserva r = new Reserva();
						r.setRechazada(false);
						r.setAceptada(false);
						r.setHuesped(userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado()));
						resDao.save(r);
						ReservaOferta ro = null;
						for(Oferta o : ofertasseleccionadas) {
							ro = new ReservaOferta();
							ro.setOferta(o);
							ro.setReserva(r);
							reofDao.save(ro);
						}
						Notification.show("Reserva creada correctamente. \nSe le avisara cuando el anfitrión acepte la reserva", Notification.TYPE_WARNING_MESSAGE);
						getUI().getNavigator().navigateTo(AnunciosMostrarVista.NOMBRE);
					}catch(Exception e) {
						System.out.println(e);
						Notification.show("Ha ocurrido un error al crear la reserva, por favor intentelo de nuevo", Notification.TYPE_ERROR_MESSAGE);
					}
				}
			}
		});
	}
	
	private Panel calendario(LocalDate fecha, Grid<Oferta> grid, Label total) {
		LocalDate inicio = fecha.withDayOfMonth(1);
		LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());
		
		Set<Oferta> ofertasmes = ofertaDao.ofertaIntervalo(apartamento.getId(), inicio, fin);
		Oferta oferta;
		if(ofertasmes.iterator().hasNext()) oferta = ofertasmes.iterator().next();
		else {
			oferta = new Oferta();
			oferta.setLDFecha(fecha.minusYears(1));
		}
		
		Panel calendario = new Panel(inicio.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " - " + inicio.getYear());
		
		VerticalLayout vl = new VerticalLayout();
		int diasem = 1;
		HorizontalLayout hl = new HorizontalLayout();
		for(LocalDate i = inicio; i.compareTo(fin) <= 0; i = i.plusDays(1)) {
			if(diasem == 8) {
				vl.addComponent(hl);
				hl = new HorizontalLayout();
				diasem = 1;
			}
			if(i.compareTo(inicio) == 0) {
				LocalDate premes = inicio.minusDays(inicio.getDayOfWeek().getValue()-1);
				while(premes.compareTo(inicio) != 0) {
					Panel dia = new Panel();
					dia.setWidth(130.0f, Unit.PIXELS);
					dia.setHeight(130.0f, Unit.PIXELS);
					dia.setCaption(Integer.toString(premes.getDayOfMonth()));
					dia.setContent(new VerticalLayout());
					hl.addComponent(dia);
					premes = premes.plusDays(1);
					++diasem;
				}
			}
			Panel dia = new Panel();
			dia.setWidth(130.0f, Unit.PIXELS);
			dia.setHeight(130.0f, Unit.PIXELS);
			dia.setCaption(Integer.toString(i.getDayOfMonth()));
			Button p = new Button();
			p.addStyleName(ValoTheme.BUTTON_TINY);
			if(oferta.getLDFecha().compareTo(i) == 0) {
				Boolean disponible = true;
				if(oferta.getReservasofertas() == null) disponible = false;
				else {
					for(ReservaOferta ro : oferta.getReservasofertas()) {
						if(ro.getReserva().isAceptada()) {
							disponible = false;
							break;
						}else {
							if(!ro.getReserva().isRechazada()) {
								disponible = false;
								break;
							}
						}
					}
				}
				
				if(disponible) {
					if(LocalDate.now().compareTo(i) > 0) p.setEnabled(false);
					Oferta ofer = oferta;
					p.addStyleName(ValoTheme.BUTTON_PRIMARY);
					String caption = "Disponible " + ofer.getPrecio()*1.21 + "€";
					p.addClickListener(new ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
							if(p.getCaption().equals(caption)) {
								p.removeStyleName(ValoTheme.BUTTON_PRIMARY);
								p.addStyleName(ValoTheme.BUTTON_FRIENDLY);
								p.setCaption("Seleccionado");
								ofertasseleccionadas.add(ofer);
								grid.getDataProvider().refreshAll();
								importetotal += ofer.getPrecio();
								total.setValue("Total: " + Float.toString((float)(importetotal*1.21)) + "€");
							}else {
								p.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
								p.addStyleName(ValoTheme.BUTTON_PRIMARY);
								p.setCaption(caption);
								ofertasseleccionadas.remove(ofer);
								grid.getDataProvider().refreshAll();
								importetotal -= ofer.getPrecio();
								total.setValue("Total: " + Float.toString((float)(importetotal*1.21)) + "€");
							}
						}
					});
					p.setCaption(caption);
				} else {
					p.addStyleName(ValoTheme.BUTTON_DANGER);
					p.setCaption("Reservado");
					p.setEnabled(false);
				}				
				if(ofertasmes.iterator().hasNext()) ofertasmes.remove(ofertasmes.iterator().next());
				if(ofertasmes.iterator().hasNext()) oferta = ofertasmes.iterator().next();
			} else {
				p.setCaption("No disponible");
				p.setEnabled(false);
			}
			p.setSizeFull();
			dia.setContent(p);
			hl.addComponent(dia);
			++diasem;
		}
		while(diasem != 8) {
			fin = fin.plusDays(1);
			Panel dia = new Panel();
			dia.setWidth(130.0f, Unit.PIXELS);
			dia.setHeight(130.0f, Unit.PIXELS);
			dia.setCaption(Integer.toString(fin.getDayOfMonth()));
			dia.setContent(new VerticalLayout());
			hl.addComponent(dia);
			fin = fin.plusDays(1);
			++diasem;
		}
		vl.addComponent(hl);
		calendario.setContent(vl);
		return calendario;
	}
}

