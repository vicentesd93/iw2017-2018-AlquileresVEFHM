package es.uca.iw.AlquileresVEFHM.seguridad;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

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
    
    public static Collection<? extends GrantedAuthority> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null ) return authentication.getAuthorities();
        return null;
    }
    
    public static String getRol() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null ) return authentication.getAuthorities().iterator().next().getAuthority();
        return null;
    }
    
    public static String getLoginUsuarioLogeado() {
    	Authentication aut = SecurityContextHolder.getContext().getAuthentication();
    	if(aut != null) return aut.getName();
    	return "";
    }
}
