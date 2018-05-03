package es.uca.iw.AlquileresVEFHM.modelos;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface UsuarioDao extends CrudRepository<Usuario, Integer> {
}
