package es.uca.iw.AlquileresVEFHM.seguridad;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;

import es.uca.iw.AlquileresVEFHM.modelos.User;

public final class SeguridadUtil {
	@Autowired
	AuthenticationManager authenticationManager;
	
	private SeguridadUtil() {}
	
	public static boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static boolean hasRole(String rol) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority(rol));
    }
    
    public static Collection<? extends GrantedAuthority> roles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null ){
        	return authentication.getAuthorities();
        } else{
        	return null;
        }
    }
    
    public static String getLoginUsuarioLogeado() {
    	Authentication aut = SecurityContextHolder.getContext().getAuthentication();
    	if(aut != null) return aut.getName();
    	return "";
    }
    public boolean login(String username, String password) {
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
