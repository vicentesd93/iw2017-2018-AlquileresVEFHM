package es.uca.iw.AlquileresVEFHM.vaadin;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.navigator.View;
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
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.IncidenciaDAO;
import es.uca.iw.AlquileresVEFHM.DAO.ReservaDAO;
import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Incidencia;
import es.uca.iw.AlquileresVEFHM.modelos.Reserva;
import es.uca.iw.AlquileresVEFHM.modelos.ReservaOferta;
import es.uca.iw.AlquileresVEFHM.modelos.User;
import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;

@SuppressWarnings({"serial", "deprecation"})
@SpringView(name = IncidenciasVista.NOMBRE)
public class IncidenciasVista extends VerticalLayout implements View {
	public static final String NOMBRE = "Incidencias";
	
	User receptor;
	
	private final UserDAO userDao;
	private final IncidenciaDAO incidenciaDao;
	private final ReservaDAO reservaDao;
	
	@Autowired
	public IncidenciasVista(UserDAO ud, IncidenciaDAO id, ReservaDAO rd) {
		userDao = ud;
		incidenciaDao = id;
		reservaDao = rd;
	}
	
	@PostConstruct
	void init() {
		if(!SeguridadUtil.isLoggedIn() || SeguridadUtil.isLoggedIn() && !SeguridadUtil.getRol().equals("Huesped") && !SeguridadUtil.getRol().equals("Anfitrion")) {
			Notification.show("No tiene permisos para acceder a la página", Notification.TYPE_ERROR_MESSAGE);
			Page.getCurrent().open("/#!login", null);
			return;
		}
		
		setSizeFull();
		
		User emisor = userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado());
		
		List<Reserva> reservas;
		if(SeguridadUtil.getRol().equals("Huesped")) {
			reservas = reservaDao.findByHuesped(emisor);
		}else {
			reservas = reservaDao.buscarporAnfitrion(emisor);
		}
		
		HorizontalLayout hl = new HorizontalLayout();
        Button crear = new Button("Crear Incidencia");
		NativeSelect<Reserva> reserv = new NativeSelect<>();
        TextArea tarea = new TextArea();
        TextField recf = new TextField();
		if(reservas.size() != 0) {
			Label nuevai = new Label("Nueva Incidencia");
			addComponent(nuevai);
			
			Label resl = new Label("Reserva");
			hl.addComponent(resl);
			
			reserv.setItems(reservas);
			reserv.setEmptySelectionAllowed(false);
			reserv.setSelectedItem(reservas.get(0));
	        hl.addComponent(reserv);
	        addComponent(hl);
	        
	        if(SeguridadUtil.getRol().equals("Huesped")) {
	 			receptor = reserv.getValue().getAnfitrion();
	 		}else {
	 			receptor = reserv.getValue().getHuesped();
	 		}
	        
	        hl = new HorizontalLayout();
	        Label emil = new Label("Emisor");
	        hl.addComponent(emil);
	        
	        TextField emif = new TextField();
	        emif.setEnabled(false);
	        emif.setValue(emisor.getNombre() + " " + emisor.getApellidos());
	        hl.addComponent(emif);
	        addComponent(hl);
	        
	        hl = new HorizontalLayout();
	        Label recl = new Label("Receptor");
	        hl.addComponent(recl);
	        
	        recf.setEnabled(false);
	        recf.setValue(receptor.getNombre() + " " + receptor.getApellidos());
	        hl.addComponent(recf);
	        addComponent(hl);
	        
	        reserv.addValueChangeListener(reserva -> {
	        	if(SeguridadUtil.getRol().equals("Huesped")) {
	     			receptor = reserv.getValue().getAnfitrion();
	     		}else {
	     			receptor = reserv.getValue().getHuesped();
	     		}
	        	recf.setValue(receptor.getNombre() + " " + receptor.getApellidos());
	        });
	        
	        Label desl = new Label("Descripción");
	        addComponent(desl);
	        
	        addComponent(tarea);
	        
	        addComponent(crear);
		}
		
		Label nombre = new Label("Recibidas");
		addComponent(nombre);
		
