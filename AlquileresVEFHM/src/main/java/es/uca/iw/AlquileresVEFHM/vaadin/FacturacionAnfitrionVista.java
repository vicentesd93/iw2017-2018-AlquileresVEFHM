package es.uca.iw.AlquileresVEFHM.vaadin;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.FacturaDAO;
import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Factura;
import es.uca.iw.AlquileresVEFHM.modelos.ReservaOferta;
import es.uca.iw.AlquileresVEFHM.modelos.User;
import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;

@SuppressWarnings({"serial", "deprecation"})
@SpringView(name = FacturacionAnfitrionVista.NOMBRE)
public class FacturacionAnfitrionVista extends VerticalLayout implements View {
	public static final String NOMBRE = "facturacion_anfitrion";
	
	private final FacturaDAO facturaDao;
	private final UserDAO usuarioDao;
	
	@Autowired
	public FacturacionAnfitrionVista(FacturaDAO fd, UserDAO ud) {
		facturaDao = fd;
		usuarioDao = ud;
	}
	
	@PostConstruct
	void init() {
		if(!SeguridadUtil.isLoggedIn() || SeguridadUtil.isLoggedIn() && !SeguridadUtil.getRol().equals("Anfitrion")) {
			Notification.show("No tiene permisos para acceder a la página", Notification.TYPE_ERROR_MESSAGE);
			Page.getCurrent().open("/#!login", null);
			return;
		}
		
		User user = usuarioDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado());
		
		Grid<Factura> facturacion = new Grid<>();
		facturacion.addColumn(Factura::getId).setCaption("Id");
		facturacion.addComponentColumn(factura -> {
			Button reserva = new Button("Ver reserva");
			reserva.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			reserva.addClickListener(click -> {
				Window ventana = new Window("Reserva");
				ventana.setClosable(true);
				ventana.setResizable(false);
				ventana.setModal(true);
				ventana.setWidth("80%");
				VerticalLayout vlv = new VerticalLayout();
				vlv.addComponent(new Label("Huesped: " + factura.getReserva().getHuesped()));
				vlv.addComponent(new Label("Fechas:"));
				for(ReservaOferta ro : factura.getReserva().getReservasofertas()) {
					vlv.addComponent(new Label(ro.getOferta().getFecha().toString()));
				}
				vlv.addComponent(new Label("APARTAMENTO"));
				vlv.addComponent(new Label("Dirección: " + factura.getReserva().getReservasofertas().iterator().next().getOferta().getApartamento().getDireccion()));
				vlv.addComponent(new Label("Población: " + factura.getReserva().getReservasofertas().iterator().next().getOferta().getApartamento().getPoblacion()));
				vlv.addComponent(new Label("País: " + factura.getReserva().getReservasofertas().iterator().next().getOferta().getApartamento().getPais()));
				vlv.addComponent(new Label("Descripción: " + factura.getReserva().getReservasofertas().iterator().next().getOferta().getApartamento().getDescripcion()));
				Button cerrar = new Button("Cerrar", event -> { 
					ventana.close();
				});
				vlv.addComponent(cerrar);
				ventana.setContent(vlv);
				getUI().addWindow(ventana);
			});
			return reserva;
		}).setCaption("Reserva");
		facturacion.addColumn(factura -> {
			return factura.getMetodo_pago().getDescripcion();
		}).setCaption("Método de pago");
		facturacion.addColumn(factura -> {
			return factura.getMetodo_pago().getCargo_adicional();
		}).setCaption("Recargo %");
		facturacion.addColumn(factura -> {
			return String.format("%.2f €", factura.getTotal() - factura.getIva() - factura.getComision());
		}).setCaption("Precio");
		facturacion.addColumn(factura -> {
			return String.format("%.2f €", factura.getIva());
		}).setCaption("IVA");
		facturacion.addColumn(factura -> {
			return String.format("%.2f €", factura.getComision());
		}).setCaption("Recargo");
		facturacion.addColumn(factura -> {
			return String.format("%.2f €", factura.getTotal());
		}).setCaption("Total");
		facturacion.addColumn(factura -> {
			return String.format("%.2f €", (factura.getTotal() - factura.getIva() - factura.getComision()) * 0.95);
		}).setCaption("Ganancia");
		facturacion.setSelectionMode(SelectionMode.NONE);
		facturacion.setItems(facturaDao.buscarAnfitrion(user));
		facturacion.setWidth("100%");
		addComponent(facturacion);
		float iva = 0;
		float recargo = 0;
		float totbruto = 0;
		float comision = 0;
		float total = 0;
		for(Factura fac : facturaDao.findAll()) {
			iva += fac.getIva();
			recargo += fac.getComision();
			totbruto += (float)(fac.getTotal() - fac.getIva() - fac.getComision());
			comision += (float)((fac.getTotal() - fac.getIva() - fac.getComision()) * 0.95);
			total += fac.getTotal();
		}
		addComponent(new Label("IVA total: " + String.format("%.2f", iva) + " € "
				+ "Recargo total: " + String.format("%.2f", recargo) + " € "
				+ "Total bruto: " + String.format("%.2f", totbruto) + " € "
				+ "Total neto: " + String.format("%.2f", total) + " €"));
		Label ganan = new Label("Ganancia total: " + String.format("%.2f", comision) + " € ");
		ganan.addStyleName(ValoTheme.LABEL_H4);
		addComponent(ganan);
	}
}
