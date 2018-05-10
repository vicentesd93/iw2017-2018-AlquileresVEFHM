package es.uca.iw.AlquileresVEFHM.controladores;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.uca.iw.AlquileresVEFHM.DAO.UsuarioDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Usuario;

@Controller
public class loginControlador {
	@Autowired
	private UsuarioDAO userDao;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		return new ModelAndView("login");
	}
	
	@RequestMapping(value = "/usuario", method = RequestMethod.GET)
	@ResponseBody
	public String usuario(Principal principal) {
		Usuario u = userDao.findByLogin(principal.getName());
		return u.getNombre()+" "+u.getApellidos()+" "+u.getRol().getNombre();
	}
}
