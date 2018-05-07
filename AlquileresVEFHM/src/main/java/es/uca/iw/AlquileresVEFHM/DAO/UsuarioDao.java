package es.uca.iw.AlquileresVEFHM.DAO;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import es.uca.iw.AlquileresVEFHM.modelos.Usuario;

@Transactional
public interface UsuarioDao extends CrudRepository<Usuario, Integer> {
	Usuario findByLogin(String login);
	Usuario findByEmail(String email);
}
