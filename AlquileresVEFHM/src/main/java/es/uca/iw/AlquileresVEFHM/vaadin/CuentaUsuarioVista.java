package es.uca.iw.AlquileresVEFHM.vaadin;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.modelos.User;

@SpringView()
public class CuentaUsuarioVista extends HorizontalLayout implements View {
	public final static String NOMBRE = "cuenta_usuario";
	private User usuario;
	
	private Component datos() {
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		Label titulo = new Label("Datos personales");
		titulo.addStyleName(ValoTheme.LABEL_HUGE);
		vl.addComponent(titulo);
		return vl;
	}
	
	public CuentaUsuarioVista(User u) {
		usuario = u;
		//COMPROBAR QUE SEA UN USUARIO
		setSizeFull();
		TabSheet tabs = new TabSheet();
		tabs.setSizeFull();
		tabs.addStyleName(ValoTheme.TABSHEET_FRAMED);
		Panel tab1 = new Panel();
		tab1.setContent(datos());
		tabs.addTab(tab1, "Mis datos");
		Panel tab2 = new Panel();
		tabs.addTab(tab2, "Modificar datos");
		Panel tab3 = new Panel();
		tabs.addTab(tab3, "Cambiar contraseña");
		Panel tab4 = new Panel();
		tabs.addTab(tab4, "Borrar cuenta");
		addComponent(tabs);
	}
}
