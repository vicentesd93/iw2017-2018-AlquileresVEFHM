package es.uca.iw.AlquileresVEFHM.vaadin;

import java.util.ArrayList;
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
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.EditorSaveEvent;
import com.vaadin.ui.components.grid.EditorSaveListener;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.ApartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.Foto_apartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Foto_apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Oferta;
import es.uca.iw.AlquileresVEFHM.modelos.User;
import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;

@SpringView(name = ApartamentosVista.NOMBRE)
@SuppressWarnings({"serial","deprecation"})
public class ApartamentosVista extends VerticalLayout implements View {
	public static final String NOMBRE = "apartamentos";
	private User usuario;
	private final UserDAO userDao;
	private final ApartamentoDAO aparDao;
	private final Foto_apartamentoDAO faDao;
	
	private Integer windowfotoindice = 0;
	
	@Autowired
	public ApartamentosVista(UserDAO ud, ApartamentoDAO ad, Foto_apartamentoDAO fd) {
		userDao = ud;
		aparDao = ad;
		faDao = fd;
	}

	@PostConstruct
	void init() {
		usuario = userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado());
		
		setWidth("100%");
		
		Set<Apartamento> apartamentos = usuario.getApartamentos();
		
		Label titulo = new Label("Mis apartamentos");
		titulo.addStyleName(ValoTheme.LABEL_HUGE);
		addComponent(titulo);
		
		TextField Edescripcion = new TextField();
		TextField Edireccion = new TextField();
		TextField Epoblacion = new TextField();
		TextField Epais = new TextField();
		Grid<Apartamento> grid = new Grid<>();
		grid.setSizeFull();
		grid.setItems(apartamentos);
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addColumn(Apartamento::getDescripcion).setCaption("Descripción").setEditorComponent(Edescripcion, Apartamento::setDescripcion);
		grid.addColumn(Apartamento::getDireccion).setCaption("Dirección").setEditorComponent(Edireccion, Apartamento::setDireccion);
		grid.addColumn(Apartamento::getPoblacion).setCaption("Población").setEditorComponent(Epoblacion, Apartamento::setPoblacion);
		grid.addColumn(Apartamento::getPais).setCaption("País").setEditorComponent(Epais, Apartamento::setPais);
		grid.getEditor().setEnabled(true);
		grid.getEditor().setSaveCaption("Guardar");
		grid.getEditor().setCancelCaption("Cancelar");
		grid.getEditor().addSaveListener(new EditorSaveListener<Apartamento>() {
			@Override
			public void onEditorSave(EditorSaveEvent<Apartamento> event) {
				aparDao.save(event.getBean());
				Notification.show("Apartamento actualizado", Notification.TYPE_TRAY_NOTIFICATION).setPosition(Notification.POSITION_CENTERED_TOP);;
			}
		});
		addComponent(grid);

				
		CssLayout opciones = new CssLayout();
		opciones.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		opciones.setEnabled(false);
		opciones.addComponent(crearBotonOpcion("Ofertas", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
					Set<Oferta> ofertas;
					Window wofertas = new Window("Ofertas");
					wofertas.setResizable(false);
					wofertas.setDraggable(false);
					wofertas.setModal(true);
					wofertas.setWidth("70%");
					wofertas.setHeight("70%");
					
					VerticalLayout vl = new VerticalLayout();
					wofertas.setContent(vl);
					
					Label titulo = new Label("Ofertas");
					titulo.addStyleName(ValoTheme.LABEL_HUGE);
					vl.addComponent(titulo);
					
					Label existentes = new Label("Ofertas actuales");
					existentes.addStyleName(ValoTheme.LABEL_H3);
					vl.addComponent(existentes);
					
					Grid<Apartamento> grid = new Grid<>();
					grid.setSizeFull();
					grid.setItems(apartamentos);
			}
		}));
		
		opciones.addComponent(crearBotonOpcion("Reservas", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
					Notification.show("Reservas");
			}
		}));
		
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
				
				opc.addComponent(crearBotonOpcion("Añadir Imagen", new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {

						
					}
				}));
				opc.addComponent(crearBotonOpcion("Eliminar imagen", new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						Window eliminar = new Window("Eliminar imagen");
						eliminar.setWidth(100.0f, Unit.PIXELS);
						eliminar.setModal(true);
						eliminar.setResizable(false);
						eliminar.setClosable(false);
						eliminar.setDraggable(false);
						
						VerticalLayout vl = new VerticalLayout();
						eliminar.setContent(vl);

						Label mensaje = new Label("¿Eliminar imagen?");
						vl.addComponent(mensaje);
						vl.setComponentAlignment(mensaje, Alignment.MIDDLE_CENTER);
						
						HorizontalLayout hl = new HorizontalLayout();
						vl.addComponent(hl);
						
						hl.setWidth("100%");
						Button si = new Button("Si", new ClickListener() {
							@Override
							public void buttonClick(ClickEvent event) {
								faDao.delete(foto_apartamento);
								fotos.remove(foto_apartamento);
								ind.setValue(String.valueOf(windowfotoindice + 1) + "/" + String.valueOf(fotos.size()));
								eliminar.close();
								ventana.close();
								getUI().getUI().addWindow(ventana);
							}
							
						});
						si.addStyleName(ValoTheme.BUTTON_FRIENDLY);
						hl.addComponent(si);
						hl.setComponentAlignment(si, Alignment.MIDDLE_CENTER);
						
						Button no = new Button("No", new ClickListener() {
							@Override
							public void buttonClick(ClickEvent event) {
								eliminar.close();
							}
						});
						no.addStyleName(ValoTheme.BUTTON_DANGER);
						hl.addComponent(no);
						hl.setComponentAlignment(no, Alignment.MIDDLE_CENTER);
						
						getUI().getUI().addWindow(eliminar);
					}
				}));
				
				vl.setMargin(true);
				vl.setWidth("100%");
				ventana.setContent(vl);
				
				getUI().getUI().addWindow(ventana);
			}
		}));
		
		opciones.addComponent(crearBotonOpcion("Modificar", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				
			}
		}));
		addComponent(opciones);
		setComponentAlignment(opciones, Alignment.MIDDLE_CENTER);		
		grid.addSelectionListener(new SelectionListener<Apartamento>() {
			@Override
			public void selectionChange(SelectionEvent<Apartamento> event) {
				opciones.setEnabled(event.getFirstSelectedItem().isPresent());
			}
		});
	}
	
	private Button crearBotonOpcion(String caption, ClickListener clicklistener) {
		Button boton = new Button(caption);
		boton.addStyleName(ValoTheme.BUTTON_TINY);
		boton.addClickListener(clicklistener);
		return boton;
	}
}
