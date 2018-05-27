package es.uca.iw.AlquileresVEFHM.vaadin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
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
import es.uca.iw.AlquileresVEFHM.DAO.OfertaDAO;
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
	private final OfertaDAO ofertaDao;
	
	private Integer windowfotoindice = 0;
	private Apartamento apartamento;
	private Set<Oferta> ofertas;
	
	@Autowired
	public ApartamentosVista(UserDAO ud, ApartamentoDAO ad, Foto_apartamentoDAO fd, OfertaDAO od) {
		userDao = ud;
		aparDao = ad;
		faDao = fd;
		ofertaDao = od;
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
				apartamento = grid.asSingleSelect().getOptionalValue().get();
				ofertas = apartamento.getOfertas();
				
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
				
				Grid<Oferta> gridoferta = new Grid<>();
				gridoferta.setSizeFull();
				gridoferta.setItems(ofertas);
				gridoferta.setSelectionMode(SelectionMode.NONE);
				gridoferta.addColumn(Oferta::getFecha).setCaption("Fecha").setId("fecha");
				gridoferta.addColumn(Oferta::getPrecio).setCaption("Precio");
				gridoferta.addColumn(Oferta::getPenalizacion).setCaption("Penalización");
				gridoferta.addComponentColumn(oferta -> {
					CheckBox reservado = new CheckBox();
					if(oferta.getReserva() != null) reservado.setValue(oferta.getReserva().isAceptada());
					else reservado.setValue(false);
					reservado.setReadOnly(true);
					return reservado;
				}).setCaption("Reservado");
				gridoferta.sort("fecha");
				vl.addComponent(gridoferta);
				
				Label nueva = new Label("Nueva oferta");
				nueva.addStyleName(ValoTheme.LABEL_H3);
				vl.addComponent(nueva);
				
				FormLayout fl = new FormLayout();
				vl.addComponent(fl);
				Binder<Oferta> binder = new Binder<>();
				DateField f_ini = new DateField("Fecha inicio");
				f_ini.setValue(LocalDate.now());
				binder.forField(f_ini)
					.asRequired()
					.withValidator(fecha -> fecha.compareTo(LocalDate.now()) > -1, "La fecha debe ser futura")
					.bind(Oferta::getLDFecha,Oferta::setLDFecha);
				fl.addComponent(f_ini);
				
				DateField f_fin = new DateField("Fecha fin");
				f_fin.setValue(LocalDate.now());
				binder.forField(f_fin)
					.asRequired()
					.withValidator(fecha -> fecha.compareTo(f_ini.getValue()) > -1, "La fecha debe ser mayor que la de la fecha de inicio")
					.bind(Oferta::getLDFecha,Oferta::setLDFecha);
				fl.addComponent(f_fin);

				TextField precio = new TextField("Precio");
				binder.forField(precio)
					.asRequired()
			    	.withConverter(Float::valueOf, String::valueOf, "Debe introducir un numero")
					.withValidator(pre -> pre >= 0.0f, "El precio debe ser positivo")
					.bind(Oferta::getPrecio, Oferta::setPrecio);
				fl.addComponent(precio);
				
				TextField penalizacion = new TextField("Penalización (%)");
				binder.forField(penalizacion)
					.asRequired()
			    	.withConverter(Float::valueOf, String::valueOf, "Debe introducir un numero")
					.withValidator(pe -> pe >= 0.0f, "La penalización ser positivo")
					.bind(Oferta::getPenalizacion, Oferta::setPenalizacion);
				fl.addComponent(penalizacion);
				
				Button registrar = new Button("Registrar", new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						if(binder.isValid()) {
							LocalDate fec = f_ini.getValue();
							Set<LocalDate> fechascogidas = new HashSet<LocalDate>();
							Set<LocalDate> fechaserroneas = new HashSet<LocalDate>();
							for(Oferta ofer : ofertas) fechascogidas.add(ofer.getLDFecha());
							while(fec.compareTo(f_fin.getValue()) != 1) {
								if(fechascogidas.contains(fec)) {
									fechaserroneas.add(fec);
								}
								System.out.println(fec.toString());
								fec = fec.plusDays(1);
							}
							if(fechaserroneas.isEmpty()) {
								fec = f_ini.getValue();
								Oferta ofer = new Oferta();
								while(fec.compareTo(f_fin.getValue()) != 1) {
									try {
										binder.writeBean(ofer);
										ofer.setApartamento(grid.asSingleSelect().getOptionalValue().get());
										ofer.setLDFecha(fec);
										ofertaDao.save(ofer);
									} catch (ValidationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									fec = fec.plusDays(1);
								}
								Notification.show("Ofertas registradas correctamente", Notification.TYPE_TRAY_NOTIFICATION);
								apartamento = aparDao.findById(grid.asSingleSelect().getOptionalValue().get().getId()).get();
								ofertas = apartamento.getOfertas();
								gridoferta.setItems(ofertas);
								gridoferta.sort("fecha");
								gridoferta.getDataProvider().refreshAll();
							} else {
								String errorfecha;
								if(fechaserroneas.size() == 1) errorfecha = "Ya existe una oferta para el dia: ";
								else errorfecha = "Ya existe una oferta para los dias: ";
								for(LocalDate fechaerronea : fechaserroneas) errorfecha += "\n" + fechaerronea;
								f_ini.setComponentError(new UserError("Fecha errónea"));
								f_fin.setComponentError(new UserError("Fecha errónea"));
								Notification.show(errorfecha, Notification.TYPE_ERROR_MESSAGE);
							}
						}
					}
				});
				fl.addComponent(registrar);
				
				getUI().getUI().addWindow(wofertas);
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
