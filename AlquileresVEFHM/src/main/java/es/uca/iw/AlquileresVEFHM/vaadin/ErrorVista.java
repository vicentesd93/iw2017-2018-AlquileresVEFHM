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
public class ErrorVista extends VerticalLayout implements View {
	private Label errorLabel;

    public ErrorVista() {
        setMargin(true);
        errorLabel = new Label();
        errorLabel.addStyleName(ValoTheme.LABEL_FAILURE);
        errorLabel.setSizeUndefined();
        addComponent(errorLabel);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        errorLabel.setValue(String.format("No se encuentra la vista: %s", event.getViewName()));
}
}
