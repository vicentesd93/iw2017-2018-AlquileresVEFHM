package es.uca.iw.AlquileresVEFHM.controladores;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.uca.iw.AlquileresVEFHM.modelos.Usuario;
import es.uca.iw.AlquileresVEFHM.DAO.UsuarioDAO;

@Controller
public class gestion_usuarioControlador {
	@Autowired
	private UsuarioDAO userDao;
	
	/*@RequestMapping(value="/modificar/{id}", method=RequestMethod.GET)
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
	}*/
	
	@RequestMapping(value="/registro", method=RequestMethod.GET)
	public ModelAndView alta_usuario_GET() {
		return new ModelAndView("alta_usuario", "usuario", new Usuario());
	}
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@RequestMapping(value="/registro", method=RequestMethod.POST)
	public ModelAndView alta_usuario_POST(@Valid Usuario usuario, BindingResult br) {
		ModelAndView modelAndView = new ModelAndView();
		if(userDao.findByLogin(usuario.getLogin()) != null) {
			br.rejectValue("login", "error.login", "El nombre de usuario ya esta en uso.");
		}
		if(userDao.findByEmail(usuario.getEmail()) != null) {
			br.rejectValue("email", "error.email", "El correo eléctronico ya esta asociado a una cuenta.");
		}
		if (br.hasErrors()) {
			modelAndView.setViewName("alta_usuario");
		} else {
			usuario.setF_creacion(new Date());
			usuario.setActivo(true); 
			usuario.setClave(bCryptPasswordEncoder.encode(usuario.getClave()));
			userDao.save(usuario);
			modelAndView.addObject("exito", "Usuario registrado correctamente");
			modelAndView.addObject("usuario", new Usuario());
			modelAndView.setViewName("alta_usuario");
			
		}
		return modelAndView;
	}
}
