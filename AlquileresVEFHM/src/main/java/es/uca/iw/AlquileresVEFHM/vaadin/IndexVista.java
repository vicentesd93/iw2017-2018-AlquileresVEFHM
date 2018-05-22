package es.uca.iw.AlquileresVEFHM.vaadin;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.User;
import es.uca.iw.AlquileresVEFHM.seguridad.SeguridadUtil;

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
		root.setSizeFull();
		
		// Creamos la cabecera 
		root.addComponent(new Label("Sesion: " + VaadinSession.getCurrent()));
		root.addComponent(new Label("UI: " + this.toString()));
		root.addComponent(new Label("Usuario: " + SeguridadUtil.getLoginUsuarioLogeado()));
		root.addComponent(new Label("Usuario logueado: " + SeguridadUtil.isLoggedIn()));
		if(SeguridadUtil.isLoggedIn()) {
			root.addComponent(new Label("Rol: " + Usuario().getAuthorities().iterator().next().getAuthority()));
		}

		// Creamos la barra de navegación
		final CssLayout barraNavegacion = new CssLayout();
		barraNavegacion.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		barraNavegacion.addComponent(crearBotonNavegacion("Inicio", LoginVista.VIEW_NAME));
		barraNavegacion.addComponent(crearBotonNavegacion("Buscar", LoginVista.VIEW_NAME));
		barraNavegacion.addComponent(crearBotonNavegacion("Contacto", LoginVista.VIEW_NAME));

		if(!SeguridadUtil.isLoggedIn()) {
			barraNavegacion.addComponent(crearBotonNavegacion("Iniciar sesión", LoginVista.VIEW_NAME));
			barraNavegacion.addComponent(crearBotonNavegacion("Registro", RegistroUsuarioVista.NOMBRE));
		}else {
			//barraNavegacion.addComponent(new Label(" Hola, "+Usuario().getNombre()+" "+Usuario().getApellidos()+" "));
			Button cuentaButton = new Button("Mi cuenta");
			cuentaButton.addStyleName(ValoTheme.BUTTON_SMALL);
			cuentaButton.addClickListener(event -> getUI().getNavigator().addView("cuenta", new CuentaUsuarioVista(Usuario())));
			cuentaButton.addClickListener(event -> getUI().getNavigator().navigateTo("cuenta"));
			barraNavegacion.addComponent(cuentaButton);
			Button logoutButton = new Button("Cerrar sesión", event -> logout());
			logoutButton.setStyleName(ValoTheme.BUTTON_SMALL);
			barraNavegacion.addComponent(logoutButton);
		}
		
		/*navigationBar.addComponent(createNavigationButton("Welcome", WelcomeView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("Users", UserView.VIEW_NAME));
		navigationBar.addComponent(createNavigationButton("User Management", UserManagementView.VIEW_NAME));
		*/root.addComponent(barraNavegacion);

		// Creamos el panel
		springViewDisplay = new Panel();
		springViewDisplay.setSizeFull();
		root.addComponent(springViewDisplay);
		root.setExpandRatio(springViewDisplay, 1.0f);
		addComponent(root);
		
		setSizeFull();
	}
	@Autowired
	private UserDAO userDao;
	private User usuario = null;
	private User Usuario() {
		if(usuario != null) return usuario;
		usuario = userDao.findByLogin(SeguridadUtil.getLoginUsuarioLogeado());
		return usuario;
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
