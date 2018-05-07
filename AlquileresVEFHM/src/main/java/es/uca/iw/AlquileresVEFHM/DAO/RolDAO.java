package es.uca.iw.AlquileresVEFHM.DAO;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import es.uca.iw.AlquileresVEFHM.modelos.Rol;

@Transactional
public interface RolDAO extends CrudRepository<Rol, Integer> {
	Rol findByNombre(String nombre);
	Optional<Rol> findById(Integer id);
}
