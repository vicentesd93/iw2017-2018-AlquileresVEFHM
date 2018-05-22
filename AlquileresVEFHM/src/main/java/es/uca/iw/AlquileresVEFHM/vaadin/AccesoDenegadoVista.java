package es.uca.iw.AlquileresVEFHM.vaadin;

import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Component
@UIScope
public class AccesoDenegadoVista extends VerticalLayout implements View {
	public AccesoDenegadoVista() {
        setMargin(true);
        Label lbl = new Label("No tienes acceso a esta página.");
        lbl.addStyleName(ValoTheme.LABEL_FAILURE);
        lbl.setSizeUndefined();
        addComponent(lbl);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {}
}
