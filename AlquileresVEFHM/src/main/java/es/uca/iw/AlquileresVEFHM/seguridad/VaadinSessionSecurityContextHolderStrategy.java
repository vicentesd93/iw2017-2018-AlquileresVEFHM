package es.uca.iw.AlquileresVEFHM.seguridad;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;

import com.vaadin.server.VaadinSession;

public class VaadinSessionSecurityContextHolderStrategy implements SecurityContextHolderStrategy {
	@Override
	public void clearContext() {
		getSession().setAttribute(SecurityContext.class, null);
	}
	
	@Override
	public SecurityContext getContext() {
		VaadinSession sesion = getSession();
		SecurityContext contexto = sesion.getAttribute(SecurityContext.class);
		if(contexto == null) {
			contexto = createEmptyContext();
			sesion.setAttribute(SecurityContext.class, contexto);
		}
		return contexto;
	}
	
	@Override
	public void setContext(SecurityContext contexto) {
		getSession().setAttribute(SecurityContext.class, contexto);
	}
	
	@Override
	public SecurityContext createEmptyContext() {
		return new SecurityContextImpl();
	}
	
	private static VaadinSession getSession() {
		VaadinSession sesion = VaadinSession.getCurrent();
		if(sesion == null) throw new IllegalStateException("El hilo actual no tiene ninguna VaadinSession vinculada");
		return sesion;
	}
	
}