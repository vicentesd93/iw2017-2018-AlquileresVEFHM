package es.uca.iw.AlquileresVEFHM.vaadin;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.ValidationException;
import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
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
import com.vaadin.ui.Link;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.FacturaDAO;
import es.uca.iw.AlquileresVEFHM.DAO.Metodo_pagoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.ReservaDAO;
import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Factura;
import es.uca.iw.AlquileresVEFHM.modelos.Metodo_pago;
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
	private final Metodo_pagoDAO mpagDao;
	private final FacturaDAO facturaDao;
	private final ReservaDAO reservaDao;
	
	@Autowired
	public ReservaHuespedVista(UserDAO ud, Metodo_pagoDAO mpd, FacturaDAO fd, ReservaDAO rd) {
		userDao = ud;
		mpagDao = mpd;
		facturaDao = fd;
		reservaDao = rd;
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
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		Set<Reserva> reservaspagadas = new HashSet<Reserva>();
		Set<Reserva> reservassinpagar = new HashSet<Reserva>();
		Set<Reserva> reservasrechazadas = new HashSet<Reserva>();
		Set<Reserva> reservaspendientes = new HashSet<Reserva>();

		for(Reserva r : usuario.getReservas()) {
			if(r.isAceptada() && r.getFactura() == null) reservassinpagar.add(r);
			if(r.isAceptada() && r.getFactura() != null) reservaspagadas.add(r);
			if(r.isRechazada()) reservasrechazadas.add(r);
			if(!r.isAceptada() && !r.isRechazada()) reservaspendientes.add(r);
		}
		
		Label titulo = new Label("Reservas");
		titulo.addStyleNames(ValoTheme.LABEL_HUGE);
		addComponent(titulo);
		
		Label lblaceptada = new Label("Reservas pagadas");
		lblaceptada.addStyleName(ValoTheme.LABEL_H3);
		addComponent(lblaceptada);
		
		Grid<Reserva> gridpagadas = new Grid<>();
		gridpagadas.setSelectionMode(SelectionMode.NONE);
		gridpagadas.setWidth("100%");
		gridpagadas.addComponentColumn(reserva -> {
			Button b = new Button("Ver fechas");
			b.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			b.addClickListener(verofertas(reserva));
			return b;
		}).setCaption("Fechas");
		gridpagadas.addComponentColumn(reserva -> {
			Button b = new Button("Ver factura");
			b.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			b.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					Window wpago = new Window();
					wpago.setModal(true);
					wpago.setDraggable(false);
					wpago.setResizable(false);
					
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
			FileResource file = new FileResource(new File("pdf/"+reserva.getFactura().getId()+".pdf"));
			Link link = new Link("Ver Factura", file);
			return link;
		}).setCaption("Factura");
		gridpagadas.addComponentColumn(reserva -> new Label(df.format(reserva.getFactura().getTotal()))).setCaption("Total €");
		gridpagadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getDireccion())).setCaption("Dirección");
		gridpagadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getPoblacion())).setCaption("Población");
		gridpagadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getPais())).setCaption("País");
		gridpagadas.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getDescripcion())).setCaption("Descripción");
		gridpagadas.setItems(reservaspagadas);
		addComponent(gridpagadas);
		
		lblaceptada = new Label("Reservas sin pagar");
		lblaceptada.addStyleName(ValoTheme.LABEL_H3);
		addComponent(lblaceptada);
		
		Grid<Reserva> gridsinpagar= new Grid<>();
		gridsinpagar.setSelectionMode(SelectionMode.NONE);
		gridsinpagar.setWidth("100%");
		gridsinpagar.addComponentColumn(reserva -> {
			Button b = new Button("Ver fechas");
			b.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			b.addClickListener(verofertas(reserva));
			return b;
		}).setCaption("Fechas");
		gridsinpagar.addComponentColumn(reserva -> {
			Button b = new Button("Pagar");
			b.addStyleName(ValoTheme.BUTTON_FRIENDLY);
			b.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					Window wpago = new Window();
					wpago.setModal(true);
					wpago.setDraggable(false);
					wpago.setResizable(false);
					
					FormLayout vl = new FormLayout();
					vl.setMargin(true);
					vl.setWidth(500.0f, Unit.PIXELS);
					
					Binder<Factura> binder = new Binder<>();
					
					Label titulo = new Label("Realizar pago");
					vl.addComponent(titulo);
					
					NativeSelect<Metodo_pago> metp = new NativeSelect<>("Método pago");
					metp.setItems(mpagDao.findAll());
					metp.setValue(mpagDao.findAll().iterator().next());
					metp.setEmptySelectionAllowed(false);
					vl.addComponent(metp);
					binder.forField(metp)
						.asRequired()
						.bind(Factura::getMetodo_pago, Factura::setMetodo_pago);
					
					TextField mpval = new TextField();
					mpval.setCaption(metp.getValue().getDescripcion());
					vl.addComponent(mpval);
					binder.forField(mpval)
						.asRequired()
						.bind(Factura::getMpvalor,Factura::setMpvalor);
					
					TextField tot = new TextField("Total reservas");
					tot.setValue(df.format(reserva.getTotal()) + "€");
					tot.setReadOnly(true);
					tot.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					vl.addComponent(tot);
					
					TextField tot1 = new TextField("IVA (21%)");
					tot1.setValue(df.format(reserva.getTotal() * 0.21f) + "€");
					tot1.setReadOnly(true);
					tot1.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					vl.addComponent(tot1);
					
					TextField tot2 = new TextField("Cargo por metodo de pago (" + metp.getValue().getCargo_adicional() + "%)");
					tot2.setValue(df.format(reserva.getTotal() * metp.getValue().getCargo_adicional() / 100) + "€");
					tot2.setReadOnly(true);
					tot2.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					vl.addComponent(tot2);
					
					TextField tot3 = new TextField("Total");
					tot3.setValue(df.format(reserva.getTotal() * (1.21f + metp.getValue().getCargo_adicional() / 100)) + "€");
					tot3.setReadOnly(true);
					tot3.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
					vl.addComponent(tot3);
					
					metp.addValueChangeListener(new ValueChangeListener<Metodo_pago>() {
						@Override
						public void valueChange(ValueChangeEvent<Metodo_pago> event) {
							mpval.setCaption(event.getValue().getDescripcion());
							tot2.setCaption("Cargo por metodo de pago (" + metp.getValue().getCargo_adicional() + ")");
							tot2.setValue(reserva.getTotal() * metp.getValue().getCargo_adicional() / 100 + "€");
							tot3.setValue(df.format(reserva.getTotal() * (1.21f + metp.getValue().getCargo_adicional() / 100))+ "€");
						}
					});
					
					Button pagar = new Button("Pagar");
					pagar.addClickListener(new ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
							if(binder.isValid()) {
								Factura f = new Factura();
								try {
									binder.writeBean(f);
									f.setIva(reserva.getTotal() * 0.21f);
									f.setComision(reserva.getTotal() * metp.getValue().getCargo_adicional() / 100);
									f.setTotal(reserva.getTotal() * (1.21f + metp.getValue().getCargo_adicional() / 100));
									facturaDao.save(f);
									reserva.setFactura(f);
									reservaDao.save(reserva);
									wpago.close();
									removeAllComponents();
									init();
									
									PDPage singlePage = new PDPage();
									PDFont courierBoldFont = PDType1Font.COURIER_BOLD;
									int fontSize = 12;
									try (PDDocument document = new PDDocument()){
										document.addPage(singlePage);
										PDPageContentStream contentStream = new PDPageContentStream(document, singlePage);
										contentStream.beginText();
										contentStream.setFont(courierBoldFont, fontSize);
										contentStream.newLineAtOffset(150, 750);
										contentStream.drawString(reserva.getFactura().getMetodo_pago().getDescripcion() + ": " + reserva.getFactura().getMpvalor());
										contentStream.newLineAtOffset(0, -15);
										contentStream.drawString("IVA: "+reserva.getFactura().getIva()+ "%");
										contentStream.newLineAtOffset(0, -15);
										contentStream.drawString("Cargo: " + reserva.getFactura().getComision());
										contentStream.newLineAtOffset(0, -15);
										contentStream.drawString("Total: " + reserva.getFactura().getTotal());
										
										//pdf = pdf.replace("\n", "").replace("\r", "");
										//contentStream.showText(pdf);
										contentStream.endText();
										contentStream.close();  // Stream must be closed before saving document.
										document.save("pdf/"+reserva.getFactura().getId()+".pdf");
									}
									catch (IOException ioEx){}	
									
								} catch (ValidationException e) {
									Notification.show("Error al generar factura", Notification.TYPE_ERROR_MESSAGE);
									wpago.close();
									removeAllComponents();
									init();
								}
							}
						}
					});
					vl.addComponent(pagar);
					wpago.setContent(vl);
					getUI().addWindow(wpago);
				}
			});
			return b;
		}).setCaption("");
		gridsinpagar.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getDireccion())).setCaption("Dirección");
		gridsinpagar.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getPoblacion())).setCaption("Población");
		gridsinpagar.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getPais())).setCaption("País");
		gridsinpagar.addComponentColumn(reserva -> new Label(reserva.getReservasofertas().iterator().next().getOferta().getApartamento().getDescripcion())).setCaption("Descripción");
		gridsinpagar.setItems(reservassinpagar);
		addComponent(gridsinpagar);
		
		lblaceptada = new Label("Reservas rechazadas");
		lblaceptada.addStyleName(ValoTheme.LABEL_H3);
		addComponent(lblaceptada);
		
		Grid<Reserva> gridrechazadas = new Grid<>();
		gridrechazadas.setSelectionMode(SelectionMode.NONE);
		gridrechazadas.setWidth("100%");
		gridrechazadas.addComponentColumn(reserva -> {
			Button b = new Button("Ver fechas");
			b.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			b.addClickListener(verofertas(reserva));
			return b;
		}).setCaption("Fechas");
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
			Button b = new Button("Ver fechas");
			b.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			b.addClickListener(verofertas(reserva));
			return b;
		}).setCaption("Fechas");
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