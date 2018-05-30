package es.uca.iw.AlquileresVEFHM.vaadin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings({"serial", "deprecation"})
@SpringView(name = LoginVista.NOMBRE)
public class LoginVista extends VerticalLayout implements View{
	public static final String NOMBRE = "login";
	
	public LoginVista() {
		setMargin(true);
        setSpacing(true);

        TextField usuario = new TextField("Usuario");
        addComponent(usuario);
        
        PasswordField clave = new PasswordField("Clave");
        clave.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        clave.setIcon(FontAwesome.LOCK);
        addComponent(clave);

        Button login = new Button("Login", evt -> {
            String pass = clave.getValue();
            clave.setValue("");
            if (!login(usuario.getValue(), pass)) {
                Notification.show("Login fallido");
                usuario.focus();
            }
        });
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        addComponent(login);
	}
	
	@FunctionalInterface
    public interface LoginCallback {
        boolean login(String username, String password);
	}
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	private boolean login(String username, String password) {
		try {
			Authentication token = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			// Reinitialize the session to protect against session fixation
			// attacks. This does not work with websocket communication.
			VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
			SecurityContextHolder.getContext().setAuthentication(token);
			Page.getCurrent().reload();
			// Show the main UI
			//showIndexVista();
			return true;
		} catch (AuthenticationException ex) {
			return false;
		}
	}
}
