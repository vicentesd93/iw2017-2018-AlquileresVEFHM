package es.uca.iw.AlquileresVEFHM.vaadin;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.AlquileresVEFHM.modelos.User;

@SpringView(name = CuentaUsuarioVista.NOMBRE)
public class CuentaUsuarioVista extends VerticalLayout implements View {
	public final static String NOMBRE = "cuenta_usuario";
	
	public CuentaUsuarioVista() {
		addComponent(new Label("MI CUENTA"));
	}
	
	public CuentaUsuarioVista(User u) {
		addComponent(new Label("MI CUENTA yo soy"+u.getNombre()));
	}
}
