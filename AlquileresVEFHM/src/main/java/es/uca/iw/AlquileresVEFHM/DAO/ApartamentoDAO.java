package es.uca.iw.AlquileresVEFHM.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;

public interface ApartamentoDAO extends CrudRepository<Apartamento, Integer> {
	@Query(value = "SELECT DISTINCT apartamento.* FROM apartamento INNER JOIN oferta ON apartamento._id = oferta.apartamento AND oferta.fecha >= DATE(NOW())", nativeQuery = true)
	List<Apartamento> apartamentosOfertados();
}
