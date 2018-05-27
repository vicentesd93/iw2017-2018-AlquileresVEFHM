package es.uca.iw.AlquileresVEFHM.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@SpringUI
public class VaadinUI extends UI {
	@Autowired
	SpringViewProvider viewProvider;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	IndexVista indexVista;
	
	@Override
    protected void init(VaadinRequest request) {
		this.getUI().getNavigator().setErrorView(ErrorVista.class);
		viewProvider.setAccessDeniedViewClass(AccesoDenegadoVista.class);
		setSizeFull();
		System.out.println("INICIO");
		/*if (SeguridadUtil.isLoggedIn()) {
			System.out.println("Index");
			showIndexVista();
			System.out.println("Index fin");
		} else {
			System.out.println("Login");
			showLoginVista();
			System.out.println("Login fin");
		}*/
		showIndexVista();
	}
	
	/*private void showLoginVista() {
		setContent(new LoginVista(this::login));
	}*/
	private void showIndexVista() {
		setContent(indexVista);
	}
}
