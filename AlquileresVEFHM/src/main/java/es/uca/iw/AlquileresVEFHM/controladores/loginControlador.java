package es.uca.iw.AlquileresVEFHM.controladores;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.uca.iw.AlquileresVEFHM.DAO.RolDAO;
import es.uca.iw.AlquileresVEFHM.DAO.UsuarioDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Rol;
import es.uca.iw.AlquileresVEFHM.modelos.Usuario;

@Controller
public class loginControlador {
	@Autowired
	private UsuarioDAO userDao;
	@Autowired
	private RolDAO rolDao;
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	@RequestMapping(value="/usuario", method=RequestMethod.GET)
	@ResponseBody
	public String usuario(Principal principal) {
		Usuario u = userDao.findByLogin(principal.getName());
		Optional<Rol> rol = rolDao.findById(u.getRol());
		return u.getNombre()+" "+u.getApellidos()+" "+rol.get().getNombre();
	}
}
