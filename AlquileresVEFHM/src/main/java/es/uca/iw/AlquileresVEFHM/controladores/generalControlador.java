package es.uca.iw.AlquileresVEFHM.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class generalControlador {
	@RequestMapping("/")
    public String index() {
        return "index";
    }
	
	@RequestMapping(value="/acceso-denegado")
	public ModelAndView AccesoDenegado() {
		return new ModelAndView("acceso-denegado");
	}

	
}
