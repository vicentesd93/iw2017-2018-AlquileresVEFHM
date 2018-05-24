package es.uca.iw.AlquileresVEFHM.DAO;

import org.springframework.data.repository.CrudRepository;

import es.uca.iw.AlquileresVEFHM.modelos.User;

public interface UserDAO extends CrudRepository<User, Integer> {
	User findByLogin(String login);
	User findByEmail(String email);
}