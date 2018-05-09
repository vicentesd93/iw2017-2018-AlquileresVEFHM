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
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@RequestMapping(value = "/registro", method = RequestMethod.GET)
	public ModelAndView alta_usuario_GET() {
		return new ModelAndView("alta_usuario", "usuario", new Usuario());
	}
	
	@RequestMapping(value = "/registro", method = RequestMethod.POST)
	public ModelAndView alta_usuario_POST(@Valid Usuario usuario, BindingResult br) {
		ModelAndView mav = new ModelAndView();
		if(userDao.findByLogin(usuario.getLogin()) != null) {
			br.rejectValue("login", "error.login", "El nombre de usuario ya esta en uso.");
		}
		if(userDao.findByEmail(usuario.getEmail()) != null) {
			br.rejectValue("email", "error.email", "El correo eléctronico ya esta asociado a una cuenta.");
		}
		if (br.hasErrors()) {
			mav.setViewName("alta_usuario");
		} else {
			usuario.setF_creacion(new Date());
			usuario.setActivo(true); 
			usuario.setClave(bCryptPasswordEncoder.encode(usuario.getClave()));
			userDao.save(usuario);
			mav.addObject("exito", "Usuario registrado correctamente");
			mav.addObject("usuario", new Usuario());
			mav.setViewName("alta_usuario");		
		}
		return mav;
	}
}
