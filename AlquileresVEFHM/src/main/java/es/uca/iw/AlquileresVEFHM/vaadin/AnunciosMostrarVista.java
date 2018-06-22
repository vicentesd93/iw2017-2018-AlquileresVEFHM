package es.uca.iw.AlquileresVEFHM.vaadin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import es.uca.iw.AlquileresVEFHM.DAO.ApartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.OfertaDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Oferta;

@SuppressWarnings({"serial", "deprecation"})
@SpringView(name = AnunciosMostrarVista.NOMBRE)
public class AnunciosMostrarVista extends VerticalLayout implements View {
	public static final String NOMBRE = "";
	
	private final ApartamentoDAO aparDao;
	private final OfertaDAO ofertaDao;
	
	private List<Apartamento> apartamentos;
	private Integer anuncios = 3;
	
	@Autowired
	public AnunciosMostrarVista(ApartamentoDAO ad, OfertaDAO of) {
		aparDao = ad;
		ofertaDao = of;
	}
	
	@PostConstruct
	void init() {
		List<Apartamento> apartamentosParaMostrar = new ArrayList<Apartamento>();

		//Huesped busque apartamentos -> TRAERNOS TODOS LOS APARTAMENTOS DE LA BD [PENDIENTE FILTRAR]
		//Huesped posibilidad de que pueda reservar un apartamento
		
		for (Iterator<Oferta> it = ofertaDao.findAll().iterator(); it.hasNext();) {
			Oferta a = it.next();
			if (!apartamentosParaMostrar.contains(a.getApartamento())) {
				apartamentosParaMostrar.add(a.getApartamento());
			}
		}

		VerticalLayout vlFiltro = new VerticalLayout();
		VerticalLayout vlMostrarApartamentos = new VerticalLayout();
		
		Label titulo = new Label("Apartamentos disponibles");
		titulo.addStyleName(ValoTheme.LABEL_HUGE);
		addComponent(titulo);
		
		HorizontalLayout hl1 = new HorizontalLayout();
		HorizontalLayout hl2= new HorizontalLayout();
		HorizontalLayout hl3 = new HorizontalLayout();
		
		TextField direccion = new TextField("Dirección");
		TextField poblacion = new TextField("Población");
		TextField pais = new TextField("País");
		TextField m2 = new TextField("Metros cuadrados (>= m2) ");	
		TextField aseos = new TextField("Aseos (>= aseos)");
		TextField dormitorios = new TextField("Dormitorios (>= dormitorios)");
		TextField precioMax = new TextField("Precio maximo");
		TextField precioMin = new TextField("Precio minimo");
		DateField rangoFechaInicio = new DateField("Fecha inicio");
		DateField rangoFechaFin = new DateField("Fecha fin");
		
		Button filtrar = new Button("Buscar");
		filtrar.setWidth("100px");
		filtrar.setHeight("70px");
		filtrar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		filtrar.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						boolean dir,pob,pai,m2b,ase,dor,preciomin,preciomax,fechaini,fechafin;
						apartamentosParaMostrar.clear();
						for (Iterator<Oferta> it = ofertaDao.findAll().iterator(); it.hasNext();) {
					    	Oferta a = it.next();
					    	dir = false;pob = false;pai = false; m2b = false;ase = false;dor = false;
					    	fechafin = false; preciomin = false; preciomax = false; fechaini = false;
					    	if (!apartamentosParaMostrar.contains(a.getApartamento())) {
					    		if (a.getApartamento().getDireccion().toLowerCase().contains(direccion.getValue().toLowerCase()) || direccion.isEmpty()) {
						    		dir = true;
								}
						    	
						    	if (a.getApartamento().getPoblacion().toLowerCase().contains(poblacion.getValue().toLowerCase()) || poblacion.isEmpty()) {
						    		pob = true;
								}
						    	
						    	if (a.getApartamento().getPais().toLowerCase().contains(pais.getValue().toLowerCase()) || pais.isEmpty()) {
						    		pai = true;
								}
						    	
						    	try {
							    	if (Integer.parseInt(m2.getValue()) < a.getApartamento().getM2()) {
							    		m2b = true;
									}
						    	}catch (NumberFormatException e) {
						    		m2b = true;
								}
						    	
						    	try {
							    	if (Integer.parseInt(aseos.getValue()) < a.getApartamento().getAseos()) {
							    		ase = true;
									}
						    	}catch (NumberFormatException e) {
						    		ase = true;
						    	}
						    	
						    	try {
							    	if (Integer.parseInt(dormitorios.getValue()) < a.getApartamento().getDormitorios()) {
							    		dor = true;
									}
						    	}catch (NumberFormatException e) {
						    		dor = true;
						    	}
						    	//-----------------------------
						    	try {
							    	if (Integer.parseInt(precioMax.getValue()) >= a.getPrecio()) {
							    		preciomax = true;
									}
						    	}catch (NumberFormatException e) {
						    		preciomax = true;
						    	}
						    	try {
							    	if (Integer.parseInt(precioMin.getValue()) < a.getPrecio()) {
							    		preciomin = true;
									}
						    	}catch (NumberFormatException e) {
						    		preciomin = true;
						    	}
						    	
						    	for (Iterator<Oferta> iterador = a.getApartamento().getOfertas().iterator(); iterador.hasNext();) {
						    		Oferta ofer = iterador.next();
							    	if (rangoFechaInicio.getValue() == null) {
										fechaini = true;
									}else if(ofer.getLDFecha().isAfter(rangoFechaInicio.getValue())) {
										fechaini = true;
									}
							    	if (rangoFechaFin.getValue() == null) {
										fechafin = true;
									}else if(ofer.getLDFecha().isBefore(rangoFechaFin.getValue())) {
										fechafin = true;
									}
						    	}
						    	
						    	if (dor && dir && ase && m2b && pai && pob && preciomin && preciomax && fechaini && fechafin) {
									apartamentosParaMostrar.add(a.getApartamento());
								}
							}
					    }
						apartamentos = apartamentosParaMostrar;
						mostrar(1, vlMostrarApartamentos);
					}
				});
		
		apartamentos = apartamentosParaMostrar;
		
		hl1.addComponent(direccion);
		hl1.addComponent(poblacion);
		hl1.addComponent(pais);
		hl1.addComponent(precioMin);
		hl1.addComponent(precioMax);
		hl2.addComponent(m2);
		hl2.addComponent(dormitorios);
		hl2.addComponent(aseos);
		hl2.addComponent(rangoFechaInicio);
		hl2.addComponent(rangoFechaFin);
		hl3.addComponent(filtrar);
		
		vlFiltro.addComponent(hl1);
		vlFiltro.addComponent(hl3);
		vlFiltro.addComponent(hl2);
		
		vlFiltro.setComponentAlignment(hl1, Alignment.MIDDLE_LEFT);
		vlFiltro.setComponentAlignment(hl2, Alignment.MIDDLE_LEFT);
		vlFiltro.setComponentAlignment(hl3, Alignment.MIDDLE_RIGHT);
		
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		setMargin(true);
		
		setWidth("100%");
		mostrar(1, vlMostrarApartamentos);
		
		addComponent(vlFiltro);
		addComponent(vlMostrarApartamentos);		
	}
	
	void mostrar(int pagina, VerticalLayout vl) {
		int ind = (pagina - 1) * anuncios;
		vl.removeAllComponents();
		vl.setWidth("100%");
		for(int i = 0; i < anuncios; i++) {
			if(ind < apartamentos.size()) {
				HorizontalLayout hl = new HorizontalLayout();
				hl.setWidth("90%");
				hl.setHeight(230.0f, Unit.PIXELS);
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
				hl.setComponentAlignment(panfoto, Alignment.MIDDLE_RIGHT);
				
				TextArea contenido = new TextArea();
				contenido.setValue(apartamento.getDescripcion());
				contenido.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
				contenido.setReadOnly(true);
				contenido.setWidth("90%");
				contenido.setHeight("90%");
				hl.addComponent(contenido);
				hl.setComponentAlignment(contenido, Alignment.MIDDLE_CENTER);

				VerticalLayout der = new VerticalLayout();
				Label dir = new Label(apartamento.getPoblacion());
				der.addComponent(dir);

				dir = new Label(apartamento.getPais());
				der.addComponent(dir);
				
				Button ver = new Button("Ver más");
				ver.addStyleName(ValoTheme.BUTTON_PRIMARY);
				ver.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						getUI().getNavigator().navigateTo(AnuncioVerVista.NOMBRE + "/" + apartamento.getId());
					}
				});
				der.addComponent(ver);
				hl.addComponent(der);
				hl.setComponentAlignment(der, Alignment.MIDDLE_LEFT);

				vl.addComponent(hl);
				vl.setComponentAlignment(hl, Alignment.MIDDLE_CENTER);
			} else {
				i = anuncios;
			}
		}
		int paginas = (int) Math.ceil((float)apartamentos.size()/anuncios);
		if(paginas == 0) paginas = 1;
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
}