		Grid<Incidencia> recibidas = new Grid<>();
		recibidas.addComponentColumn(incidencia -> {
			Button estado = new Button();
			if(incidencia.isAbierta()) {
				estado.setCaption("Abierta");
				estado.setStyleName(ValoTheme.BUTTON_DANGER);
			}else {
				estado.setCaption("Cerrada");
				estado.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			}
			estado.setEnabled(false);
			estado.setSizeFull();
			return estado;
		}).setCaption("Estado");
		recibidas.addColumn(Incidencia::getId).setCaption("Id");
		recibidas.addColumn(Incidencia::getFecha).setCaption("Fecha");
		recibidas.addColumn(Incidencia::getEmisor).setCaption("Emisor");
		recibidas.addColumn(Incidencia::getReceptor).setCaption("Receptor");
		recibidas.setItems(incidenciaDao.findByreceptor(emisor));
		recibidas.setSelectionMode(SelectionMode.SINGLE);
		recibidas.setWidth("100%");
		addComponent(recibidas);
		
		hl = new HorizontalLayout();
		Button datosrecibidos = new Button();
		datosrecibidos.setCaption("Datos");
		datosrecibidos.setEnabled(false);
		hl.addComponent(datosrecibidos);
		Button resolverrecibidos = new Button();
		resolverrecibidos.setCaption("Resolver Incidencia");
		resolverrecibidos.setEnabled(false);
		hl.addComponent(resolverrecibidos);
		addComponent(hl);
		
		Label nombre2 = new Label("Enviadas");
		addComponent(nombre2);
		
		Grid<Incidencia> enviadas = new Grid<>();
		enviadas.addComponentColumn(incidencia -> {
			Button estado = new Button();
			if(incidencia.isAbierta()) {
				estado.setCaption("Abierta");
				estado.setStyleName(ValoTheme.BUTTON_DANGER);
			}else {
				estado.setCaption("Cerrada");
				estado.setStyleName(ValoTheme.BUTTON_FRIENDLY);
			}
			estado.setEnabled(false);
			estado.setSizeFull();
			return estado;
		}).setCaption("Estado");
		enviadas.addColumn(Incidencia::getId).setCaption("Id");
		enviadas.addColumn(Incidencia::getFecha).setCaption("Fecha");
		enviadas.addColumn(Incidencia::getEmisor).setCaption("Emisor");
		enviadas.addColumn(Incidencia::getReceptor).setCaption("Receptor");
		enviadas.setItems(incidenciaDao.findByemisor(emisor));
		enviadas.setSelectionMode(SelectionMode.SINGLE);
		enviadas.setWidth("100%");
		addComponent(enviadas);
		
		Button datosenviados = new Button();
		datosenviados.setCaption("Datos");
		datosenviados.setEnabled(false);
		addComponent(datosenviados);
			
		crear.addClickListener(click -> {
        	Incidencia inci = new Incidencia();
        	inci.setAbierta(true);
        	inci.setDescripcion(tarea.getValue());
        	inci.setEmisor(emisor);
        	inci.setFecha(new Date());
        	inci.setReceptor(receptor);
        	inci.setReserva(reserv.getValue());
        	incidenciaDao.save(inci);
        	Notification.show("Incidencia registrada", Type.WARNING_MESSAGE);
        	enviadas.setItems(incidenciaDao.findByemisor(emisor));
        	reserv.setValue(reservas.get(0));
        	if(SeguridadUtil.getRol().equals("Huesped")) {
     			receptor = reserv.getValue().getAnfitrion();
     		}else {
     			receptor = reserv.getValue().getHuesped();
     		}
        	recf.setValue(receptor.getNombre() + " " + receptor.getApellidos());
        	tarea.setValue("");
        });
		
		recibidas.addSelectionListener(new SelectionListener<Incidencia>() {
			@Override
			public void selectionChange(SelectionEvent<Incidencia> event) {
				if(recibidas.asSingleSelect().getValue() == null) {
					datosrecibidos.setEnabled(false);
					resolverrecibidos.setEnabled(false);
				}else {
					datosrecibidos.setEnabled(true);
					if(recibidas.asSingleSelect().getValue().isAbierta()) resolverrecibidos.setEnabled(true);
					else resolverrecibidos.setEnabled(false);
				}
			}
		});
		
