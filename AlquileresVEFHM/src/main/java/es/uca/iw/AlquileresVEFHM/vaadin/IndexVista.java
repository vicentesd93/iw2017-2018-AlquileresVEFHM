package es.uca.iw.AlquileresVEFHM.vaadin;

import java.io.File;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.FileResource;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;




@SuppressWarnings("serial")
@SpringViewDisplay
public class IndexVista extends VerticalLayout implements ViewDisplay {
	private Panel springViewDisplay;
	
	@Override
    public void attach() {
        super.attach();
        this.getUI().getNavigator().navigateTo("");
    }

	@Override
	public void showView(View view) {
		springViewDisplay.setContent((Component) view);
	}
	

	@PostConstruct
	void init() {
		final VerticalLayout root = new VerticalLayout();
		root.setWidth("100%");
		
		FileResource resource = new FileResource(new File( "img/logo-inmo.png"));
		Embedded logo = new Embedded(null, resource);		
		root.addComponent(logo);
		
		final CssLayout barraNavegacion = new CssLayout();
		barraNavegacion.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		barraNavegacion.addComponent(crearBotonNavegacion("Inicio", AnunciosMostrarVista.NOMBRE));

		if(!SeguridadUtil.isLoggedIn()) {
			barraNavegacion.addComponent(crearBotonNavegacion("Iniciar sesión", LoginVista.NOMBRE));
			barraNavegacion.addComponent(crearBotonNavegacion("Registro", RegistroUsuarioVista.NOMBRE));
		}else {
			barraNavegacion.addComponent(crearBotonNavegacion("Mi cuenta", CuentaUsuarioVista.NOMBRE));
			if(SeguridadUtil.getRol().equals("Anfitrion")) {
				MenuBar apartamentosMenu = new MenuBar();
				apartamentosMenu.addStyleName(ValoTheme.BUTTON_SMALL);
				MenuBar.Command comando = new MenuBar.Command() {
					@Override
					public void menuSelected(MenuItem selectedItem) {
						switch(selectedItem.getText()) {
						case "Añadir apartamento":
							getUI().getNavigator().navigateTo(ApartamentoRegistroVista.NOMBRE);
							break;
						case "Mis apartamentos":
							getUI().getNavigator().navigateTo(ApartamentosVista.NOMBRE);
							break;
						}
					}
				};
				MenuItem MIinicio = apartamentosMenu.addItem("Mis Apartamentos", null, null);
				MIinicio.addItem("Añadir apartamento", null, comando);
				MIinicio.addItem("Mis apartamentos", null, comando);
				barraNavegacion.addComponent(apartamentosMenu);
				barraNavegacion.addComponent(crearBotonNavegacion("Reservas",ReservaAnfitrionVista.NOMBRE));
				barraNavegacion.addComponent(crearBotonNavegacion("Incidencias",IncidenciasVista.NOMBRE));
				barraNavegacion.addComponent(crearBotonNavegacion("Facturación",FacturacionAnfitrionVista.NOMBRE));				
			}else if(SeguridadUtil.getRol().equals("Huesped")) {
				barraNavegacion.addComponent(crearBotonNavegacion("Reservas",ReservaHuespedVista.NOMBRE));
				barraNavegacion.addComponent(crearBotonNavegacion("Incidencias",IncidenciasVista.NOMBRE));
			}else if(SeguridadUtil.getRol().equals("admin")) {
				barraNavegacion.addComponent(crearBotonNavegacion("Administracion",AdministracionIndex.NOMBRE));
			}
			Button logoutButton = new Button("Cerrar sesión", event -> logout());
			logoutButton.setStyleName(ValoTheme.BUTTON_SMALL);
			barraNavegacion.addComponent(logoutButton);
		}
		root.addComponent(barraNavegacion);
		root.setComponentAlignment(barraNavegacion, Alignment.MIDDLE_CENTER);
		
		springViewDisplay = new Panel();
		springViewDisplay.setSizeFull();
		root.addComponent(springViewDisplay);
		root.setExpandRatio(springViewDisplay, 1.0f);
		addComponent(root);
		
		setWidth("100%");
	}
	
	private Button crearBotonNavegacion(String Titulo, final String Nombre_vista) {
		Button button = new Button(Titulo);
		button.addStyleName(ValoTheme.BUTTON_SMALL);
		button.addClickListener(event -> getUI().getNavigator().navigateTo(Nombre_vista));
		return button;
	}
	
	private void logout() {
		getUI().getPage().reload();
		getSession().close();
	}
}
