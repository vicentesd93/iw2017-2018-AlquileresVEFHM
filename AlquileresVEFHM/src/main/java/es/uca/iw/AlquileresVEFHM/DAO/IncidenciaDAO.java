package es.uca.iw.AlquileresVEFHM.DAO;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import es.uca.iw.AlquileresVEFHM.modelos.Incidencia;
import es.uca.iw.AlquileresVEFHM.modelos.User;

public interface IncidenciaDAO extends CrudRepository<Incidencia, Integer> {
	List<Incidencia> findByemisor(User emisor);
	List<Incidencia> findByreceptor(User receptor);
	List<Incidencia> findAll();
}
