package es.uca.iw.AlquileresVEFHM.vaadin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.OfertaDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Foto_apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Oferta;

@SuppressWarnings({"serial", "deprecation"})
@SpringView(name = ApartamentoBuscar.NOMBRE)
public class ApartamentoBuscar extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String NOMBRE = "buscarApartamentos";
	private Integer windowfotoindice = 0;
	private OfertaDAO ofertaDao;
	private Grid<Apartamento> grid;
	
	private Component apartamentos() {
		
		Set<Apartamento> apartamentosParaMostrar = new HashSet<Apartamento>();

		//Huesped busque apartamentos -> TRAERNOS TODOS LOS APARTAMENTOS DE LA BD [PENDIENTE FILTRAR]
		//Huesped posibilidad de que pueda reservar un apartamento
		
		for (Iterator<Oferta> it = ofertaDao.findAll().iterator(); it.hasNext();) {
			Oferta a = it.next();
			apartamentosParaMostrar.add(a.getApartamento());
		}
		
		grid = new Grid<Apartamento>();
		
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		
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
		
		Button filtrar = new Button("Filtrar");
		filtrar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		filtrar.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						boolean dir,pob,pai,m2b,ase,dor;
						apartamentosParaMostrar.clear();
						for (Iterator<Oferta> it = ofertaDao.findAll().iterator(); it.hasNext();) {
					    	Oferta a = it.next();
					    	dir = false;pob = false;pai = false; m2b = false;ase = false;dor = false;
					    	
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
					    	
					    	if (dor && dir && ase && m2b && pai && pob) {
								apartamentosParaMostrar.add(a.getApartamento());
							}
					    }
						grid.setItems(apartamentosParaMostrar);
					}
				});
		
		hl1.addComponent(direccion);
		hl1.addComponent(poblacion);
		hl1.addComponent(pais);
		hl2.addComponent(m2);
		hl2.addComponent(dormitorios);
		hl2.addComponent(aseos);
		hl3.addComponent(filtrar);
		
		vl.addComponent(hl1);
		vl.addComponent(hl2);
		vl.addComponent(hl3);
		
		grid.setItems(apartamentosParaMostrar);
		grid.setSizeFull();		
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.removeAllColumns();
		grid.addColumn(Apartamento::getDescripcion).setCaption("Descripción");
		grid.addColumn(Apartamento::getDireccion).setCaption("Dirección");
		grid.addColumn(Apartamento::getPoblacion).setCaption("Población");
		grid.addColumn(Apartamento::getPais).setCaption("País");
		
		CssLayout opciones = new CssLayout();
		opciones.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		opciones.setEnabled(false);
		
		opciones.addComponent(crearBotonOpcion("Imágenes", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				windowfotoindice = 0;
				Window ventana = new Window("Imágenes");
				ventana.setWidth("80%");
				ventana.setClosable(true);
				ventana.setResizable(false);
				ventana.setModal(true);
				
				VerticalLayout vl = new VerticalLayout();
				CssLayout opc = new CssLayout();
				opc.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
				vl.addComponent(opc);
				vl.setComponentAlignment(opc, Alignment.MIDDLE_CENTER);
				
				List<Foto_apartamento> fotos = new ArrayList<>();
				
				fotos.addAll(grid.asSingleSelect().getOptionalValue().get().getFotos_apartamento());
				if (fotos.isEmpty())
					opciones.setEnabled(false);
				Foto_apartamento foto_apartamento = fotos.get(windowfotoindice);
				
				Image foto = new Image();
				foto.setSource(foto_apartamento.getStreamResource());
				foto.setWidth("100%");
				vl.addComponent(foto);
				vl.setComponentAlignment(foto, Alignment.MIDDLE_CENTER);
				
				HorizontalLayout hl = new HorizontalLayout();
				vl.addComponent(hl);
				vl.setComponentAlignment(hl, Alignment.MIDDLE_CENTER);
				
				Button anterior = new Button();
				anterior.setIcon(FontAwesome.ARROW_LEFT);
				anterior.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
				anterior.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
				hl.addComponent(anterior);
				hl.setComponentAlignment(anterior, Alignment.MIDDLE_LEFT);
				
				Label ind = new Label(String.valueOf(windowfotoindice + 1) + "/" + String.valueOf(fotos.size()));
				hl.addComponent(ind);
				hl.setComponentAlignment(ind, Alignment.MIDDLE_CENTER);
				
				Button siguiente = new Button();
				siguiente.setIcon(FontAwesome.ARROW_RIGHT);
				siguiente.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
				siguiente.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
				hl.addComponent(siguiente);
				hl.setComponentAlignment(siguiente, Alignment.MIDDLE_RIGHT);
				
				anterior.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						if(windowfotoindice <= 0) windowfotoindice = fotos.size() - 1;
						else --windowfotoindice;
						ind.setValue(String.valueOf(windowfotoindice + 1) + "/" + String.valueOf(fotos.size()));
						foto.setSource(fotos.get(windowfotoindice).getStreamResource());
					}
				});
				
				siguiente.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						if(windowfotoindice >= fotos.size() - 1) windowfotoindice = 0;
						else ++windowfotoindice;
						ind.setValue(String.valueOf(windowfotoindice + 1) + "/" + String.valueOf(fotos.size()));
						foto.setSource(fotos.get(windowfotoindice).getStreamResource());
					}
				});
								
				vl.setMargin(true);
				vl.setWidth("100%");
				ventana.setContent(vl);
				
				getUI().getUI().addWindow(ventana);
			}
		}));
		
		grid.addSelectionListener(new SelectionListener<Apartamento>() {
			@Override
			public void selectionChange(SelectionEvent<Apartamento> event) {
				opciones.setEnabled(event.getFirstSelectedItem().isPresent());
			}
		});
				
		vl.addComponent(grid);
		//vl.addComponent(gridAux);
		vl.addComponent(opciones);
		return vl;
	}
	
	private Button crearBotonOpcion(String caption, ClickListener clicklistener) {
		Button boton = new Button(caption);
		boton.addStyleName(ValoTheme.BUTTON_TINY);
		boton.addClickListener(clicklistener);
		return boton;
	}
	@Autowired
	public ApartamentoBuscar(OfertaDAO ud) {
		ofertaDao = ud;
	}
	
	@PostConstruct
	void init() {
		//usuario = userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado());
		setWidth("100%");		
		
		addComponent(apartamentos());
	}
}