package es.uca.iw.AlquileresVEFHM.controladores;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.uca.iw.AlquileresVEFHM.modelos.Login;
import es.uca.iw.AlquileresVEFHM.modelos.Usuario;
import es.uca.iw.AlquileresVEFHM.modelos.UsuarioDao;

@Controller
public class gestion_usuarioControlador {
	@Autowired
	private UsuarioDao userDao;
	
	@RequestMapping("/")
    public String index() {
        return "index";
    }
	
	/*@RequestMapping(value="/usuario", method=RequestMethod.GET)
	@ResponseBody
	public String usuario_GET() {
		Optional<Usuario> user = null;
		try {
			user = userDao.findById(1);
			System.out.println("APELLIDOS: "+user.get().getApellidos());
		}catch(Exception e) {
			return e.toString();
		}
		return "si";
	}*/
	
	@RequestMapping(value="/modificar/{id}", method=RequestMethod.GET)
	public ModelAndView modificar_usuario_GET(@PathVariable(value="id") Integer id) {
		return new ModelAndView("modificar_usuario", "usuario", userDao.findById(id).get());
	}
	
	@RequestMapping(value="/modificar", method=RequestMethod.POST)
	@ResponseBody
	public String modificar_usuario_POST(@ModelAttribute("Usuario")Usuario usuario) {
		try {
			Optional<Usuario> usu = userDao.findById(usuario.getId());
			usuario.setLogin(usu.get().getLogin());
			usuario.setClave(usu.get().getClave());
			usuario.setF_creacion(usu.get().getF_creacion());
			userDao.save(usuario);
		}catch(Exception e) {
			return e.toString();
		}
		return "OK";
	}
	
	@RequestMapping(value="/alta", method=RequestMethod.GET)
	public ModelAndView alta_usuario_GET() {
		return new ModelAndView("alta_usuario", "usuario", new Usuario());
	}
	
	@RequestMapping(value="/alta", method=RequestMethod.POST)
	@ResponseBody
	public String alta_usuario_POST(@ModelAttribute("Usuario")Usuario usuario) {
		try {
			usuario.setF_creacion(new Date());
			userDao.save(usuario);
		}catch(Exception e) {
			return e.toString();
		}
		return "OK";
	}
	
	@RequestMapping(value="/gestion", method=RequestMethod.GET)
	public ModelAndView login_GET() {
		return new ModelAndView("login", "login", new Login());
	}
	
	@RequestMapping(value="/gestion", method=RequestMethod.POST)
	public ModelAndView login_POST(@ModelAttribute("login")Login login) {
		return new ModelAndView("usuario", "login", login);
	}
}