		enviadas.addSelectionListener(new SelectionListener<Incidencia>() {
			@Override
			public void selectionChange(SelectionEvent<Incidencia> event) {
				if(enviadas.asSingleSelect().getValue() == null) {
					datosenviados.setEnabled(false);
				}else {
					datosenviados.setEnabled(true);
				}
			}
		});
		
		datosrecibidos.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Window ventana = new Window("Datos");
				ventana.setClosable(true);
				ventana.setResizable(false);
				ventana.setModal(true);
				ventana.setWidth("80%");
				
				VerticalLayout vl = new VerticalLayout();
				vl.addComponent(new Label("Emisor: " + recibidas.asSingleSelect().getValue().getEmisor()));
				vl.addComponent(new Label("Receptor: " + recibidas.asSingleSelect().getValue().getReceptor()));
				Label apal = new Label("Apartamento");
				apal.setStyleName(ValoTheme.LABEL_H3);
				vl.addComponent(apal);
				vl.addComponent(new Label("Dirección: " + recibidas.asSingleSelect().getValue().getReserva().getReservasofertas().iterator().next().getOferta().getApartamento().getDireccion()));
				vl.addComponent(new Label("Población: " + recibidas.asSingleSelect().getValue().getReserva().getReservasofertas().iterator().next().getOferta().getApartamento().getPoblacion()));
				vl.addComponent(new Label("País: " + recibidas.asSingleSelect().getValue().getReserva().getReservasofertas().iterator().next().getOferta().getApartamento().getPais()));
				vl.addComponent(new Label("Descripción: " + recibidas.asSingleSelect().getValue().getReserva().getReservasofertas().iterator().next().getOferta().getApartamento().getDescripcion()));
				Label fecl = new Label("Fechas reserva");
				fecl.setStyleName(ValoTheme.LABEL_H3);
				vl.addComponent(fecl);
				for(ReservaOferta ro : recibidas.asSingleSelect().getValue().getReserva().getReservasofertas()) {
					vl.addComponent(new Label(ro.getOferta().getFecha().toString()));
				}
				Label incl = new Label("Datos Incidencia");
				incl.setStyleName(ValoTheme.LABEL_H3);
				vl.addComponent(incl);
				vl.addComponent(new Label("Fecha: " + recibidas.asSingleSelect().getValue().getFecha().toString()));
				Label actl = new Label();
				if(recibidas.asSingleSelect().getValue().isAbierta()) {
					actl.setValue("Abierta");
					actl.addStyleName(ValoTheme.LABEL_FAILURE);
				}else {
					actl.setValue("Cerrada");
					actl.addStyleName(ValoTheme.LABEL_SUCCESS);
				}
				vl.addComponent(actl);
				TextArea des = new TextArea();
				des.setCaption("Descripción");
				des.setValue(recibidas.asSingleSelect().getValue().getDescripcion());
				des.setEnabled(false);
				vl.addComponent(des);
				if(!recibidas.asSingleSelect().getValue().isAbierta()) {
					TextArea res = new TextArea();
					res.setCaption("Resolución");
					if(recibidas.asSingleSelect().getValue().getResolucion() == null) res.setValue("");
					else res.setValue(recibidas.asSingleSelect().getValue().getResolucion());
					res.setEnabled(false);
					vl.addComponent(res);
				}
				Button cerrar = new Button("Cerrar");
				cerrar.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						ventana.close();
					}
				});
				vl.addComponent(cerrar);
				vl.setComponentAlignment(cerrar, Alignment.MIDDLE_CENTER);
				ventana.setContent(vl);
				getUI().addWindow(ventana);
			}
		});
		
		datosenviados.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Window ventana = new Window("Datos");
				ventana.setClosable(true);
				ventana.setResizable(false);
				ventana.setModal(true);
				ventana.setWidth("80%");
				
				VerticalLayout vl = new VerticalLayout();
				vl.addComponent(new Label("Emisor: " + enviadas.asSingleSelect().getValue().getEmisor()));
				vl.addComponent(new Label("Receptor: " + enviadas.asSingleSelect().getValue().getReceptor()));
				Label apal = new Label("Apartamento");
				apal.setStyleName(ValoTheme.LABEL_H3);
				vl.addComponent(apal);
				vl.addComponent(new Label("Dirección: " + enviadas.asSingleSelect().getValue().getReserva().getReservasofertas().iterator().next().getOferta().getApartamento().getDireccion()));
				vl.addComponent(new Label("Población: " + enviadas.asSingleSelect().getValue().getReserva().getReservasofertas().iterator().next().getOferta().getApartamento().getPoblacion()));
				vl.addComponent(new Label("País: " + enviadas.asSingleSelect().getValue().getReserva().getReservasofertas().iterator().next().getOferta().getApartamento().getPais()));
				vl.addComponent(new Label("Descripción: " + enviadas.asSingleSelect().getValue().getReserva().getReservasofertas().iterator().next().getOferta().getApartamento().getDescripcion()));
				Label fecl = new Label("Fechas reserva");
				fecl.setStyleName(ValoTheme.LABEL_H3);
				vl.addComponent(fecl);
				for(ReservaOferta ro : enviadas.asSingleSelect().getValue().getReserva().getReservasofertas()) {
					vl.addComponent(new Label(ro.getOferta().getFecha().toString()));
				}
				Label incl = new Label("Datos Incidencia");
				incl.setStyleName(ValoTheme.LABEL_H3);
				vl.addComponent(incl);
				vl.addComponent(new Label("Fecha: " + enviadas.asSingleSelect().getValue().getFecha().toString()));
				Label actl = new Label();
				if(enviadas.asSingleSelect().getValue().isAbierta()) {
					actl.setValue("Abierta");
					actl.addStyleName(ValoTheme.LABEL_FAILURE);
				}else {
					actl.setValue("Cerrada");
					actl.addStyleName(ValoTheme.LABEL_SUCCESS);
				}
				vl.addComponent(actl);
				TextArea des = new TextArea();
				des.setCaption("Descripción");
				des.setValue(enviadas.asSingleSelect().getValue().getDescripcion());
				des.setEnabled(false);
				vl.addComponent(des);
				if(!enviadas.asSingleSelect().getValue().isAbierta()) {
					TextArea res = new TextArea();
					res.setCaption("Resolución");
					
					if(enviadas.asSingleSelect().getValue().getResolucion() == null) res.setValue("");
					else res.setValue(enviadas.asSingleSelect().getValue().getResolucion());
					res.setEnabled(false);
					vl.addComponent(res);
				}
				Button cerrar = new Button("Cerrar");
				cerrar.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						ventana.close();
					}
				});
				vl.addComponent(cerrar);
				vl.setComponentAlignment(cerrar, Alignment.MIDDLE_CENTER);
				ventana.setContent(vl);
				getUI().addWindow(ventana);
			}
		});
		
		resolverrecibidos.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Window ventana = new Window("Resolver Incidencia");
				ventana.setClosable(true);
				ventana.setResizable(false);
				ventana.setModal(true);
				ventana.setWidth("80%");
				
				VerticalLayout vl = new VerticalLayout();
				vl.addComponent(new Label("Resolver Incidencia"));
				TextArea sol = new TextArea();
				sol.setWidth("100%");
				vl.addComponent(sol);
				
				HorizontalLayout hl = new HorizontalLayout();
				Button aceptar = new Button("Aceptar");
				aceptar.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						Incidencia in = recibidas.asSingleSelect().getValue();
						in.setAbierta(false);
						if(sol.getValue() == null) in.setResolucion("");
						else in.setResolucion(sol.getValue());
						incidenciaDao.save(in);
						Notification.show("Incidencia resuelta", Type.WARNING_MESSAGE);
						recibidas.setItems(incidenciaDao.findByreceptor(emisor));
						recibidas.getDataProvider().refreshAll();
						ventana.close();
					}
				});
				hl.addComponent(aceptar);
				Button cerrar = new Button("Cerrar");
				cerrar.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						ventana.close();
					}
				});
				hl.addComponent(cerrar);
				vl.addComponent(hl);
				ventana.setContent(vl);
				getUI().addWindow(ventana);
			}
		});
	}
}
