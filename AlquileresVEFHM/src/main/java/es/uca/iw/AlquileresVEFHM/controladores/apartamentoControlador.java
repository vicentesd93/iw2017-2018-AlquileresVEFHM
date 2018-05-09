package es.uca.iw.AlquileresVEFHM.controladores;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import es.uca.iw.AlquileresVEFHM.DAO.ApartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.Foto_apartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.Tipo_apartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.UsuarioDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Foto_apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Tipo_apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Usuario;

@Controller
public class apartamentoControlador {
	@Autowired
	private ApartamentoDAO aparDao;
	@Autowired
	private Tipo_apartamentoDAO t_aparDao;
	@Autowired
	private Foto_apartamentoDAO f_aparDao;
	@Autowired
	private UsuarioDAO userDao;
	
	@RequestMapping(value = "/apartamento/registro", method = RequestMethod.GET)
	public ModelAndView registro_GET() {
		ModelAndView mav = new ModelAndView("alta_apartamento");
		mav.addObject("apartamento", new Apartamento());
		mav.addObject("tipos", (ArrayList<Tipo_apartamento>)t_aparDao.findAll());
		return mav;
	}
	
	@RequestMapping(value = "/apartamento/registro", method = RequestMethod.POST)
	public ModelAndView registro_POST(Principal principal, @RequestParam("fotos") List<MultipartFile> fotos, @Valid Apartamento apartamento, BindingResult br) {
		ModelAndView mav = new ModelAndView();
		if(br.hasErrors()) {
			mav.addObject("tipos", (ArrayList<Tipo_apartamento>)t_aparDao.findAll());
			mav.setViewName("alta_apartamento");
		}else {
			Apartamento apar = null;
			try {
				Usuario usu = userDao.findByLogin(principal.getName());
				apartamento.setAnfitrion(usu.getId());
				apar = aparDao.save(apartamento);
				for (MultipartFile foto : fotos) {
					String nombre = usu.getId()+"_"+apar.getId()+"_"+System.currentTimeMillis()+"."+foto.getOriginalFilename().split("\\.")[1];
					Foto_apartamento fa = new Foto_apartamento();
					fa.setApartamento(apar.getId());
					fa.setNombre(nombre);
					fa.setFoto(new SerialBlob(foto.getBytes()));
					f_aparDao.save(fa);
				}
			} catch(Exception e) {
				if(apar != null) {
					for(Foto_apartamento foto : f_aparDao.findByApartamento(apar.getId())) f_aparDao.delete(foto);
					aparDao.delete(apar);
				}
			}
			mav.addObject("apartamento", new Apartamento());
			mav.addObject("tipos", (ArrayList<Tipo_apartamento>)t_aparDao.findAll());
			mav.addObject("exito", "Apartamento registrado correctamente");
			mav.setViewName("alta_apartamento");
		}
		return mav;
	}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class NotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}
	
	@RequestMapping(value = "/fotos/{img:.+}")
	@ResponseBody
	public byte[] mostrarfoto(@PathVariable String img, HttpServletResponse res) throws Exception{
		Foto_apartamento foto = f_aparDao.findByNombre(img);
		if(foto == null) throw new NotFoundException();
		return foto.getFoto().getBytes(1, (int)foto.getFoto().length());
	}
	
	@RequestMapping(value = "/apartamento/ver", method = RequestMethod.GET)
	public ModelAndView ver_GET(Principal principal) {
		ModelAndView mav = new ModelAndView("apartamento");
		Usuario usu = userDao.findByLogin(principal.getName());
		List<Apartamento> aparts = aparDao.findByAnfitrion(usu.getId());
		List<Foto_apartamento> fotos = f_aparDao.findByApartamento(aparts.get(0).getId());
		mav.addObject("apartamento", aparts.get(0));
		mav.addObject("fotos", fotos);
		return mav;
	}
}
