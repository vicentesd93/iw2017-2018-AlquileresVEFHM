package es.uca.iw.AlquileresVEFHM.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.uca.iw.AlquileresVEFHM.modelos.Login;

@Controller
public class gestion_usuarioControlador {
	
	@RequestMapping("/")
    public String index() {
        return "index";
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
