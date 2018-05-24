package es.uca.iw.AlquileresVEFHM.seguridad;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.uca.iw.AlquileresVEFHM.DAO.UserDAO;
import es.uca.iw.AlquileresVEFHM.modelos.User;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		User u = userDao.findByLogin(username);
		if(u == null) throw new UsernameNotFoundException(username);
		return u;
	}
	
	public User save(User user) {
		user.setClave(passwordEncoder.encode(user.getPassword() != null ? user.getPassword() : "default"));
		user.setF_creacion(new Date());
		user.setActivo(true);
		return userDao.save(user);
	}
	
	
}
